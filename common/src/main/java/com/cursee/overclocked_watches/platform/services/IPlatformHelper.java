package com.cursee.overclocked_watches.platform.services;

import com.cursee.overclocked_watches.client.item.renderer.IWatchRenderer;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.util.function.Supplier;

public interface IPlatformHelper {

    /**
     * Gets the name of the current platform
     *
     * @return The name of the current platform.
     */
    String getPlatformName();

    /**
     * Checks if a mod with the given id is loaded.
     *
     * @param modId The mod to check if it is loaded.
     * @return True if the mod is loaded, false otherwise.
     */
    boolean isModLoaded(String modId);

    /**
     * Check if the game is currently in a development environment.
     *
     * @return True if in a development environment, false otherwise.
     */
    boolean isDevelopmentEnvironment();

    /**
     * Gets the name of the environment type as a string.
     *
     * @return The name of the environment type.
     */
    default String getEnvironmentName() {

        return isDevelopmentEnvironment() ? "development" : "production";
    }

    <T extends Item> void registerWatchRenderer(T item, Supplier<IWatchRenderer> rendererSupplier);

    <T extends Item> IWatchRenderer getWatchRenderer(T item);

    Supplier<Item> getRegisteredGoldenWatchItem();
    Supplier<Item> getRegisteredDiamondWatchItem();
    Supplier<Item> getRegisteredNetheriteWatchItem();

    boolean playerHasGoldenWatchEquipped(Player player);
    boolean playerHasDiamondWatchEquipped(Player player);
    boolean playerHasNetheriteWatchEquipped(Player player);

    ItemStack getEquippedGoldenWatch(Player player);
    ItemStack getEquippedDiamondWatch(Player player);
    ItemStack getEquippedNetheriteWatch(Player player);

    SimpleParticleType getGoldenWatchGrowthParticle();
    SimpleParticleType getDiamondWatchGrowthParticle();
    SimpleParticleType getNetheriteWatchGrowthParticle();

    boolean consumeWatchCharge(Player player);
}