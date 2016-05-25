package me.touko.randomevent;

import android.app.IntentService;
import android.content.Intent;

/**
 * author: zhoulei date: 15/7/7.
 */
public class RandomEventProcessService extends IntentService {
  public static final String KEY_INTENT_ACTION = "intent_action";

  public RandomEventProcessService() {
    super(RandomEventProcessService.class.getName());
  }

  @Override
  protected void onHandleIntent(Intent intent) {
    String intentAction = intent.getStringExtra(KEY_INTENT_ACTION);
    RandomEvent startEvent = RandomEvent.getRandomEvent(intentAction);
    if (startEvent != null) {
      RandomEventManager.getInstance().onRandomEvent(RandomEvent.getRandomEvent(intentAction));
    }
  }
}
