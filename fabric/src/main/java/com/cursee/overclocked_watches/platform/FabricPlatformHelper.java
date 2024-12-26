package com.cursee.overclocked_watches.platform;

import com.cursee.overclocked_watches.OverclockedWatches;
import com.cursee.overclocked_watches.client.item.renderer.IWatchRenderer;
import com.cursee.overclocked_watches.core.registry.ModItemsFabric;
import com.cursee.overclocked_watches.core.registry.ModParticlesFabric;
import com.cursee.overclocked_watches.platform.services.IPlatformHelper;
import com.mojang.blaze3d.vertex.PoseStack;
import dev.emi.trinkets.api.SlotReference;
import dev.emi.trinkets.api.TrinketComponent;
import dev.emi.trinkets.api.TrinketsApi;
import dev.emi.trinkets.api.client.TrinketRenderer;
import dev.emi.trinkets.api.client.TrinketRendererRegistry;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.util.Tuple;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Supplier;

public class FabricPlatformHelper implements IPlatformHelper {

    @Override
    public String getPlatformName() {
        return "Fabric";
    }

    @Override
    public boolean isModLoaded(String modId) {

        return FabricLoader.getInstance().isModLoaded(modId);
    }

    @Override
    public boolean isDevelopmentEnvironment() {

        return FabricLoader.getInstance().isDevelopmentEnvironment();
    }

    @Override
    public <T extends Item> void registerWatchRenderer(T item, Supplier<IWatchRenderer> rendererSupplier) {
        TrinketRendererRegistry.registerRenderer(item, new WatchTrinketRenderer(rendererSupplier.get()));
    }

    @Override
    public IWatchRenderer getWatchRenderer(Item item) {
        Optional<TrinketRenderer> renderer = TrinketRendererRegistry.getRenderer(item);
        if (renderer.isPresent() && renderer.get() instanceof WatchTrinketRenderer artifactTrinketRenderer) {
            return artifactTrinketRenderer.renderer();
        }
        return null;
    }

    @Override
    public Supplier<Item> getRegisteredGoldenWatchItem() {
        return () -> ModItemsFabric.GOLDEN_WATCH;
    }

    @Override
    public Supplier<Item> getRegisteredDiamondWatchItem() {
        return () -> ModItemsFabric.DIAMOND_WATCH;
    }

    @Override
    public Supplier<Item> getRegisteredNetheriteWatchItem() {
        return () -> ModItemsFabric.NETHERITE_WATCH;
    }

    @Override
    public boolean playerHasGoldenWatchEquipped(Player player) {

        final AtomicBoolean foundWatch = new AtomicBoolean(false);
        TrinketsApi.getTrinketComponent(player).ifPresent(trinketComponent -> {
            if (trinketComponent.isEquipped(itemStack -> itemStack.getItem() == ModItemsFabric.GOLDEN_WATCH)) foundWatch.set(true);
        });

        return foundWatch.get();
    }

    @Override
    public boolean playerHasDiamondWatchEquipped(Player player) {

        final AtomicBoolean foundWatch = new AtomicBoolean(false);
        TrinketsApi.getTrinketComponent(player).ifPresent(trinketComponent -> {
            if (trinketComponent.isEquipped(itemStack -> itemStack.getItem() == ModItemsFabric.DIAMOND_WATCH)) foundWatch.set(true);
        });

        return foundWatch.get();
    }

    @Override
    public boolean playerHasNetheriteWatchEquipped(Player player) {

        final AtomicBoolean foundWatch = new AtomicBoolean(false);
        TrinketsApi.getTrinketComponent(player).ifPresent(trinketComponent -> {
            if (trinketComponent.isEquipped(itemStack -> itemStack.getItem() == ModItemsFabric.NETHERITE_WATCH)) foundWatch.set(true);
        });

        return foundWatch.get();
    }

    @Override
    public ItemStack getEquippedGoldenWatch(Player player) {

        AtomicReference<ItemStack> itemStackReference = new AtomicReference<ItemStack>(ItemStack.EMPTY);
//        TrinketsApi.getTrinketComponent(player).ifPresent(trinketComponent -> {
//            if (trinketComponent.isEquipped(itemStack -> itemStack.getItem() == ModItemsFabric.GOLDEN_WATCH)) itemStackReference.set(trinketComponent.getEquipped(ModItemsFabric.NETHERITE_WATCH).get(0).getB());
//        });
        TrinketsApi.getTrinketComponent(player).ifPresent(trinketComponent -> trinketComponent.getEquipped(ModItemsFabric.GOLDEN_WATCH).forEach(slotReferenceItemStackTuple -> itemStackReference.set(slotReferenceItemStackTuple.getB())));

        return itemStackReference.get();
    }

