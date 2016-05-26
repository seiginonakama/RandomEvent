# RandomEvent
***provide simple api to receive android system random event***

---

### Import
```gradle
repositories {
  jcenter()
}
dependencies {
  compile 'me.touko.randomevent:RandomEventLib:0.8.0'
}
```

---
## Now Support Random Event
 * SCREEN_ON: equals Intent.ACTION_SCREEN_ON;
 * SCREEN_OFF: equals Intent.ACTION_SCREEN_OFF;
 * BATTERY_CHANGED: equals Intent.ACTION_BATTERY_CHANGED;
 * TIME_TICK: equals Intent.ACTION_TIME_TICK;


## Usage
### Step 1: call RandomEventManager.init(Context) in you application onCreate() method
```java
    @Override
    public void onCreate() {
      super.onCreate();
      RandomEventManager.getInstance().init(this);
    }
```
### Step 2: register RandomEventObserver
note: onRandomEvent(RandomEvent randomEvent) method are running on ****non-ui thread****
```java
    RandomEventManager.getInstance().registerObserver(new RandomEventObserver() {
      @Override
      protected void onRandomEvent(final RandomEvent randomEvent) {
        handler.post(new Runnable() {
          @Override
          public void run() {
            textView.setText("receive event:" + randomEvent.name());
          }
        });
      }
    });
```
### (Optional) Step 3 : override  RandomEventObserver method if you want to customize
```java
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
```