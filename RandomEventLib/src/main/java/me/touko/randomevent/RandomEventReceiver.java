package me.touko.randomevent;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * author: zhoulei date: 15/7/7.
 */
class RandomEventReceiver extends BroadcastReceiver {

  @Override
  public void onReceive(Context context, Intent intent) {
    if (intent == null || intent.getAction() == null) {
      return;
    }

    Intent newIntent = new Intent(context, RandomEventProcessService.class);
    newIntent.putExtra(RandomEventProcessService.KEY_INTENT_ACTION, intent.getAction());
    context.startService(newIntent);
  }
}
