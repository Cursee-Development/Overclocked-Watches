package io.github.jason13official.overclocked_watches.mixin;

import io.github.jason13official.overclocked_watches.api.common.data.IEntityDataSaver;
import io.github.jason13official.overclocked_watches.impl.common.util.OverclockedWatchesUtil;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Entity.class)
public abstract class FabricEntityMixin implements IEntityDataSaver {

  @Unique
  private CompoundTag persistentData;

  @Override
  public CompoundTag overclocked_watches$getPersistentData() {
    if (this.persistentData == null) {
      this.persistentData = new CompoundTag();
    }
    return persistentData;
  }

  @Inject(method = "saveWithoutId", at = @At("HEAD"))
  protected void injectWriteMethod(ValueOutput output, CallbackInfo info) {
    if (persistentData != null) {
      output.store(OverclockedWatchesUtil.PERSISTENT_DATA_TAG, CompoundTag.CODEC, persistentData);
    }
  }

  @Inject(method = "load", at = @At("HEAD"))
  protected void injectReadMethod(ValueInput input, CallbackInfo info) {
    persistentData = input.read(OverclockedWatchesUtil.PERSISTENT_DATA_TAG, CompoundTag.CODEC).orElse(null);
  }
}
