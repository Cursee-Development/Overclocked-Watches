package com.cursee.overclocked_watches;

import com.cursee.monolib.core.sailing.Sailing;
import com.cursee.overclocked_watches.core.network.ModMessagesFabric;
import com.cursee.overclocked_watches.core.registry.RegistryFabric;
import net.fabricmc.api.ModInitializer;

public class OverclockedWatchesFabric implements ModInitializer {
    
    @Override
    public void onInitialize() {
        OverclockedWatches.init();
        Sailing.register(Constants.MOD_NAME, Constants.MOD_ID, Constants.MOD_VERSION, Constants.MC_VERSION_RAW, Constants.PUBLISHER_AUTHOR, Constants.PRIMARY_CURSEFORGE_MODRINTH);
        RegistryFabric.register();
        ModMessagesFabric.registerClientToServerPackets();
    }
}
