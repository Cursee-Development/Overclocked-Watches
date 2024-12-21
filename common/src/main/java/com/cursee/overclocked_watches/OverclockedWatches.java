package com.cursee.overclocked_watches;

import com.cursee.overclocked_watches.platform.Services;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;

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
}