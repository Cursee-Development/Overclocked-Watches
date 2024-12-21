package com.cursee.overclocked_watches.client;

import com.cursee.overclocked_watches.Constants;
import com.cursee.overclocked_watches.client.item.model.ArmsModel;
import com.cursee.overclocked_watches.client.item.renderer.WatchRenderer;
import com.cursee.overclocked_watches.core.registry.ModItemsFabric;
import com.cursee.overclocked_watches.platform.Services;
import net.fabricmc.fabric.api.resource.SimpleSynchronousResourceReloadListener;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;

public class TrinketRenderers implements SimpleSynchronousResourceReloadListener {

    @Override
    public void onResourceManagerReload(ResourceManager resourceManager) {
        Services.PLATFORM.registerWatchRenderer(ModItemsFabric.GOLDEN_WATCH, () -> new WatchRenderer("golden_watch", ArmsModel::bakeGoldenWatchTextureOnModel));
        Services.PLATFORM.registerWatchRenderer(ModItemsFabric.DIAMOND_WATCH, () -> new WatchRenderer("diamond_watch", ArmsModel::bakeDiamondWatchTextureOnModel));
        Services.PLATFORM.registerWatchRenderer(ModItemsFabric.NETHERITE_WATCH, () -> new WatchRenderer("netherite_watch", ArmsModel::bakeNetheriteWatchTextureOnModel));
    }

    @Override
    public ResourceLocation getFabricId() {
        return new ResourceLocation(Constants.MOD_ID, "overclocked_watches_renderers");
    }
}
