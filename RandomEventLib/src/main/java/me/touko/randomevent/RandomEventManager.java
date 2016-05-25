package me.touko.randomevent;

import android.content.Context;
import android.content.IntentFilter;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * you can through RandomEventManager's simple api to receive random system event
 *
 * {@link RandomEventObserver}
 * {@link #registerObserver(RandomEventObserver)}
 * {@link #unregisterObserver(RandomEventObserver)}
 *
 * author: zhoulei date: 15/7/7.
 */
public class RandomEventManager {
  private static RandomEventManager instance;
  private static List<WeakReference<RandomEventObserver>> listeners = new ArrayList<>();
  private static final byte[] listenerLock = new byte[0];
  private static final Map<RandomEventObserver, Long> lastEventInvokeTimeMap = new HashMap<>();

  private Executor singleThreadPool = Executors.newSingleThreadExecutor();

  /**
   * get the singleton of RandomEventManager
   *
   * @return the singleton of RandomEventManager
   */
  public static synchronized RandomEventManager getInstance() {
    if (instance == null) {
      instance = new RandomEventManager();
    }
    return instance;
  }

  private RandomEventManager() {
  }

  /**
   * init method for RandomEventManager, must call before use
   *
   * @param context must not be null
   */
  public void init(Context context) {
    if(context == null) {
      throw new IllegalArgumentException("context can't be null");
    }
    initReceiver(context.getApplicationContext());
  }

  private void initReceiver(Context context) {
    RandomEventReceiver receiver = new RandomEventReceiver();

    IntentFilter intentFilter = new IntentFilter();
    for (RandomEvent randomEvent : RandomEvent.values()) {
      intentFilter.addAction(randomEvent.getIntentAction());
    }
    context.registerReceiver(receiver, intentFilter);
  }

  /**
   * add a RandomEventObserver to RandomEventManager
   *
   * @param randomEventObserver link{com.shandianshua.autostart.RandomEventObserver}
   */
  public void registerObserver(RandomEventObserver randomEventObserver) {
    if (isObserverExist(randomEventObserver)) {
      return;
    }
    synchronized (listenerLock) {
      listeners.add(new WeakReference<>(randomEventObserver));
    }
  }

  /**
   * remove the RandomEventObserver from RandomEventManager
   *
   * @param randomEventObserver link{com.shandianshua.autostart.RandomEventObserver}
   */
  public void unregisterObserver(RandomEventObserver randomEventObserver) {
    synchronized (listenerLock) {
      if (listeners.size() <= 0) {
        return;
      }
      Iterator<WeakReference<RandomEventObserver>> iterator = listeners.iterator();
      while (iterator.hasNext()) {
        WeakReference<RandomEventObserver> autoStartConfigWeakReference = iterator.next();
        if (autoStartConfigWeakReference == null || autoStartConfigWeakReference.get() == null) {
          iterator.remove();
          continue;
        }
        if (autoStartConfigWeakReference.get() == randomEventObserver) {
          iterator.remove();
          break;
        }
      }
    }
  }

  private boolean isObserverExist(RandomEventObserver randomEventObserver) {
    synchronized (listenerLock) {
      if (listeners.size() <= 0) {
        return false;
      }
      Iterator<WeakReference<RandomEventObserver>> iterator = listeners.iterator();
      while (iterator.hasNext()) {
        WeakReference<RandomEventObserver> autoStartConfigWeakReference = iterator.next();
        if (autoStartConfigWeakReference == null || autoStartConfigWeakReference.get() == null) {
          iterator.remove();
          continue;
        }
        if (autoStartConfigWeakReference.get() == randomEventObserver) {
          return true;
        }
      }
      return false;
    }
  }

  void onRandomEvent(final RandomEvent randomEvent) {
    if (randomEvent == null) {
      return;
    }

    synchronized (listenerLock) {
      if (listeners.size() < 0) {
        return;
      }

      Iterator<WeakReference<RandomEventObserver>> iterator = listeners.iterator();
      while (iterator.hasNext()) {
        WeakReference<RandomEventObserver> autoStartConfigWeakReference = iterator.next();
        if (autoStartConfigWeakReference == null || autoStartConfigWeakReference.get() == null) {
          iterator.remove();
          continue;
        }
        final RandomEventObserver randomEventObserver = autoStartConfigWeakReference.get();
        if (checkAutoStartValid(randomEventObserver, randomEvent)) {
          updateLastStartTime(randomEventObserver);
          singleThreadPool.execute(new Runnable() {
            @Override
            public void run() {
              randomEventObserver.onRandomEvent(randomEvent);
            }
          });
          break;
        }
      }
    }
  }

  private boolean checkAutoStartValid(RandomEventObserver randomEventObserver, RandomEvent randomEvent) {
    if (randomEventObserver == null || randomEvent == null) {
      return false;
    }
    return randomEventObserver.focusRandomEvents().contains(randomEvent) && checkStartTimeValid(randomEventObserver);
  }

  private void updateLastStartTime(RandomEventObserver key) {
    if (key == null) {
      return;
    }
    lastEventInvokeTimeMap.put(key, System.currentTimeMillis());
  }

  private boolean checkStartTimeValid(RandomEventObserver key) {
    if (key == null) {
      return false;
    }
    Long lastStartTime = lastEventInvokeTimeMap.get(key);
    return lastStartTime == null || System.currentTimeMillis() - lastStartTime >= key.minDuration();
  }
}
