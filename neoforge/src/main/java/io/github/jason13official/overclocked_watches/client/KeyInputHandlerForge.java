package io.github.jason13official.overclocked_watches.client;

import com.mojang.blaze3d.platform.InputConstants;
import io.github.jason13official.overclocked_watches.Constants;
import io.github.jason13official.overclocked_watches.impl.client.ClientModConfig;
import net.minecraft.client.KeyMapping;
import net.minecraft.resources.Identifier;
import net.neoforged.neoforge.client.settings.KeyConflictContext;

public class KeyInputHandlerForge {

  public static final KeyMapping.Category DAY_NIGHT_CATEGORY = KeyMapping.Category.register(
      Identifier.fromNamespaceAndPath(Constants.MOD_ID, "day_night"));

  public static final KeyMapping dayNightKey = new KeyMapping(
      Constants.KEY_DAY_NIGHT,
      KeyConflictContext.IN_GAME,
      InputConstants.Type.KEYSYM,
      Math.toIntExact(ClientModConfig.DEFAULT_DAY_NIGHT_KEY.get()),
      DAY_NIGHT_CATEGORY
  );
}
