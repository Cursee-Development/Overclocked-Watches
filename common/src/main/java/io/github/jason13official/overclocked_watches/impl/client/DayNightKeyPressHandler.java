package io.github.jason13official.overclocked_watches.impl.client;

import io.github.jason13official.overclocked_watches.impl.common.ServerModConfig;
import io.github.jason13official.overclocked_watches.impl.common.item.WatchItem;
import io.github.jason13official.overclocked_watches.impl.common.util.TimeManager;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.item.Item;

public class DayNightKeyPressHandler {

  public static void handle(ClientLevel level, LocalPlayer player) {

    long pAmount = getTimeAdvanceAmount(player.getMainHandItem().getItem());

    // Time is server-authoritative; the server advances the clock once it receives our C2S packet.
    if (ServerModConfig.USE_LONG_TIME_DELTA.get()) {
      TimeManager.CLIENT.addToRemainingTime((int) pAmount);
    }

    player.playSound(SoundEvents.BELL_BLOCK, 1.0f, 1.0f);
    player.playSound(SoundEvents.BELL_RESONATE, 1.0f, 1.0f);
  }

  private static long getTimeAdvanceAmount(Item mainHandItem) {

    if (mainHandItem instanceof WatchItem watchItem) {
      return watchItem.getTier().getTimeAdvancementTicks();
    }

    return 0L;
  }
}
