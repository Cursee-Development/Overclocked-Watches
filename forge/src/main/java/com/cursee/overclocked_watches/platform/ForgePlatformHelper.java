package com.cursee.overclocked_watches.platform;

import com.cursee.overclocked_watches.OverclockedWatches;
import com.cursee.overclocked_watches.client.item.renderer.IWatchRenderer;
import com.cursee.overclocked_watches.core.registry.ModItemsForge;
import com.cursee.overclocked_watches.core.registry.ModParticlesForge;
import com.cursee.overclocked_watches.platform.services.IPlatformHelper;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.util.Tuple;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.loading.FMLLoader;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.SlotContext;
import top.theillusivec4.curios.api.SlotResult;
import top.theillusivec4.curios.api.client.CuriosRendererRegistry;
import top.theillusivec4.curios.api.client.ICurioRenderer;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Supplier;

public class ForgePlatformHelper implements IPlatformHelper {

    @Override
    public String getPlatformName() {

        return "Forge";
    }

    @Override
    public boolean isModLoaded(String modId) {

        return ModList.get().isLoaded(modId);
    }

    @Override
    public boolean isDevelopmentEnvironment() {

        return !FMLLoader.isProduction();
    }

    @Override
    public IWatchRenderer getWatchRenderer(Item item) {
        Optional<ICurioRenderer> renderer = CuriosRendererRegistry.getRenderer(item);
        if (renderer.isPresent() && renderer.get() instanceof WatchCurioRenderer artifactTrinketRenderer) {
            return artifactTrinketRenderer.renderer();
        }
        return null;
    }

    @Override
    public <T extends Item> void registerWatchRenderer(T item, Supplier<IWatchRenderer> rendererSupplier) {
        CuriosRendererRegistry.register(item, () -> new WatchCurioRenderer(rendererSupplier.get()));
    }

    @Override
    public boolean playerHasGoldenWatchEquipped(Player player) {

        final AtomicBoolean foundWatch = new AtomicBoolean(false);
        CuriosApi.getCuriosInventory(player).ifPresent(iCuriosItemHandler -> {
            if (iCuriosItemHandler.isEquipped(ModItemsForge.GOLDEN_WATCH.get())) foundWatch.set(true);
        });

        return foundWatch.get();
    }

    @Override
    public boolean playerHasDiamondWatchEquipped(Player player) {

        final AtomicBoolean foundWatch = new AtomicBoolean(false);
        CuriosApi.getCuriosInventory(player).ifPresent(iCuriosItemHandler -> {
            if (iCuriosItemHandler.isEquipped(ModItemsForge.DIAMOND_WATCH.get())) foundWatch.set(true);
        });

        return foundWatch.get();
    }

    @Override
    public boolean playerHasNetheriteWatchEquipped(Player player) {

        final AtomicBoolean foundWatch = new AtomicBoolean(false);
        CuriosApi.getCuriosInventory(player).ifPresent(iCuriosItemHandler -> {
            if (iCuriosItemHandler.isEquipped(ModItemsForge.NETHERITE_WATCH.get())) foundWatch.set(true);
        });

        return foundWatch.get();
    }

    @Override
    public SimpleParticleType getGoldenWatchGrowthParticle() {
        return ModParticlesForge.GOLDEN_WATCH_GROWTH.get();
    }

    @Override
    public SimpleParticleType getDiamondWatchGrowthParticle() {
        return ModParticlesForge.DIAMOND_WATCH_GROWTH.get();
    }

    @Override
    public SimpleParticleType getNetheriteWatchGrowthParticle() {
        return ModParticlesForge.NETHERITE_WATCH_GROWTH.get();
    }

    @Override
    public boolean consumeWatchCharge(Player player) {
//        CuriosApi.getCuriosInventory(player).ifPresent(iCuriosItemHandler -> {
//            if (iCuriosItemHandler.isEquipped(ModItemsForge.NETHERITE_WATCH.get())) {
//                Optional<SlotResult> equippedWatch = iCuriosItemHandler.findFirstCurio(ModItemsForge.NETHERITE_WATCH.get());
//                equippedWatch.ifPresent(slotResult -> {
//                    slotResult.stack().getOrCreateTagElement("watch_charge.5");
//                });
//            }
//        });

        final AtomicBoolean CHARGE_CONSUMED = new AtomicBoolean(false);
        CuriosApi.getCuriosInventory(player).ifPresent(iCuriosItemHandler -> {
            if (iCuriosItemHandler.isEquipped(ModItemsForge.NETHERITE_WATCH.get())) {
                iCuriosItemHandler.findFirstCurio(ModItemsForge.NETHERITE_WATCH.get()).ifPresent(slotResult -> {
                    if (OverclockedWatches.handleNetheriteWatchTag(slotResult.stack())) CHARGE_CONSUMED.set(true);
                });
            }
            else if (iCuriosItemHandler.isEquipped(ModItemsForge.DIAMOND_WATCH.get())) {
                iCuriosItemHandler.findFirstCurio(ModItemsForge.DIAMOND_WATCH.get()).ifPresent(slotResult -> {
                    if (OverclockedWatches.handleDiamondWatchTag(slotResult.stack())) CHARGE_CONSUMED.set(true);
                });
            }
            else if (iCuriosItemHandler.isEquipped(ModItemsForge.GOLDEN_WATCH.get())) {
                iCuriosItemHandler.findFirstCurio(ModItemsForge.GOLDEN_WATCH.get()).ifPresent(slotResult -> {
                    if (OverclockedWatches.handleGoldenWatchTag(slotResult.stack())) CHARGE_CONSUMED.set(true);
                });
            }
        });

        return CHARGE_CONSUMED.get();
    }

    private record WatchCurioRenderer(IWatchRenderer renderer) implements ICurioRenderer {

        @Override
        public <T extends LivingEntity, M extends EntityModel<T>> void render(ItemStack stack, SlotContext slotContext, PoseStack poseStack, RenderLayerParent<T, M> renderLayerParent, MultiBufferSource multiBufferSource, int light, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
            renderer.render(stack, slotContext.entity(), slotContext.index(), poseStack, multiBufferSource, light, limbSwing, limbSwingAmount, partialTicks, ageInTicks, netHeadYaw, headPitch);
        }
    }
}