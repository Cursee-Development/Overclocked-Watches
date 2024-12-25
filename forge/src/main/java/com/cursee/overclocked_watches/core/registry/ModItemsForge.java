package com.cursee.overclocked_watches.core.registry;

import com.cursee.overclocked_watches.core.item.custom.WatchItem;
import net.minecraft.world.item.Item;
import net.minecraftforge.registries.RegistryObject;

public class ModItemsForge {

    public static void register() {}

    public static final RegistryObject<Item> GOLDEN_WATCH = RegistryForge.registerItem("golden_watch", () -> new WatchItem(new Item.Properties().stacksTo(1).defaultDurability(50)));
    public static final RegistryObject<Item> DIAMOND_WATCH = RegistryForge.registerItem("diamond_watch", () -> new WatchItem(new Item.Properties().stacksTo(1).defaultDurability(250)));
    public static final RegistryObject<Item> NETHERITE_WATCH = RegistryForge.registerItem("netherite_watch", () -> new WatchItem(new Item.Properties().stacksTo(1).defaultDurability(500)));
}
