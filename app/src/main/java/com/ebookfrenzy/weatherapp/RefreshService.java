package com.ebookfrenzy.weatherapp;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import java.util.Timer;
import java.util.TimerTask;

public class RefreshService extends Service {

    private static final String TAG = "RefreshService";
    private Timer timer = new Timer();
    private int checker = 1;

    @Override
    public void onCreate() {
        Log.d(TAG, "onCreate: service created");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "onStartCommand: service started");
        if (checker == 1) {
            timer.schedule(new updateWeatherInfo(), 0, 3000 * 60 * 60);
            checker++;
        }
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        Log.d(TAG, "onDestroy: service destroyed");
        timer.cancel();
        checker = 1;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    class updateWeatherInfo extends TimerTask {

        @Override
        public void run() {
            MainActivity mainActivity = new MainActivity();
            mainActivity.updateCurrentFragment();
            Log.d(TAG, "run: notification sent");
        }
    }
}
