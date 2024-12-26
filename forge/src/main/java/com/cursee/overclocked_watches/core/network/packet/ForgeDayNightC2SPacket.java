package com.cursee.overclocked_watches.core.network.packet;

import com.cursee.overclocked_watches.ConfigForge;
import com.cursee.overclocked_watches.core.registry.ModItemsForge;
import com.cursee.overclocked_watches.platform.Services;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.network.NetworkEvent;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.common.inventory.CurioSlot;
import top.theillusivec4.curios.server.SlotHelper;

import java.util.function.Supplier;

public class ForgeDayNightC2SPacket {

    public ForgeDayNightC2SPacket() {}

    public ForgeDayNightC2SPacket(FriendlyByteBuf buf) {}

    public void toBytes(FriendlyByteBuf buf) {}

    public boolean handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> {

            if (!ConfigForge.DAY_NIGHT_CHANGE_ALLOWED.get()) return;

            ServerPlayer player = context.getSender();

            // player has watch variant equipped
            final boolean hasNetheriteWatch = Services.PLATFORM.playerHasNetheriteWatchEquipped(player);
            final boolean hasDiamondWatch = Services.PLATFORM.playerHasDiamondWatchEquipped(player);
            final boolean hasGoldenWatch = Services.PLATFORM.playerHasGoldenWatchEquipped(player);
            if (!(hasNetheriteWatch || hasDiamondWatch || hasGoldenWatch)) return;

            // player does not have any watch variant on cooldown
            final boolean WATCHES_ON_COOLDOWN = player.getCooldowns().isOnCooldown(ModItemsForge.NETHERITE_WATCH.get()) || player.getCooldowns().isOnCooldown(ModItemsForge.DIAMOND_WATCH.get()) || player.getCooldowns().isOnCooldown(ModItemsForge.GOLDEN_WATCH.get());
            if (WATCHES_ON_COOLDOWN) return;

            player.serverLevel().setDayTime((player.serverLevel().getDayTime() + 12_000L) % 24_000L);

            if (hasNetheriteWatch) {
                ItemStack equippedWatch = Services.PLATFORM.getEquippedNetheriteWatch(player);
                equippedWatch.hurt(1, player.getRandom(), player);
                if (equippedWatch.getDamageValue() >= equippedWatch.getMaxDamage()) equippedWatch.shrink(1);
                applyCooldowns(player, 20 * 60 * 5);
            }
            else if (hasDiamondWatch) {
                ItemStack equippedWatch = Services.PLATFORM.getEquippedDiamondWatch(player);
                equippedWatch.hurt(1, player.getRandom(), player);
                if (equippedWatch.getDamageValue() >= equippedWatch.getMaxDamage()) equippedWatch.shrink(1);
                applyCooldowns(player, 20 * 60 * 10);
            }
            else if (hasGoldenWatch) {
                ItemStack equippedWatch = Services.PLATFORM.getEquippedGoldenWatch(player);
                equippedWatch.hurt(1, player.getRandom(), player);
                if (equippedWatch.getDamageValue() >= equippedWatch.getMaxDamage()) equippedWatch.shrink(1);
                applyCooldowns(player, 20 * 60 * 20);
            }

            player.sendSystemMessage(Component.translatable("magic.overclocked_watches.charge_consumed"));

//            if (player.getCooldowns().isOnCooldown(ModItemsForge.NETHERITE_WATCH.get()) || player.getCooldowns().isOnCooldown(ModItemsForge.DIAMOND_WATCH.get()) || player.getCooldowns().isOnCooldown(ModItemsForge.GOLDEN_WATCH.get())) return;
//
//            if (!Services.PLATFORM.consumeWatchCharge(player)) return;
//
//            player.sendSystemMessage(Component.translatable("magic.overclocked_watches.charge_consumed"));
//
//            server.getAllLevels().forEach(level -> level.setDayTime((level.getDayTime() + 12_000L) % 24_000L));
//            if (Services.PLATFORM.playerHasNetheriteWatchEquipped(player)) {
//                Services.PLATFORM.getEquippedNetheriteWatch(player).hurtAndBreak(10, player, serverPlayer -> {});
//                applyCooldowns(player, 20 * 60 * 5); // 20 ticks per 60 seconds in a minute per 5 minutes
//            }
//            else if (Services.PLATFORM.playerHasDiamondWatchEquipped(player)) {
//                Services.PLATFORM.getEquippedDiamondWatch(player).hurtAndBreak(10, player, serverPlayer -> {});
//                applyCooldowns(player, 20 * 60 * 10); // 20 ticks per 60 seconds in a minute per 10 minutes
//            }
//            else if (Services.PLATFORM.playerHasGoldenWatchEquipped(player)) {
//                Services.PLATFORM.getEquippedGoldenWatch(player).hurtAndBreak(15, player, serverPlayer -> {});
//                applyCooldowns(player, 20 * 60 * 20); // 20 ticks per 60 seconds in a minute per 20 minutes
//            }
        });

        return true;
    }

    public static void applyCooldowns(Player player, int lengthInTicks) {
        player.getCooldowns().addCooldown(ModItemsForge.NETHERITE_WATCH.get(), lengthInTicks);
        player.getCooldowns().addCooldown(ModItemsForge.DIAMOND_WATCH.get(), lengthInTicks);
        player.getCooldowns().addCooldown(ModItemsForge.GOLDEN_WATCH.get(), lengthInTicks);
    }
}
