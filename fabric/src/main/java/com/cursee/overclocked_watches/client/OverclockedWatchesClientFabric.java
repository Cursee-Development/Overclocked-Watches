package com.cursee.overclocked_watches.client;

import com.cursee.overclocked_watches.client.item.RendererLayers;
import com.cursee.overclocked_watches.client.item.RendererUtil;
import com.cursee.overclocked_watches.core.particle.custom.WatchGrowthParticle;
import com.cursee.overclocked_watches.core.registry.ModParticlesFabric;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.minecraft.server.packs.PackType;

public class OverclockedWatchesClientFabric implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        OverclockedWatchesClientFabric.registerModelLayers();
        ParticleFactoryRegistry.getInstance().register(ModParticlesFabric.GOLDEN_WATCH_GROWTH, WatchGrowthParticle.HappyVillagerParticleCopiedProvider::new);
        ParticleFactoryRegistry.getInstance().register(ModParticlesFabric.DIAMOND_WATCH_GROWTH, WatchGrowthParticle.HappyVillagerParticleCopiedProvider::new);
        ParticleFactoryRegistry.getInstance().register(ModParticlesFabric.NETHERITE_WATCH_GROWTH, WatchGrowthParticle.HappyVillagerParticleCopiedProvider::new);
        ResourceManagerHelper.get(PackType.CLIENT_RESOURCES).registerReloadListener(new TrinketRenderers());
    }

    public static void registerModelLayers() {
        EntityModelLayerRegistry.registerModelLayer(RendererLayers.GOLDEN_WATCH_WIDE, () -> RendererLayers.layer(() -> RendererUtil.createWatchModel(false), 32, 32).get());
        EntityModelLayerRegistry.registerModelLayer(RendererLayers.GOLDEN_WATCH_SLIM, () -> RendererLayers.layer(() -> RendererUtil.createWatchModel(true), 32, 32).get());
        EntityModelLayerRegistry.registerModelLayer(RendererLayers.DIAMOND_WATCH_WIDE, () -> RendererLayers.layer(() -> RendererUtil.createWatchModel(false), 32, 32).get());
        EntityModelLayerRegistry.registerModelLayer(RendererLayers.DIAMOND_WATCH_SLIM, () -> RendererLayers.layer(() -> RendererUtil.createWatchModel(true), 32, 32).get());
        EntityModelLayerRegistry.registerModelLayer(RendererLayers.NETHERITE_WATCH_WIDE, () -> RendererLayers.layer(() -> RendererUtil.createWatchModel(false), 32, 32).get());
        EntityModelLayerRegistry.registerModelLayer(RendererLayers.NETHERITE_WATCH_SLIM, () -> RendererLayers.layer(() -> RendererUtil.createWatchModel(true), 32, 32).get());
    }
}
