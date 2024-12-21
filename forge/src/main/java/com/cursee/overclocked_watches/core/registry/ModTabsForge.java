package com.cursee.overclocked_watches.core.registry;

import com.cursee.overclocked_watches.Constants;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraftforge.registries.RegistryObject;

public class ModTabsForge {

    public static void register() {}

    public static final RegistryObject<CreativeModeTab> WATCHES_TAB = RegistryForge.registerTab(Constants.MOD_ID, () -> CreativeModeTab.builder()
            .icon(ModItemsForge.DIAMOND_WATCH.get()::getDefaultInstance)
            .title(Component.translatable("itemGroup.overclockedWatches"))
            .displayItems((itemDisplayParameters, output) -> {
                output.accept(ModItemsForge.GOLDEN_WATCH.get());
                output.accept(ModItemsForge.DIAMOND_WATCH.get());
                output.accept(ModItemsForge.NETHERITE_WATCH.get());
            }).build());
}
