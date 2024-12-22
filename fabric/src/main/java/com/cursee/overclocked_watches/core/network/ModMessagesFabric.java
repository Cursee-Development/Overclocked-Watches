package com.cursee.overclocked_watches.core.network;

import com.cursee.overclocked_watches.Constants;
import com.cursee.overclocked_watches.core.network.packet.FabricDayNightC2SPacket;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.resources.ResourceLocation;

public class ModMessagesFabric {

    public static final ResourceLocation DAY_NIGHT = new ResourceLocation(Constants.MOD_ID, "day_night");

    public static void registerClientToServerPackets() {
        ServerPlayNetworking.registerGlobalReceiver(DAY_NIGHT, FabricDayNightC2SPacket::handle);
    }
}
