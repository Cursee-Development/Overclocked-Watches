package com.cursee.overclocked_watches.core.registry;

import net.minecraft.core.particles.SimpleParticleType;

public class ModParticlesFabric {

    public static void register() {}

    public static final SimpleParticleType GOLDEN_WATCH_GROWTH = RegistryFabric.registerParticle("golden_watch_growth", () -> new SimpleParticleType(false){});
    public static final SimpleParticleType DIAMOND_WATCH_GROWTH = RegistryFabric.registerParticle("diamond_watch_growth", () -> new SimpleParticleType(false){});
    public static final SimpleParticleType NETHERITE_WATCH_GROWTH = RegistryFabric.registerParticle("netherite_watch_growth", () -> new SimpleParticleType(false){});
}
