package com.cursee.overclocked_watches.core.item.custom;

import com.cursee.overclocked_watches.platform.Services;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class WatchItem extends Item {

    public WatchItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand interactionHand) {
        
        // operate on the server only
        if (!(level instanceof ServerLevel serverLevel)) return InteractionResultHolder.sidedSuccess(player.getItemInHand(interactionHand), level.isClientSide());
        ServerPlayer serverPlayer = (ServerPlayer) player;
        
        // player has watch variant equipped
        final boolean hasNetheriteWatch = Services.PLATFORM.playerHasNetheriteWatchEquipped(serverPlayer);
        final boolean hasDiamondWatch = Services.PLATFORM.playerHasDiamondWatchEquipped(serverPlayer);
        final boolean hasGoldenWatch = Services.PLATFORM.playerHasGoldenWatchEquipped(serverPlayer);
        if (!(hasNetheriteWatch || hasDiamondWatch || hasGoldenWatch)) return InteractionResultHolder.pass(serverPlayer.getItemInHand(interactionHand));

        // player does not have any watch variant on cooldown
        final boolean WATCHES_ON_COOLDOWN = serverPlayer.getCooldowns().isOnCooldown(Services.PLATFORM.getRegisteredNetheriteWatchItem().get()) || serverPlayer.getCooldowns().isOnCooldown(Services.PLATFORM.getRegisteredDiamondWatchItem().get()) || serverPlayer.getCooldowns().isOnCooldown(Services.PLATFORM.getRegisteredGoldenWatchItem().get());
        if (WATCHES_ON_COOLDOWN) return InteractionResultHolder.pass(serverPlayer.getItemInHand(interactionHand));

        serverLevel.setDayTime((serverLevel.getDayTime() + 12_000L) % 24_000L);

        if (hasNetheriteWatch) {
            ItemStack equippedWatch = Services.PLATFORM.getEquippedNetheriteWatch(serverPlayer);
            equippedWatch.hurt(1, serverPlayer.getRandom(), serverPlayer);
            if (equippedWatch.getDamageValue() >= equippedWatch.getMaxDamage()) equippedWatch.shrink(1);
            applyCooldowns(serverPlayer, 20 * 60 * 5);
        }
        else if (hasDiamondWatch) {
            ItemStack equippedWatch = Services.PLATFORM.getEquippedDiamondWatch(serverPlayer);
            equippedWatch.hurt(1, serverPlayer.getRandom(), serverPlayer);
            if (equippedWatch.getDamageValue() >= equippedWatch.getMaxDamage()) equippedWatch.shrink(1);
            applyCooldowns(serverPlayer, 20 * 60 * 10);
        }
        else if (hasGoldenWatch) {
            ItemStack equippedWatch = Services.PLATFORM.getEquippedGoldenWatch(serverPlayer);
            equippedWatch.hurt(1, serverPlayer.getRandom(), serverPlayer);
            if (equippedWatch.getDamageValue() >= equippedWatch.getMaxDamage()) equippedWatch.shrink(1);
            applyCooldowns(serverPlayer, 20 * 60 * 20);
        }

        serverPlayer.sendSystemMessage(Component.translatable("magic.overclocked_watches.charge_consumed"));

        return InteractionResultHolder.success(serverPlayer.getItemInHand(interactionHand));
    }

    public static void applyCooldowns(Player player, int lengthInTicks) {
        player.getCooldowns().addCooldown(Services.PLATFORM.getRegisteredNetheriteWatchItem().get(), lengthInTicks);
        player.getCooldowns().addCooldown(Services.PLATFORM.getRegisteredDiamondWatchItem().get(), lengthInTicks);
        player.getCooldowns().addCooldown(Services.PLATFORM.getRegisteredGoldenWatchItem().get(), lengthInTicks);
    }
}
