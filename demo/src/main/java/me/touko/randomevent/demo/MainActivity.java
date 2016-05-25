package me.touko.randomevent.demo;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import me.touko.randomevent.RandomEvent;
import me.touko.randomevent.RandomEventManager;
import me.touko.randomevent.RandomEventObserver;

public class MainActivity extends AppCompatActivity {
  private static Handler handler = new Handler(Looper.getMainLooper());

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    final TextView textView = (TextView) findViewById(R.id.event);
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
  }
}
