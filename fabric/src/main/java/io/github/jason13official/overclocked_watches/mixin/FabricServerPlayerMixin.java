package io.github.jason13official.overclocked_watches.mixin;

import com.mojang.authlib.GameProfile;
import io.github.jason13official.overclocked_watches.impl.common.util.OverclockedWatchesUtil;
import io.github.jason13official.overclocked_watches.platform.Services;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.ValueOutput;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerPlayer.class)
public abstract class FabricServerPlayerMixin extends Player {

  public FabricServerPlayerMixin(Level l, GameProfile g) {
    super(l, g);
  }

  @Inject(
      method = {"addAdditionalSaveData"},
      at = {@At("HEAD")}
  )
  private void inject$addAdditionalSaveData(ValueOutput output, CallbackInfo ci) {
    OverclockedWatchesUtil.saveCooldowns(Services.PLATFORM.getPersistentData(this), this);
  }
}
