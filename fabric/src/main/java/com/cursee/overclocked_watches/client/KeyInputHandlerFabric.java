package com.cursee.overclocked_watches.client;

import com.cursee.overclocked_watches.Constants;
import com.cursee.overclocked_watches.core.network.ModMessagesFabric;
import com.mojang.blaze3d.platform.InputConstants;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.client.KeyMapping;
import org.lwjgl.glfw.GLFW;

public class KeyInputHandlerFabric {

    public static KeyMapping dayNightKey;

    public static void registerKeyInputs() {
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (dayNightKey.consumeClick()) {
                ClientPlayNetworking.send(ModMessagesFabric.DAY_NIGHT, PacketByteBufs.create());
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
