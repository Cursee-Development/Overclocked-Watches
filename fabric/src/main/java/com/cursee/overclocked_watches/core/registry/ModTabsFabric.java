package com.cursee.overclocked_watches.core.registry;

import com.cursee.overclocked_watches.Constants;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;

public class ModTabsFabric {

    public static void register() {}

    public static final CreativeModeTab WATCHES_TAB = RegistryFabric.registerTab(Constants.MOD_ID, () -> FabricItemGroup.builder()
            .icon(ModItemsFabric.DIAMOND_WATCH::getDefaultInstance)
            .title(Component.translatable("itemGroup.overclockedWatches"))
            .displayItems((itemDisplayParameters, output) -> {
                output.accept(ModItemsFabric.GOLDEN_WATCH);
                output.accept(ModItemsFabric.DIAMOND_WATCH);
                output.accept(ModItemsFabric.NETHERITE_WATCH);
            }).build());
}
