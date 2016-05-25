package me.touko.randomevent.demo;

import me.touko.randomevent.RandomEventManager;

/**
 * author: zhou date: 2016/5/25.
 */
public class Application extends android.app.Application {
  @Override
  public void onCreate() {
    super.onCreate();
    RandomEventManager.getInstance().init(this);
  }
}
