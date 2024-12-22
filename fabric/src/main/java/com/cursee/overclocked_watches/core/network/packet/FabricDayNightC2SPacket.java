package com.cursee.overclocked_watches.core.network.packet;

import com.cursee.overclocked_watches.core.registry.ModItemsFabric;
import com.cursee.overclocked_watches.platform.Services;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;

public class FabricDayNightC2SPacket {

    public static void handle(MinecraftServer server, ServerPlayer player, ServerGamePacketListenerImpl handler, FriendlyByteBuf buf, PacketSender responseSender) {

        if (player.getCooldowns().isOnCooldown(ModItemsFabric.NETHERITE_WATCH) || player.getCooldowns().isOnCooldown(ModItemsFabric.DIAMOND_WATCH) || player.getCooldowns().isOnCooldown(ModItemsFabric.GOLDEN_WATCH)) return;

        server.getAllLevels().forEach(level -> level.setDayTime((level.getDayTime() + 12_000L) % 24_000L));
        if (Services.PLATFORM.playerHasNetheriteWatchEquipped(player)) player.getCooldowns().addCooldown(ModItemsFabric.NETHERITE_WATCH, 20*60*5); // 20 ticks per 60 seconds in a minute per 5 minutes
        if (Services.PLATFORM.playerHasDiamondWatchEquipped(player)) player.getCooldowns().addCooldown(ModItemsFabric.DIAMOND_WATCH, 20*60*10); // 20 ticks per 60 seconds in a minute per 10 minutes
        if (Services.PLATFORM.playerHasGoldenWatchEquipped(player)) player.getCooldowns().addCooldown(ModItemsFabric.GOLDEN_WATCH, 20*60*20); // 20 ticks per 60 seconds in a minute per 20 minutes

        // todo: consume charge from watch
        // Services.PLATFORM.consumeWatchCharge(player);
    }
}
