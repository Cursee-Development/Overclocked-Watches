package io.github.jason13official.overclocked_watches.impl.client.item.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import io.github.jason13official.overclocked_watches.api.client.renderer.IWatchRenderer;
import io.github.jason13official.overclocked_watches.impl.client.item.model.ArmsModel;
import io.github.jason13official.overclocked_watches.impl.common.item.WatchTier;
import io.github.jason13official.overclocked_watches.impl.common.registry.ModItems;
import io.github.jason13official.overclocked_watches.platform.Services;
import java.util.Locale;
import java.util.function.Function;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.state.ArmedEntityRenderState;
import net.minecraft.client.renderer.entity.state.AvatarRenderState;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.Identifier;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.player.PlayerModelType;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

public class WatchRenderer implements IWatchRenderer {

  private final Identifier wideTexture;
  private final Identifier slimTexture;
  private final ArmsModel wideModel;
  private final ArmsModel slimModel;

  public WatchRenderer(String name, Function<Boolean, ArmsModel> model) {
    this(IWatchRenderer.getTexturePath(name, "%s_wide".formatted(name)), IWatchRenderer.getTexturePath(name, "%s_slim".formatted(name)), model);
  }

  public WatchRenderer(String wideTexture, String slimTexture, Function<Boolean, ArmsModel> model) {
    this(IWatchRenderer.getTexturePath(wideTexture), IWatchRenderer.getTexturePath(slimTexture), model);
  }

  public WatchRenderer(Identifier wideTexture, Identifier slimTexture, Function<Boolean, ArmsModel> model) {
    this.wideTexture = wideTexture;
    this.slimTexture = slimTexture;
    this.wideModel = model.apply(false);
    this.slimModel = model.apply(true);
  }

  public static void registerAll() {
    for (WatchTier tier : WatchTier.values()) {
      String name = tier.name().toLowerCase(Locale.ROOT) + "_watch";
      Services.PLATFORM.registerWatchRenderer(ModItems.getWatch(tier), () -> new WatchRenderer(name, hasSlimArms -> ArmsModel.bakeWatchTextureOnModel(tier, hasSlimArms)));
    }
  }

  @Nullable
  public static WatchRenderer getGloveRenderer(ItemStack stack) {

    if (!stack.isEmpty() && Services.PLATFORM.getWatchRenderer(stack.getItem()) instanceof WatchRenderer gloveRenderer) {
      return gloveRenderer;
    }

    return null;
  }

  protected static boolean hasSlimArms(LivingEntityRenderState state) {
    return state instanceof AvatarRenderState avatarState && avatarState.skin.model() == PlayerModelType.SLIM;
  }

  protected Identifier getTexture(boolean hasSlimArms) {
    return hasSlimArms ? slimTexture : wideTexture;
  }

  protected ArmsModel getModel(boolean hasSlimArms) {
    return hasSlimArms ? slimModel : wideModel;
  }

  @Override
  public void submit(
      ItemStack stack,
      EntityModel<? extends LivingEntityRenderState> contextModel,
      int slotIndex,
      PoseStack poseStack,
      SubmitNodeCollector submit,
      int light,
      LivingEntityRenderState state,
      float limbAngle,
      float limbDistance
  ) {
    if (!(state instanceof ArmedEntityRenderState armedState)) {
      return;
    }

    boolean hasSlimArms = hasSlimArms(state);
    ArmsModel model = getModel(hasSlimArms);
    InteractionHand hand = slotIndex % 2 == 0 ? InteractionHand.MAIN_HAND : InteractionHand.OFF_HAND;
    HumanoidArm handSide = hand == InteractionHand.MAIN_HAND ? armedState.mainArm : armedState.mainArm.getOpposite();

    poseStack.pushPose();
    if (contextModel instanceof HumanoidModel<?> humanoidModel) {
      model.root().loadPose(humanoidModel.root().storePose());
      model.head.loadPose(humanoidModel.head.storePose());
      model.body.loadPose(humanoidModel.body.storePose());
      model.leftArm.loadPose(humanoidModel.leftArm.storePose());
      model.rightArm.loadPose(humanoidModel.rightArm.storePose());
      model.leftLeg.loadPose(humanoidModel.leftLeg.storePose());
      model.rightLeg.loadPose(humanoidModel.rightLeg.storePose());
    }
    model.prepareArm(handSide);

    RenderType renderType = model.renderType(getTexture(hasSlimArms));
    submit.submitModelPart(model.root(), poseStack, renderType, light, OverlayTexture.NO_OVERLAY, null, false, stack.hasFoil());
    poseStack.popPose();
  }

  public final void renderFirstPersonArm(ModelPart vanillaArm, PoseStack matrixStack, SubmitNodeCollector submit, int light, boolean hasSlimArms, HumanoidArm side, boolean hasFoil) {

    ArmsModel model = getModel(hasSlimArms);
    model.prepareArm(side);
    ModelPart ourArm = side == HumanoidArm.LEFT ? model.leftArm : model.rightArm;
    ourArm.resetPose();

    matrixStack.pushPose();
    vanillaArm.translateAndRotate(matrixStack);
    matrixStack.translate(side == HumanoidArm.RIGHT ? -0.0625F : 0.0625F, 0.625F, 0.0F);

    RenderType renderType = model.renderType(getTexture(hasSlimArms));
    submit.submitModelPart(model.root(), matrixStack, renderType, light, OverlayTexture.NO_OVERLAY, null, false, hasFoil);
    matrixStack.popPose();
  }

  /// Variant for platforms (e.g. Curios) that already position the PoseStack at the arm before calling in.
  public final void renderFirstPersonArm(PoseStack matrixStack, SubmitNodeCollector submit, int light, boolean hasSlimArms, HumanoidArm side, boolean hasFoil) {

    ArmsModel model = getModel(hasSlimArms);
    model.prepareArm(side);
    ModelPart ourArm = side == HumanoidArm.LEFT ? model.leftArm : model.rightArm;
    ourArm.resetPose();

    RenderType renderType = model.renderType(getTexture(hasSlimArms));
    submit.submitModelPart(model.root(), matrixStack, renderType, light, OverlayTexture.NO_OVERLAY, null, false, hasFoil);
  }
}
