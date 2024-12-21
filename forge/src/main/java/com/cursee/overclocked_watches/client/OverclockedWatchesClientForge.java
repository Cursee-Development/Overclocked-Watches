package com.cursee.overclocked_watches.client;

import com.cursee.overclocked_watches.client.item.RendererLayers;
import com.cursee.overclocked_watches.client.item.RendererUtil;
import com.cursee.overclocked_watches.client.item.model.ArmsModel;
import com.cursee.overclocked_watches.client.item.renderer.WatchRenderer;
import com.cursee.overclocked_watches.core.registry.ModItemsForge;
import com.cursee.overclocked_watches.mixin.LivingEntityRendererAccessor;
import com.cursee.overclocked_watches.platform.Services;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.world.entity.EntityType;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import top.theillusivec4.curios.client.render.CuriosLayer;

import java.util.Set;

public class OverclockedWatchesClientForge {

    public OverclockedWatchesClientForge(IEventBus modEventBus) {

        modEventBus.addListener(this::onRegisterEntityLayerDefinitions);
        modEventBus.addListener(this::onClientSetup);
        modEventBus.addListener(this::onAddEntityRendererLayers);

        ArmRenderHandler.setup();
    }

    public void onRegisterEntityLayerDefinitions(EntityRenderersEvent.RegisterLayerDefinitions event) {

        event.registerLayerDefinition(RendererLayers.GOLDEN_WATCH_WIDE, RendererLayers.layer(() -> RendererUtil.createWatchModel(false), 32, 32));
        event.registerLayerDefinition(RendererLayers.GOLDEN_WATCH_SLIM, RendererLayers.layer(() -> RendererUtil.createWatchModel(true), 32, 32));
        event.registerLayerDefinition(RendererLayers.DIAMOND_WATCH_WIDE, RendererLayers.layer(() -> RendererUtil.createWatchModel(false), 32, 32));
        event.registerLayerDefinition(RendererLayers.DIAMOND_WATCH_SLIM, RendererLayers.layer(() -> RendererUtil.createWatchModel(true), 32, 32));
        event.registerLayerDefinition(RendererLayers.NETHERITE_WATCH_WIDE, RendererLayers.layer(() -> RendererUtil.createWatchModel(false), 32, 32));
        event.registerLayerDefinition(RendererLayers.NETHERITE_WATCH_SLIM, RendererLayers.layer(() -> RendererUtil.createWatchModel(true), 32, 32));
    }

    public void onClientSetup(FMLClientSetupEvent event) {
        event.enqueueWork(() -> {
            Services.PLATFORM.registerWatchRenderer(ModItemsForge.GOLDEN_WATCH.get(), () -> new WatchRenderer("golden_watch", ArmsModel::bakeGoldenWatchTextureOnModel));
            Services.PLATFORM.registerWatchRenderer(ModItemsForge.DIAMOND_WATCH.get(), () -> new WatchRenderer("diamond_watch", ArmsModel::bakeDiamondWatchTextureOnModel));
            Services.PLATFORM.registerWatchRenderer(ModItemsForge.NETHERITE_WATCH.get(), () -> new WatchRenderer("netherite_watch", ArmsModel::bakeNetheriteWatchTextureOnModel));
        });
    }

    public void onAddEntityRendererLayers(EntityRenderersEvent.AddLayers event) {
        Set<EntityType<?>> entities = Set.of(EntityType.ZOMBIE,
                EntityType.HUSK,
                EntityType.DROWNED,
                EntityType.SKELETON,
                EntityType.STRAY,
                EntityType.WITHER_SKELETON,
                EntityType.PIGLIN,
                EntityType.PIGLIN_BRUTE,
                EntityType.ZOMBIFIED_PIGLIN);
        loop:
        for (EntityType<?> entity : entities) {
            EntityRenderer<?> renderer = Minecraft.getInstance().getEntityRenderDispatcher().renderers.get(entity);
            if (renderer == null) {
                continue;
            }
            LivingEntityRenderer livingEntityRenderer = (LivingEntityRenderer<?, ?>) renderer;
            for (RenderLayer<?, ?> layer : ((LivingEntityRendererAccessor<?, ?>) livingEntityRenderer).getLayers()) {
                if (layer instanceof CuriosLayer<?, ?>) {
                    continue loop;
                }
            }
            livingEntityRenderer.addLayer(new CuriosLayer<>(livingEntityRenderer));
        }
    }
}
