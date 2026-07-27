package io.github.jason13official.overclocked_watches.platform;

import com.mojang.blaze3d.vertex.PoseStack;
import io.github.jason13official.overclocked_watches.api.client.renderer.IWatchRenderer;
import io.github.jason13official.overclocked_watches.impl.common.item.WatchTier;
import io.github.jason13official.overclocked_watches.impl.common.registry.ModItems;
import io.github.jason13official.overclocked_watches.platform.services.IPlatformHelper;
import java.nio.file.Path;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Supplier;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTab.Builder;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.fml.ModList;
import net.neoforged.fml.loading.FMLLoader;
import net.neoforged.fml.loading.FMLPaths;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.SlotContext;
import top.theillusivec4.curios.api.client.ICurioRenderer;

public class NeoForgePlatformHelper implements IPlatformHelper {

  @Override
  public String getPlatformName() {

    return "Forge";
  }

  @Override
  public boolean isModLoaded(String modId) {

    return ModList.get().isLoaded(modId);
  }

  @Override
  public boolean isDevelopmentEnvironment() {

    return !FMLLoader.getCurrent().isProduction();
  }

  @Override
  public Path getGameDirectory() {

    return FMLPaths.GAMEDIR.get();
  }

  @Override
  public Builder tabBuilder() {

    return CreativeModeTab.builder(CreativeModeTab.Row.TOP, 0);
  }

  @Override
  public boolean isClientSide() {

    return FMLLoader.getCurrent().getDist() == Dist.CLIENT;
  }

  @Override
  public <T extends Item> void registerWatchRenderer(T item, Supplier<IWatchRenderer> rendererSupplier) {
    ICurioRenderer.register(item, () -> new WatchCurioRenderer(rendererSupplier.get()));
  }

  @Override
  public IWatchRenderer getWatchRenderer(Item item) {
    if (ICurioRenderer.getOrNull(item) instanceof WatchCurioRenderer curioRenderer) {
      return curioRenderer.renderer();
    }
    return null;
  }

  @Override
  public boolean playerHasWatchEquipped(Player player, WatchTier tier) {

    Item watch = ModItems.getWatch(tier);
    final AtomicBoolean foundWatch = new AtomicBoolean(false);
    CuriosApi.getCuriosInventory(player).ifPresent(iCuriosItemHandler -> {
      if (iCuriosItemHandler.isEquipped(watch)) {
        foundWatch.set(true);
      }
    });

    return foundWatch.get();
  }

  @Override
  public ItemStack getEquippedWatch(Player player, WatchTier tier) {

    Item watch = ModItems.getWatch(tier);
    AtomicReference<ItemStack> itemStackReference = new AtomicReference<ItemStack>(ItemStack.EMPTY);
    CuriosApi.getCuriosInventory(player)
        .ifPresent(iCuriosItemHandler -> iCuriosItemHandler.findFirstCurio(watch).ifPresent(slotResult -> itemStackReference.set(slotResult.stack())));

    return itemStackReference.get();
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
    return entity.getPersistentData();
  }

  private record WatchCurioRenderer(IWatchRenderer renderer) implements ICurioRenderer {

    @Override
    public <S extends LivingEntityRenderState, M extends EntityModel<? super S>> void render(
        ItemStack stack,
        SlotContext slotContext,
        PoseStack poseStack,
        SubmitNodeCollector submitNodeCollector,
        int packedLight,
        S renderState,
        RenderLayerParent<S, M> renderLayerParent,
        EntityRendererProvider.Context context,
        float yRotation,
        float xRotation) {
      @SuppressWarnings("unchecked")
      EntityModel<? extends LivingEntityRenderState> contextModel = (EntityModel<? extends LivingEntityRenderState>) renderLayerParent.getModel();
      renderer.submit(stack, contextModel, slotContext.index(), poseStack, submitNodeCollector, packedLight, renderState, yRotation, xRotation);
    }
  }
}