    @Override
    public ItemStack getEquippedDiamondWatch(Player player) {

        AtomicReference<ItemStack> itemStackReference = new AtomicReference<ItemStack>(ItemStack.EMPTY);
        TrinketsApi.getTrinketComponent(player).ifPresent(trinketComponent -> trinketComponent.getEquipped(ModItemsFabric.DIAMOND_WATCH).forEach(slotReferenceItemStackTuple -> itemStackReference.set(slotReferenceItemStackTuple.getB())));

        return itemStackReference.get();
    }

    @Override
    public ItemStack getEquippedNetheriteWatch(Player player) {

        AtomicReference<ItemStack> itemStackReference = new AtomicReference<ItemStack>(ItemStack.EMPTY);
        TrinketsApi.getTrinketComponent(player).ifPresent(trinketComponent -> trinketComponent.getEquipped(ModItemsFabric.NETHERITE_WATCH).forEach(slotReferenceItemStackTuple -> itemStackReference.set(slotReferenceItemStackTuple.getB())));

        return itemStackReference.get();
    }

    @Override
    public SimpleParticleType getGoldenWatchGrowthParticle() {
        return ModParticlesFabric.GOLDEN_WATCH_GROWTH;
    }

    @Override
    public SimpleParticleType getDiamondWatchGrowthParticle() {
        return ModParticlesFabric.DIAMOND_WATCH_GROWTH;
    }

    @Override
    public SimpleParticleType getNetheriteWatchGrowthParticle() {
        return ModParticlesFabric.NETHERITE_WATCH_GROWTH;
    }

    @Override
    public boolean consumeWatchCharge(Player player) {
//        TrinketsApi.getTrinketComponent(player).ifPresent(trinketComponent -> {
//            if (trinketComponent.isEquipped(itemStack -> itemStack.getItem() == ModItemsFabric.NETHERITE_WATCH)) {
//                List<Tuple<SlotReference, ItemStack>> equippedNetheriteWatches = trinketComponent.getEquipped(ModItemsFabric.NETHERITE_WATCH);
//                equippedNetheriteWatches.forEach(slotReferenceItemStackTuple -> {
//                    slotReferenceItemStackTuple.getB().getOrCreateTagElement("watch_charge.5");
//                });
//            }
//        });

        final AtomicBoolean CHARGE_CONSUMED = new AtomicBoolean(false);
        TrinketsApi.getTrinketComponent(player).ifPresent(component -> {
            if (component.isEquipped(ModItemsFabric.NETHERITE_WATCH)) {
                component.getEquipped(ModItemsFabric.NETHERITE_WATCH).forEach(slotStackPair -> {
                    if (OverclockedWatches.handleNetheriteWatchTag(slotStackPair.getB())) CHARGE_CONSUMED.set(true);
                });
            }
            else if (component.isEquipped(ModItemsFabric.DIAMOND_WATCH)) {
                component.getEquipped(ModItemsFabric.DIAMOND_WATCH).forEach(slotStackPair -> {
                    if (OverclockedWatches.handleDiamondWatchTag(slotStackPair.getB())) CHARGE_CONSUMED.set(true);
                });
            }
            else if (component.isEquipped(ModItemsFabric.GOLDEN_WATCH)) {
                component.getEquipped(ModItemsFabric.GOLDEN_WATCH).forEach(slotStackPair -> {
                    if (OverclockedWatches.handleGoldenWatchTag(slotStackPair.getB())) CHARGE_CONSUMED.set(true);
                });
            }
        });

        return CHARGE_CONSUMED.get();
    }

    private record WatchTrinketRenderer(IWatchRenderer renderer) implements TrinketRenderer {

        @Override
        public void render(ItemStack stack, SlotReference slotReference, EntityModel<? extends LivingEntity> entityModel, PoseStack poseStack, MultiBufferSource multiBufferSource, int light, LivingEntity entity, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
            int index = slotReference.index() + (slotReference.inventory().getSlotType().getGroup().equals("hand") ? 0 : 1);
            renderer.render(stack, entity, index, poseStack, multiBufferSource, light, limbSwing, limbSwingAmount, partialTicks, ageInTicks, netHeadYaw, headPitch);
        }
    }
}
