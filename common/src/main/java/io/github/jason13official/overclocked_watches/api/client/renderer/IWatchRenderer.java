package io.github.jason13official.overclocked_watches.api.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import io.github.jason13official.overclocked_watches.OverclockedWatches;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.ItemStack;

public interface IWatchRenderer {

  static Identifier getTexturePath(String... names) {
    StringBuilder path = new StringBuilder("textures/entity/wearable");
    for (String name : names) {
      path.append('/');
      path.append(name);
    }
    path.append(".png");
    return OverclockedWatches.identifier(path.toString());
  }

  void submit(
      ItemStack stack,
      EntityModel<? extends LivingEntityRenderState> contextModel,
      int slotIndex,
      PoseStack poseStack, SubmitNodeCollector submit,
      int light,
      LivingEntityRenderState state,
      float limbAngle, float limbDistance);
}
