package com.example.user.service;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;

/**
 * Created by User on 5/30/2017.
 */

public class MyService extends Service  {
    private static final String TAG = null;
    private final IBinder mBinder = new LocalBinder();
    MediaPlayer mediaPlayer;

    @Override
    public void onCreate() {
        super.onCreate();
        mediaPlayer = MediaPlayer.create(this,R.raw.nasif);
        mediaPlayer.setLooping(true);
        mediaPlayer.setVolume(100,100);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
          return  mBinder;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        mediaPlayer.start();
        return START_STICKY;
    }

    public int  play(){
        mediaPlayer.start();
        return START_STICKY;
    }
    public void stop(){
        mediaPlayer.stop();
        mediaPlayer.release();
    }
    public void pause(){
        mediaPlayer.pause();
    }

    public class LocalBinder extends Binder {
        MyService getService(){
            return MyService.this;
        }
    }
}
