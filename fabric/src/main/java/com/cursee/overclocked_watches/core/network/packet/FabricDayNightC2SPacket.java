package com.cursee.overclocked_watches.core.network.packet;

import com.cursee.overclocked_watches.core.registry.ModItemsFabric;
import com.cursee.overclocked_watches.platform.Services;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class FabricDayNightC2SPacket {

    public static void handle(MinecraftServer server, ServerPlayer player, ServerGamePacketListenerImpl handler, FriendlyByteBuf buf, PacketSender responseSender) {

        // player has watch variant equipped
        final boolean hasNetheriteWatch = Services.PLATFORM.playerHasNetheriteWatchEquipped(player);
        final boolean hasDiamondWatch = Services.PLATFORM.playerHasDiamondWatchEquipped(player);
        final boolean hasGoldenWatch = Services.PLATFORM.playerHasGoldenWatchEquipped(player);
        if (!(hasNetheriteWatch || hasDiamondWatch || hasGoldenWatch)) return;

        // player does not have any watch variant on cooldown
        final boolean WATCHES_ON_COOLDOWN = player.getCooldowns().isOnCooldown(ModItemsFabric.NETHERITE_WATCH) || player.getCooldowns().isOnCooldown(ModItemsFabric.DIAMOND_WATCH) || player.getCooldowns().isOnCooldown(ModItemsFabric.GOLDEN_WATCH);
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



//        if (player.getCooldowns().isOnCooldown(ModItemsFabric.NETHERITE_WATCH) || player.getCooldowns().isOnCooldown(ModItemsFabric.DIAMOND_WATCH) || player.getCooldowns().isOnCooldown(ModItemsFabric.GOLDEN_WATCH)) return;
//
//        // if (!Services.PLATFORM.consumeWatchCharge(player)) return;
//        if (!(Services.PLATFORM.getEquippedNetheriteWatch(player).hurt(10, player.getRandom(), player) || Services.PLATFORM.getEquippedDiamondWatch(player).hurt(10, player.getRandom(), player) || Services.PLATFORM.getEquippedGoldenWatch(player).hurt(10, player.getRandom(), player))) return;
//
//        player.sendSystemMessage(Component.translatable("magic.overclocked_watches.charge_consumed"));
//
//        server.getAllLevels().forEach(level -> level.setDayTime((level.getDayTime() + 12_000L) % 24_000L));
//        if (Services.PLATFORM.playerHasNetheriteWatchEquipped(player)) {
//
//            if ((Services.PLATFORM.getEquippedNetheriteWatch(player).hurt(10, player.getRandom(), player))) return;
//
//            // Services.PLATFORM.getEquippedNetheriteWatch(player).hurtAndBreak(10, player, serverPlayer -> {});
//            // Services.PLATFORM.getEquippedNetheriteWatch(player).hurt(10, player.getRandom(), player);
//            applyCooldowns(player, 20 * 60 * 5); // 20 ticks per 60 seconds in a minute per 5 minutes
//        }
//        else if (Services.PLATFORM.playerHasDiamondWatchEquipped(player)) {
//
//            if ((Services.PLATFORM.getEquippedDiamondWatch(player).hurt(10, player.getRandom(), player))) return;
//
//            // Services.PLATFORM.getEquippedDiamondWatch(player).hurtAndBreak(10, player, serverPlayer -> {});
//            // Services.PLATFORM.getEquippedDiamondWatch(player).hurt(10, player.getRandom(), player);
//            applyCooldowns(player, 20 * 60 * 10); // 20 ticks per 60 seconds in a minute per 10 minutes
//        }
//        else if (Services.PLATFORM.playerHasGoldenWatchEquipped(player)) {
//
//            if ((Services.PLATFORM.getEquippedGoldenWatch(player).hurt(10, player.getRandom(), player))) return;
//
//            // Services.PLATFORM.getEquippedGoldenWatch(player).hurtAndBreak(15, player, serverPlayer -> {});
//            // Services.PLATFORM.getEquippedGoldenWatch(player).hurt(10, player.getRandom(), player);
//            applyCooldowns(player, 20 * 60 * 20); // 20 ticks per 60 seconds in a minute per 20 minutes
//        }
    }

    public static void applyCooldowns(Player player, int lengthInTicks) {
        player.getCooldowns().addCooldown(ModItemsFabric.NETHERITE_WATCH, lengthInTicks);
        player.getCooldowns().addCooldown(ModItemsFabric.DIAMOND_WATCH, lengthInTicks);
        player.getCooldowns().addCooldown(ModItemsFabric.GOLDEN_WATCH, lengthInTicks);
    }
}
