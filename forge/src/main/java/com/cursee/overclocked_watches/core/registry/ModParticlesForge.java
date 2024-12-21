package com.cursee.overclocked_watches.core.registry;

import net.minecraft.core.particles.SimpleParticleType;
import net.minecraftforge.registries.RegistryObject;

public class ModParticlesForge {

    public static void register() {}

    public static final RegistryObject<SimpleParticleType> GOLDEN_WATCH_GROWTH = RegistryForge.registerParticle("golden_watch_growth", () -> new SimpleParticleType(false){});
    public static final RegistryObject<SimpleParticleType> DIAMOND_WATCH_GROWTH = RegistryForge.registerParticle("diamond_watch_growth", () -> new SimpleParticleType(false){});
    public static final RegistryObject<SimpleParticleType> NETHERITE_WATCH_GROWTH = RegistryForge.registerParticle("netherite_watch_growth", () -> new SimpleParticleType(false){});
}
