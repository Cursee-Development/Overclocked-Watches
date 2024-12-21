package com.cursee.overclocked_watches;

import com.cursee.monolib.core.sailing.Sailing;
import com.cursee.overclocked_watches.client.OverclockedWatchesClientForge;
import com.cursee.overclocked_watches.core.curio.WearableWatchCurio;
import com.cursee.overclocked_watches.core.item.custom.WatchItem;
import com.cursee.overclocked_watches.core.registry.RegistryForge;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLEnvironment;
import top.theillusivec4.curios.api.CuriosCapability;
import top.theillusivec4.curios.common.capability.CurioItemCapability;

@Mod(Constants.MOD_ID)
public class OverclockedWatchesForge {
    
    public OverclockedWatchesForge() {
        OverclockedWatches.init();
        Sailing.register(Constants.MOD_NAME, Constants.MOD_ID, Constants.MOD_VERSION, Constants.MC_VERSION_RAW, Constants.PUBLISHER_AUTHOR, Constants.PRIMARY_CURSEFORGE_MODRINTH);

        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        RegistryForge.register(modEventBus);
        if (FMLEnvironment.dist == Dist.CLIENT) new OverclockedWatchesClientForge(modEventBus);

        MinecraftForge.EVENT_BUS.addGenericListener(ItemStack.class, this::onAttachCapabilities);
    }

    private void onAttachCapabilities(AttachCapabilitiesEvent<ItemStack> event) {
        if (event.getObject().getItem() instanceof WatchItem item) {
            event.addCapability(CuriosCapability.ID_ITEM, CurioItemCapability.createProvider(new WearableWatchCurio(item, event.getObject())));
        }
    }
}