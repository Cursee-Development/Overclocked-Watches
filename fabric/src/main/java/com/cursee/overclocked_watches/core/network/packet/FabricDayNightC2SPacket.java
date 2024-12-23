package com.cursee.overclocked_watches.core.network.packet;

import com.cursee.overclocked_watches.core.registry.ModItemsFabric;
import com.cursee.overclocked_watches.platform.Services;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import net.minecraft.world.entity.player.Player;

public class FabricDayNightC2SPacket {

    public static void handle(MinecraftServer server, ServerPlayer player, ServerGamePacketListenerImpl handler, FriendlyByteBuf buf, PacketSender responseSender) {

        if (player.getCooldowns().isOnCooldown(ModItemsFabric.NETHERITE_WATCH) || player.getCooldowns().isOnCooldown(ModItemsFabric.DIAMOND_WATCH) || player.getCooldowns().isOnCooldown(ModItemsFabric.GOLDEN_WATCH)) return;

        if (!Services.PLATFORM.consumeWatchCharge(player)) return;

        player.sendSystemMessage(Component.translatable("magic.overclocked_watches.charge_consumed"));

        server.getAllLevels().forEach(level -> level.setDayTime((level.getDayTime() + 12_000L) % 24_000L));
        if (Services.PLATFORM.playerHasNetheriteWatchEquipped(player)) applyCooldowns(player, 20*60*5); // 20 ticks per 60 seconds in a minute per 5 minutes
        if (Services.PLATFORM.playerHasDiamondWatchEquipped(player)) applyCooldowns(player, 20*60*10); // 20 ticks per 60 seconds in a minute per 10 minutes
        if (Services.PLATFORM.playerHasGoldenWatchEquipped(player)) applyCooldowns(player, 20*60*20); // 20 ticks per 60 seconds in a minute per 20 minutes
    }

    public static void applyCooldowns(Player player, int lengthInTicks) {
        player.getCooldowns().addCooldown(ModItemsFabric.NETHERITE_WATCH, lengthInTicks);
        player.getCooldowns().addCooldown(ModItemsFabric.DIAMOND_WATCH, lengthInTicks);
        player.getCooldowns().addCooldown(ModItemsFabric.GOLDEN_WATCH, lengthInTicks);
    }
}
