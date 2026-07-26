package io.github.jason13official.overclocked_watches.platform;

import com.mojang.blaze3d.vertex.PoseStack;
import eu.pb4.trinkets.api.TrinketAttachment;
import eu.pb4.trinkets.api.TrinketSlotAccess;
import eu.pb4.trinkets.api.TrinketsApi;
import eu.pb4.trinkets.api.client.TrinketRenderer;
import eu.pb4.trinkets.api.client.TrinketRendererRegistry;
import io.github.jason13official.overclocked_watches.api.client.renderer.IWatchRenderer;
import io.github.jason13official.overclocked_watches.api.common.data.IEntityDataSaver;
import io.github.jason13official.overclocked_watches.impl.client.item.renderer.WatchRenderer;
import io.github.jason13official.overclocked_watches.impl.common.item.WatchTier;
import io.github.jason13official.overclocked_watches.impl.common.registry.ModItems;
import io.github.jason13official.overclocked_watches.platform.services.IPlatformHelper;
import java.nio.file.Path;
import java.util.Optional;
import java.util.function.Supplier;
import net.fabricmc.api.EnvType;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.player.PlayerModelType;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTab.Builder;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public class FabricPlatformHelper implements IPlatformHelper {

  @Override
  public String getPlatformName() {
    return "Fabric";
  }

  @Override
  public boolean isModLoaded(String modId) {

    return FabricLoader.getInstance().isModLoaded(modId);
  }

  @Override
  public boolean isDevelopmentEnvironment() {

    return FabricLoader.getInstance().isDevelopmentEnvironment();
  }

  @Override
  public Path getGameDirectory() {

    return FabricLoader.getInstance().getGameDir();
  }

  @Override
  public Builder tabBuilder() {

    return CreativeModeTab.builder(CreativeModeTab.Row.TOP, 0);
  }

  @Override
  public boolean isClientSide() {
    return FabricLoader.getInstance().getEnvironmentType() == EnvType.CLIENT;
  }

  @Override
  public <T extends Item> void registerWatchRenderer(T item, Supplier<IWatchRenderer> rendererSupplier) {
    TrinketRendererRegistry.registerRenderer(item, new WatchTrinketRenderer(rendererSupplier.get()));
  }

  @Override
  public <T extends Item> IWatchRenderer getWatchRenderer(T item) {
    Optional<TrinketRenderer> renderer = TrinketRendererRegistry.getRenderer(item);
    if (renderer.isPresent() && renderer.get() instanceof WatchTrinketRenderer trinketRenderer) {
      return trinketRenderer.renderer();
    }
    return null;
  }

  @Override
  public boolean playerHasWatchEquipped(Player player, WatchTier tier) {

    Item watch = ModItems.getWatch(tier);
    TrinketAttachment attachment = TrinketsApi.getAttachment(player);
    return attachment.isEquipped(watch);
  }

  @Override
  public ItemStack getEquippedWatch(Player player, WatchTier tier) {

    Item watch = ModItems.getWatch(tier);
    TrinketAttachment attachment = TrinketsApi.getAttachment(player);
    return attachment.findFirst(watch).map(TrinketSlotAccess::get).orElse(ItemStack.EMPTY);
  }

  @Override
  public Item getItemFromRL(Identifier rl) {
    return BuiltInRegistries.ITEM.getValue(rl);
  }

  @Override
  public Identifier getRLFromItem(Item item) {
    return BuiltInRegistries.ITEM.getKey(item);
  }

  @Override
  public CompoundTag getPersistentData(Entity entity) {
    return ((IEntityDataSaver) entity).overclocked_watches$getPersistentData();
  }

  private record WatchTrinketRenderer(IWatchRenderer renderer) implements TrinketRenderer {

    @Override
    public void submit(ItemStack stack, TrinketSlotAccess slotReference, EntityModel<? extends LivingEntityRenderState> contextModel,
        PoseStack poseStack, SubmitNodeCollector submit, int light, LivingEntityRenderState state,
        float limbAngle, float limbDistance) {
      int index = slotReference.index() + (slotReference.slotType().group().equals("hand") ? 0 : 1);
      renderer.submit(stack, contextModel, index, poseStack, submit, light, state, limbAngle, limbDistance);
    }

    @Override
    public void submitFirstPersonRightArm(ItemStack stack, TrinketSlotAccess slotReference, EntityModel<? extends LivingEntityRenderState> contextModel, ModelPart arm,
        PoseStack poseStack, SubmitNodeCollector submit, int light, LocalPlayer player, boolean isMainHand) {
      if (renderer instanceof WatchRenderer watchRenderer) {
        boolean hasSlimArms = player.getSkin().model() == PlayerModelType.SLIM;
        watchRenderer.renderFirstPersonArm(arm, poseStack, submit, light, hasSlimArms, HumanoidArm.RIGHT, stack.hasFoil());
      }
    }

    @Override
    public void submitFirstPersonLeftArm(ItemStack stack, TrinketSlotAccess slotReference, EntityModel<? extends LivingEntityRenderState> contextModel, ModelPart arm,
        PoseStack poseStack, SubmitNodeCollector submit, int light, LocalPlayer player, boolean isMainHand) {
      if (renderer instanceof WatchRenderer watchRenderer) {
        boolean hasSlimArms = player.getSkin().model() == PlayerModelType.SLIM;
        watchRenderer.renderFirstPersonArm(arm, poseStack, submit, light, hasSlimArms, HumanoidArm.LEFT, stack.hasFoil());
      }
    }
  }
}
