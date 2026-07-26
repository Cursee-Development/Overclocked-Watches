package io.github.jason13official.overclocked_watches;

import io.github.jason13official.overclocked_watches.impl.common.ModConfigIO;
import io.github.jason13official.overclocked_watches.impl.common.util.TimeManager;
import io.github.jason13official.overclocked_watches.platform.Services;
import net.minecraft.resources.Identifier;

public class OverclockedWatches {

  public static void init() {
    TimeManager.SERVER = new TimeManager();

    ModConfigIO.load(Services.PLATFORM.getGameDirectory());
  }

  public static Identifier identifier(String value) {
    return Identifier.fromNamespaceAndPath(Constants.MOD_ID, value);
  }
}