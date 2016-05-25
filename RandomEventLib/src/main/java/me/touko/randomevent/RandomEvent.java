package me.touko.randomevent;

import android.content.Intent;
import android.text.TextUtils;

import java.util.HashSet;
import java.util.Set;

/**
 * author: zhoulei date: 15/7/7.
 */
public enum RandomEvent {
  SCREEN_ON(Intent.ACTION_SCREEN_ON),
  SCREEN_OFF(Intent.ACTION_SCREEN_OFF),
  BATTERY_CHANGED(Intent.ACTION_BATTERY_CHANGED),
  TIME_TICK(Intent.ACTION_TIME_TICK);

  public static final Set<RandomEvent> ONLY_TIME_TICK;
  public static final Set<RandomEvent> ONLY_BATTERY_CHANGED;
  public static final Set<RandomEvent> OFFEN_EVENTS;
  public static final Set<RandomEvent> SCREEN_EVENTS;

  static {
    ONLY_BATTERY_CHANGED = new HashSet<>();
    ONLY_BATTERY_CHANGED.add(BATTERY_CHANGED);

    ONLY_TIME_TICK = new HashSet<>();
    ONLY_TIME_TICK.add(TIME_TICK);

    SCREEN_EVENTS = new HashSet<>();
    SCREEN_EVENTS.add(SCREEN_ON);
    SCREEN_EVENTS.add(SCREEN_OFF);

    OFFEN_EVENTS = new HashSet<>();
    OFFEN_EVENTS.addAll(ONLY_BATTERY_CHANGED);
    OFFEN_EVENTS.addAll(ONLY_TIME_TICK);
    OFFEN_EVENTS.addAll(SCREEN_EVENTS);
  }

  private String intentAction;

  public static RandomEvent getRandomEvent(String intentAction) {
    if (TextUtils.isEmpty(intentAction)) {
      return null;
    }
    for (RandomEvent randomEvent : RandomEvent.values()) {
      if (randomEvent.getIntentAction().equals(intentAction)) {
        return randomEvent;
      }
    }
    return null;
  }

  RandomEvent(String intentAction) {
    this.intentAction = intentAction;
  }

  public String getIntentAction() {
    return intentAction;
  }
}
