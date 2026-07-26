package io.github.jason13official.overclocked_watches.impl.common.item;

import io.github.jason13official.overclocked_watches.Constants;
import io.github.jason13official.overclocked_watches.api.common.data.WatchItemData;
import io.github.jason13official.overclocked_watches.impl.common.registry.ModDataComponents;
import io.github.jason13official.overclocked_watches.impl.common.registry.ModItems;
import io.github.jason13official.overclocked_watches.impl.common.util.OverclockedWatchesUtil;
import io.github.jason13official.overclocked_watches.platform.Services;
import java.util.function.Consumer;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipDisplay;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

public class WatchItem extends Item {

  private final WatchTier tier;

  public WatchItem(WatchTier tier) {
    super(new Properties().durability(tier.getItemDurability()).component(ModDataComponents.CHARGES, tier.getWatchCharges()));
    this.tier = tier;
  }

  /// Helper method to reduce spamming similar lines, apply same cooldown for all watches
  public static void applyCooldowns(Player player, int lengthInTicks) {
    player.getCooldowns().addCooldown(new ItemStack(ModItems.GOLDEN_WATCH), lengthInTicks);
    player.getCooldowns().addCooldown(new ItemStack(ModItems.DIAMOND_WATCH), lengthInTicks);
    player.getCooldowns().addCooldown(new ItemStack(ModItems.NETHERITE_WATCH), lengthInTicks);
  }

  @Override
  public ItemStack getDefaultInstance() {

    ItemStack itemStack = new ItemStack(this);

    return WatchItemData.initializeCharges(this, itemStack);
  }

  @Override
  public void appendHoverText(ItemStack itemStack, Item.TooltipContext context, TooltipDisplay display, Consumer<Component> builder, TooltipFlag tooltipFlag) {

    if (!itemStack.has(ModDataComponents.CHARGES)) {
      builder.accept(Component.translatable("text.overclocked_watches.default_charges", this.getTier().getWatchCharges()));
    } else {
      builder.accept(Component.translatable("text.overclocked_watches.charges", itemStack.get(ModDataComponents.CHARGES)));
    }
  }

  @Override
  public void inventoryTick(ItemStack itemStack, ServerLevel level, Entity entity, @Nullable EquipmentSlot slot) {

    WatchItemData.initializeCharges(this, itemStack);
  }

  // interaction when clicking in the air
  @Override
  public InteractionResult use(Level level, Player player, InteractionHand hand) {

    ItemStack itemInHand = player.getItemInHand(hand);
    if (!(level instanceof ServerLevel serverLevel) || !(player instanceof ServerPlayer serverPlayer)) {
      return InteractionResult.PASS;
    }

    // task: validate charges, update time, consume charge, apply cooldowns

    if (!itemInHand.has(ModDataComponents.CHARGES)) {
      itemInHand = WatchItemData.initializeCharges(this, itemInHand);
    } else if (itemInHand.get(ModDataComponents.CHARGES) == 0) {
      return InteractionResult.PASS;
    }
    // we have validated charges to be either initialized or non-zero

    OverclockedWatchesUtil.advanceDayTime(serverLevel, this.getTier().getTimeAdvancementTicks());
    // we have advanced the server daytime

    OverclockedWatchesUtil.consumeCharge(itemInHand);
    // we have consumed a charge

    applyCooldowns(serverPlayer, 20 * 60 * this.getTier().getCooldownMinutes());
    // we have applied cooldowns

    if (Services.PLATFORM.isDevelopmentEnvironment()) {
      Constants.LOG.info("[OverclockedWatches] {} used {}, cooldown={}t, charges={}", serverPlayer.getGameProfile().name(), this.getTier(),
          20 * 60 * this.getTier().getCooldownMinutes(), itemInHand.get(ModDataComponents.CHARGES));
    }

    serverPlayer.level().playLocalSound(player.position().x, player.position().y, player.position().z, SoundEvents.BELL_RESONATE, SoundSource.AMBIENT, 0.5f, 0.5f, false);
    serverPlayer.level().addParticle(ParticleTypes.END_ROD, player.position().x, player.position().y, player.position().z, 0, 0.005, 0);
    serverPlayer.sendSystemMessage(Component.translatable("magic.overclocked_watches.charge_consumed"));

    return InteractionResult.SUCCESS;
  }

  public WatchTier getTier() {

    return tier;
  }
}
