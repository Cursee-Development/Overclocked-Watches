package io.github.jason13official.overclocked_watches.impl.client.item.model;

import io.github.jason13official.overclocked_watches.impl.client.item.RendererLayers;
import io.github.jason13official.overclocked_watches.impl.client.item.RendererUtil;
import io.github.jason13official.overclocked_watches.impl.common.item.WatchTier;
import java.util.function.Function;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.entity.state.HumanoidRenderState;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.HumanoidArm;

public class ArmsModel extends HumanoidModel<HumanoidRenderState> {

  public ArmsModel(ModelPart part, Function<Identifier, RenderType> renderType) {
    super(part, renderType);
  }

  public ArmsModel(ModelPart part) {
    this(part, RenderTypes::entityCutout);
  }

  public static ArmsModel bakeWatchTextureOnModel(WatchTier tier, boolean hasSlimArms) {
    return new ArmsModel(RendererUtil.bakeLayer(RendererLayers.watch(tier, hasSlimArms)));
  }

  /// Mirrors the old HumanoidModel#setAllVisible: only toggles the named top-level
  /// parts, so nested children (e.g. the watch geometry hanging off left_arm/right_arm) keep
  /// their own default visibility instead of being hidden along with everything else.
  public void prepareArm(HumanoidArm handSide) {
    head.visible = false;
    hat.visible = false;
    body.visible = false;
    leftLeg.visible = false;
    rightLeg.visible = false;
    leftArm.visible = handSide == HumanoidArm.LEFT;
    rightArm.visible = handSide == HumanoidArm.RIGHT;
  }
}
