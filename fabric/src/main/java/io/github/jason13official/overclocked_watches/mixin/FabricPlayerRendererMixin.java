package io.github.jason13official.overclocked_watches.mixin;

import com.mojang.blaze3d.vertex.PoseStack;
import eu.pb4.trinkets.api.TrinketsApi;
import io.github.jason13official.overclocked_watches.impl.client.item.renderer.WatchRenderer;
import io.github.jason13official.overclocked_watches.impl.common.item.WatchItem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.player.AvatarRenderer;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.HumanoidArm;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AvatarRenderer.class)
public abstract class FabricPlayerRendererMixin {

  @Unique
  private static void renderArm(PoseStack poseStack, SubmitNodeCollector submit, int light, HumanoidArm handSide) {
    AbstractClientPlayer player = Minecraft.getInstance().player;
    if (player == null) {
      return;
    }

    String groupId = handSide == player.getMainArm() ? "hand" : "offhand";
    TrinketsApi.getAttachment(player).forEachVisible((slotAccess, stack) -> {
      if (slotAccess.slotType().group().equals(groupId) && stack.getItem() instanceof WatchItem) {
        WatchRenderer gloveRenderer = WatchRenderer.getGloveRenderer(stack);
        if (gloveRenderer != null) {
          gloveRenderer.renderFirstPersonArm(poseStack, submit, light, player, handSide, stack.hasFoil());
        }
      }
    });
  }

  @Inject(method = "renderLeftHand", at = @At("TAIL"))
  private void renderLeftGlove(PoseStack poseStack, SubmitNodeCollector submit, int light, Identifier skinTexture, boolean hasSleeve, CallbackInfo ci) {
    renderArm(poseStack, submit, light, HumanoidArm.LEFT);
  }

  @Inject(method = "renderRightHand", at = @At("TAIL"))
  private void renderRightGlove(PoseStack poseStack, SubmitNodeCollector submit, int light, Identifier skinTexture, boolean hasSleeve, CallbackInfo ci) {
    renderArm(poseStack, submit, light, HumanoidArm.RIGHT);
  }
}
