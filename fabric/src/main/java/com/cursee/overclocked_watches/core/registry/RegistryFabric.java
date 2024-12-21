package com.cursee.overclocked_watches.core.registry;

import com.cursee.overclocked_watches.Constants;
import net.minecraft.core.Registry;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;

import java.util.function.Supplier;

public class RegistryFabric {

    public static void register() {
        ModItemsFabric.register();
        ModParticlesFabric.register();
        ModTabsFabric.register();
    }

    protected static <T extends Item> T registerItem(String itemID, Supplier<T> itemSupplier) {
        return Registry.register(BuiltInRegistries.ITEM, new ResourceLocation(Constants.MOD_ID, itemID), itemSupplier.get());
    }

    protected static <T extends CreativeModeTab> T registerTab(String tabID, Supplier<T> tabSupplier) {
        return Registry.register(BuiltInRegistries.CREATIVE_MODE_TAB, new ResourceLocation(Constants.MOD_ID, tabID), tabSupplier.get());
    }

    protected static <T extends SimpleParticleType> T registerParticle(String particleID, Supplier<T> particleSupplier) {
        return Registry.register(BuiltInRegistries.PARTICLE_TYPE, new ResourceLocation(Constants.MOD_ID, particleID), particleSupplier.get());
    }
}
