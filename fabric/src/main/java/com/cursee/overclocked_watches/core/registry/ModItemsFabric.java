package com.cursee.overclocked_watches.core.registry;

import com.cursee.overclocked_watches.core.item.custom.WatchItem;
import net.minecraft.world.item.Item;

public class ModItemsFabric {

    public static void register() {}

    public static final Item GOLDEN_WATCH = RegistryFabric.registerItem("golden_watch", () -> new WatchItem(new Item.Properties().stacksTo(1)));
    public static final Item DIAMOND_WATCH = RegistryFabric.registerItem("diamond_watch", () -> new WatchItem(new Item.Properties().stacksTo(1)));
    public static final Item NETHERITE_WATCH = RegistryFabric.registerItem("netherite_watch", () -> new WatchItem(new Item.Properties().stacksTo(1)));
}
