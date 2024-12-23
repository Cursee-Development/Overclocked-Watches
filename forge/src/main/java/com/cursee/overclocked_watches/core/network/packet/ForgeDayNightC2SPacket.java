package com.cursee.overclocked_watches.core.network.packet;

import com.cursee.overclocked_watches.core.registry.ModItemsForge;
import com.cursee.overclocked_watches.platform.Services;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class ForgeDayNightC2SPacket {

    public ForgeDayNightC2SPacket() {}

    public ForgeDayNightC2SPacket(FriendlyByteBuf buf) {}

    public void toBytes(FriendlyByteBuf buf) {}

    public boolean handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> {
            ServerPlayer player = context.getSender();
            MinecraftServer server = player.getServer();

            if (player.getCooldowns().isOnCooldown(ModItemsForge.NETHERITE_WATCH.get()) || player.getCooldowns().isOnCooldown(ModItemsForge.DIAMOND_WATCH.get()) || player.getCooldowns().isOnCooldown(ModItemsForge.GOLDEN_WATCH.get())) return;

            if (!Services.PLATFORM.consumeWatchCharge(player)) return;

            player.sendSystemMessage(Component.translatable("magic.overclocked_watches.charge_consumed"));

            server.getAllLevels().forEach(level -> level.setDayTime((level.getDayTime() + 12_000L) % 24_000L));
            if (Services.PLATFORM.playerHasNetheriteWatchEquipped(player)) applyCooldowns(player, 20*60*5); // 20 ticks per 60 seconds in a minute per 5 minutes
            if (Services.PLATFORM.playerHasDiamondWatchEquipped(player)) applyCooldowns(player, 20*60*10); // 20 ticks per 60 seconds in a minute per 10 minutes
            if (Services.PLATFORM.playerHasGoldenWatchEquipped(player)) applyCooldowns(player, 20*60*20); // 20 ticks per 60 seconds in a minute per 20 minutes
        });

        return true;
    }

    public static void applyCooldowns(Player player, int lengthInTicks) {
        player.getCooldowns().addCooldown(ModItemsForge.NETHERITE_WATCH.get(), lengthInTicks);
        player.getCooldowns().addCooldown(ModItemsForge.DIAMOND_WATCH.get(), lengthInTicks);
        player.getCooldowns().addCooldown(ModItemsForge.GOLDEN_WATCH.get(), lengthInTicks);
    }
}
