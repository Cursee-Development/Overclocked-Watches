package com.cursee.overclocked_watches.mixin;

import com.cursee.overclocked_watches.platform.Services;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.concurrent.atomic.AtomicBoolean;

@Mixin(Mob.class)
public class MobMixin {

    @Inject(method = "tick", at = @At("TAIL"))
    private void injected_$_onTick(CallbackInfo callbackInfo) {

        Mob instanceMob = (Mob) (Object) this;
        Level instanceLevel = instanceMob.level();
        if (!(instanceMob instanceof AgeableMob) || !(instanceLevel instanceof ServerLevel)) return;

        AgeableMob mob = (AgeableMob) instanceMob;
        ServerLevel level = (ServerLevel) instanceLevel;
        if (!(level.getGameTime() % 20 == 0) || mob.age >= 0) return;

        // we are operating on the server, once per second, on a baby mob

        final AABB SEARCH_AREA = new AABB(mob.blockPosition()).inflate(4, 1, 4);
        Iterable<Player> nearbyPlayers = level.getEntitiesOfClass(Player.class, SEARCH_AREA);

        final AtomicBoolean FOUND_GOLDEN_WATCH = new AtomicBoolean(false);
        final AtomicBoolean FOUND_DIAMOND_WATCH = new AtomicBoolean(false);
        final AtomicBoolean FOUND_NETHERITE_WATCH = new AtomicBoolean(false);
        nearbyPlayers.forEach(player -> {
            if (Services.PLATFORM.playerHasGoldenWatchEquipped(player)) FOUND_GOLDEN_WATCH.set(true);
            if (Services.PLATFORM.playerHasDiamondWatchEquipped(player)) FOUND_DIAMOND_WATCH.set(true);
            if (Services.PLATFORM.playerHasNetheriteWatchEquipped(player)) FOUND_NETHERITE_WATCH.set(true);
        });

        if (!(FOUND_GOLDEN_WATCH.get() || FOUND_DIAMOND_WATCH.get() || FOUND_NETHERITE_WATCH.get())) return;

        // execute the highest tier of watch first
        if (FOUND_NETHERITE_WATCH.get()) {
            mob.ageUp((60 * 4)); // four minutes
            unique_$_addNetheriteGrowthParticles(level, mob.blockPosition(), 8);
        }
        else if (FOUND_DIAMOND_WATCH.get()) {
            mob.ageUp((60 * 2)); // two minutes
            unique_$_addDiamondGrowthParticles(level, mob.blockPosition(), 8);
        }
        else if (FOUND_GOLDEN_WATCH.get()) {
            mob.ageUp(60); // one minute
            unique_$_addGoldenGrowthParticles(level, mob.blockPosition(), 8);
        }
    }

    @Unique
    private static void unique_$_addGoldenGrowthParticles(ServerLevel level, BlockPos blockPos, int particleCount) {
        for(int i = 0; i < particleCount; ++i) {
            level.sendParticles(Services.PLATFORM.getGoldenWatchGrowthParticle(), ((double) blockPos.getX()) + level.random.nextDouble(), ((double) blockPos.getY()) + 0.5D, ((double) blockPos.getZ()) + level.random.nextDouble(), 1, 0.0D, 0.0D, 0.0D, 0.2D);
        }
    }

    @Unique
    private static void unique_$_addDiamondGrowthParticles(ServerLevel level, BlockPos blockPos, int particleCount) {
        for(int i = 0; i < particleCount; ++i) {
            level.sendParticles(Services.PLATFORM.getDiamondWatchGrowthParticle(), ((double) blockPos.getX()) + level.random.nextDouble(), ((double) blockPos.getY()) + 0.5D, ((double) blockPos.getZ()) + level.random.nextDouble(), 1, 0.0D, 0.0D, 0.0D, 0.2D);
        }
    }

    @Unique
    private static void unique_$_addNetheriteGrowthParticles(ServerLevel level, BlockPos blockPos, int particleCount) {
        for(int i = 0; i < particleCount; ++i) {
            level.sendParticles(Services.PLATFORM.getNetheriteWatchGrowthParticle(), ((double) blockPos.getX()) + level.random.nextDouble(), ((double) blockPos.getY()) + 0.5D, ((double) blockPos.getZ()) + level.random.nextDouble(), 1, 0.0D, 0.0D, 0.0D, 0.2D);
        }
    }
}
