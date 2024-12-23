package com.cursee.overclocked_watches;

import com.cursee.overclocked_watches.platform.Services;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.IntTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.ItemStack;

public class OverclockedWatches {

    public static void init() {}

    public static void addGoldenGrowthParticles(ServerLevel level, BlockPos blockPos, int particleCount) {
        for(int i = 0; i < particleCount; ++i) {
            level.sendParticles(Services.PLATFORM.getGoldenWatchGrowthParticle(), ((double) blockPos.getX()) + level.random.nextDouble(), ((double) blockPos.getY()) + 0.5D, ((double) blockPos.getZ()) + level.random.nextDouble(), 1, 0.0D, 0.0D, 0.0D, 0.2D);
        }
    }

    public static void addDiamondGrowthParticles(ServerLevel level, BlockPos blockPos, int particleCount) {
        for(int i = 0; i < particleCount; ++i) {
            level.sendParticles(Services.PLATFORM.getDiamondWatchGrowthParticle(), ((double) blockPos.getX()) + level.random.nextDouble(), ((double) blockPos.getY()) + 0.5D, ((double) blockPos.getZ()) + level.random.nextDouble(), 1, 0.0D, 0.0D, 0.0D, 0.2D);
        }
    }

    public static void addNetheriteGrowthParticles(ServerLevel level, BlockPos blockPos, int particleCount) {
        for(int i = 0; i < particleCount; ++i) {
            level.sendParticles(Services.PLATFORM.getNetheriteWatchGrowthParticle(), ((double) blockPos.getX()) + level.random.nextDouble(), ((double) blockPos.getY()) + 0.5D, ((double) blockPos.getZ()) + level.random.nextDouble(), 1, 0.0D, 0.0D, 0.0D, 0.2D);
        }
    }

    public static final String TAG_CHARGES = "charges";

    public static boolean handleNetheriteWatchTag(ItemStack itemStack) {

        CompoundTag baseTag = itemStack.getOrCreateTag();
        if (!baseTag.contains(TAG_CHARGES)) baseTag.put(TAG_CHARGES, IntTag.valueOf(5));

        int currentCharges = baseTag.getInt(TAG_CHARGES);

        if (currentCharges > 0) {
            currentCharges -= 1;
            baseTag.put(TAG_CHARGES, IntTag.valueOf(currentCharges));
            return true;
        }

        return false;
    }

    public static boolean handleDiamondWatchTag(ItemStack itemStack) {

        CompoundTag baseTag = itemStack.getOrCreateTag();
        if (!baseTag.contains(TAG_CHARGES)) baseTag.put(TAG_CHARGES, IntTag.valueOf(3));

        int currentCharges = baseTag.getInt(TAG_CHARGES);

        if (currentCharges > 0) {
            currentCharges -= 1;
            baseTag.put(TAG_CHARGES, IntTag.valueOf(currentCharges));
            return true;
        }

        return false;
    }

    public static boolean handleGoldenWatchTag(ItemStack itemStack) {

        CompoundTag baseTag = itemStack.getOrCreateTag();
        if (!baseTag.contains(TAG_CHARGES)) baseTag.put(TAG_CHARGES, IntTag.valueOf(1));

        int currentCharges = baseTag.getInt(TAG_CHARGES);

        if (currentCharges > 0) {
            currentCharges -= 1;
            baseTag.put(TAG_CHARGES, IntTag.valueOf(currentCharges));
            return true;
        }

        return false;
    }
}