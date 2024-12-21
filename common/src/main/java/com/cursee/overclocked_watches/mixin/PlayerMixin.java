package com.cursee.overclocked_watches.mixin;

import com.cursee.overclocked_watches.OverclockedWatches;
import com.cursee.overclocked_watches.platform.Services;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.CropBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.concurrent.atomic.AtomicBoolean;

@Mixin(Player.class)
public class PlayerMixin {

    @Inject(method = "tick", at = @At("TAIL"))
    private void injected_$_onTick(CallbackInfo ci) {

        Player instancePlayer = (Player) (Object) this;

        if (!(instancePlayer instanceof ServerPlayer player)) return;

        // ServerPlayer player = (ServerPlayer) (Object) this;
        ServerLevel level = (ServerLevel) player.level();
        if (!(level.getGameTime() % 20 == 0)) return;

        final AtomicBoolean FOUND_GOLDEN_WATCH = new AtomicBoolean(false);
        final AtomicBoolean FOUND_DIAMOND_WATCH = new AtomicBoolean(false);
        final AtomicBoolean FOUND_NETHERITE_WATCH = new AtomicBoolean(false);
        if (Services.PLATFORM.playerHasGoldenWatchEquipped(player)) FOUND_GOLDEN_WATCH.set(true);
        if (Services.PLATFORM.playerHasDiamondWatchEquipped(player)) FOUND_DIAMOND_WATCH.set(true);
        if (Services.PLATFORM.playerHasNetheriteWatchEquipped(player)) FOUND_NETHERITE_WATCH.set(true);

        if (!(FOUND_GOLDEN_WATCH.get() || FOUND_DIAMOND_WATCH.get() || FOUND_NETHERITE_WATCH.get())) return;

        final AABB SEARCH_AREA = new AABB(player.blockPosition()).inflate(4, 0, 4);
        final Iterable<BlockPos> SEARCH_POSITIONS = BlockPos.betweenClosed((int) SEARCH_AREA.minX, (int) SEARCH_AREA.minY, (int) SEARCH_AREA.minZ, (int) SEARCH_AREA.maxX, (int) SEARCH_AREA.maxY, (int) SEARCH_AREA.maxZ);
        SEARCH_POSITIONS.forEach(blockPos -> {

            final BlockState blockState = level.getBlockState(blockPos);
            if (!(blockState.getBlock() instanceof CropBlock crop)) return;

            if (crop.isMaxAge(blockState)) return;

            if (FOUND_NETHERITE_WATCH.get()) {
                for (int i=0; i<3; i++) {
                    unique_$_forceGrowth(crop, blockState, level, blockPos);
                }
                OverclockedWatches.addNetheriteGrowthParticles(level, blockPos, 8);
            }
            else if (FOUND_DIAMOND_WATCH.get()) {

                for (int i=0; i<2; i++) {
                    unique_$_forceGrowth(crop, blockState, level, blockPos);
                }
                OverclockedWatches.addDiamondGrowthParticles(level, blockPos, 8);
            }
            else if (FOUND_GOLDEN_WATCH.get()) {
                unique_$_forceGrowth(crop, blockState, level, blockPos);
                OverclockedWatches.addGoldenGrowthParticles(level, blockPos, 8);
            }
        });
    }

    @Unique
    private static void unique_$_forceGrowth(CropBlock crop, BlockState state, ServerLevel level, BlockPos pos) {
        if (level.getRawBrightness(pos, 0) >= 9) {
            int currentAge = crop.getAge(state);
            if (currentAge < crop.getMaxAge()) level.setBlock(pos, crop.getStateForAge(currentAge + 1), 2);
        }
    }
}
