package io.github.jason13official.overclocked_watches.impl.common.util;

import io.github.jason13official.overclocked_watches.impl.common.ServerModConfig;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.server.level.ServerLevel;

public class TimeManager {

  public static TimeManager SERVER = null;
  public static TimeManager CLIENT = null;

  private int remainingTime = 0;

  public int getRemainingTime() {
    return remainingTime;
  }

  public void addToRemainingTime(int value) {
    remainingTime += value;
  }

  public void decrementRemainingTime() {
    if (shouldOperate()) {
      remainingTime -= Math.toIntExact(ServerModConfig.LONG_TIME_DELTA.get());
    }
  }

  public boolean shouldOperate() {
    return remainingTime >= ServerModConfig.LONG_TIME_DELTA.get();
  }

  public void operate(ServerLevel level) {
    // System.out.println("operating on server");
    OverclockedWatchesUtil.advanceDayTime(level, ServerModConfig.LONG_TIME_DELTA.get());
    decrementRemainingTime();
  }

  public void operate(ClientLevel level) {
    // Time is server-authoritative; the client just waits for the synced clock update and ticks down locally.
    decrementRemainingTime();
  }
}
