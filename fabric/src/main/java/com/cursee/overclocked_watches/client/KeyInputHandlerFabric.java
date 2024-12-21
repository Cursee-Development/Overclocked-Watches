package com.cursee.overclocked_watches.client;

import com.cursee.overclocked_watches.Constants;
import com.cursee.overclocked_watches.platform.Services;
import com.mojang.blaze3d.platform.InputConstants;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import org.lwjgl.glfw.GLFW;

public class KeyInputHandlerFabric {

    public static KeyMapping dayNightKey;

    public static void registerKeyInputs() {
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (dayNightKey.consumeClick()) {
                // todo: networking to change from day to night
                if (Services.PLATFORM.playerHasNetheriteWatchEquipped(Minecraft.getInstance().player)) System.out.println("found netherite watch after key clicked");
                else if (Services.PLATFORM.playerHasDiamondWatchEquipped(Minecraft.getInstance().player)) System.out.println("found diamond watch after key clicked");
                else if (Services.PLATFORM.playerHasGoldenWatchEquipped(Minecraft.getInstance().player)) System.out.println("found golden watch after key clicked");
            }
        });
    }

    public static void register() {
        dayNightKey = KeyBindingHelper.registerKeyBinding(new KeyMapping(
                Constants.KEY_DAY_NIGHT,
                InputConstants.Type.KEYSYM,
                GLFW.GLFW_KEY_O,
                Constants.KEY_CATEGORY_DAY_NIGHT
        ));

        registerKeyInputs();
    }
}
