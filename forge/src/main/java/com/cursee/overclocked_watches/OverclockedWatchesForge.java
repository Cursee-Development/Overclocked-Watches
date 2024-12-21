package com.cursee.overclocked_watches;

import com.cursee.monolib.core.sailing.Sailing;
import com.cursee.overclocked_watches.core.registry.RegistryForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(Constants.MOD_ID)
public class OverclockedWatchesForge {
    
    public OverclockedWatchesForge() {
        OverclockedWatches.init();
        Sailing.register(Constants.MOD_NAME, Constants.MOD_ID, Constants.MOD_VERSION, Constants.MC_VERSION_RAW, Constants.PUBLISHER_AUTHOR, Constants.PRIMARY_CURSEFORGE_MODRINTH);
        RegistryForge.register(FMLJavaModLoadingContext.get().getModEventBus());
    }
}