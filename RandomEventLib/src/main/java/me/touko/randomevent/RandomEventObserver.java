package me.touko.randomevent;

import java.util.Set;

/**
 * Application can implement this and to observer random event after call RandomEventManager.registerObserver(RandomEventObserver autoStartConfig)
 * {@link RandomEventManager}
 *
 * @author zhoulei@shandianshua.com (Zhou Lei)
 */
public abstract class RandomEventObserver {
  /**
   * events to observer, default {@link RandomEvent#OFFEN_EVENTS}
   * you can override this to use your focus events
   *
   * @return focus events
   */
  Set<RandomEvent> focusRandomEvents() {
    return RandomEvent.OFFEN_EVENTS;
  }

  /**
   * the min duration between random events callback, default 0
   *
   * @return min duration between {@link #onRandomEvent(RandomEvent)}
   */
  protected long minDuration() {
    return 0L;
  }

  /**
   * when one of your focus RandomEvent occurred, this method will be called
   *
   * note: onRandomEvent(RandomEvent) running on non-ui thread
   *
   *{@link RandomEvent}
   *
   * @param randomEvent the happened focus event
   */
  protected abstract void onRandomEvent(RandomEvent randomEvent);
}
