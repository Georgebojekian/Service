package com.example.user.service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.widget.RemoteViews;

/**
 * Created by User on 5/30/2017.
 */

public class MyService extends Service  {
    int musicSituation = MainActivity.MusicSituation.PLAY.setMusic;
    private static final String TAG = "MyService";
    private final IBinder mBinder = new LocalBinder();
    MediaPlayer mediaPlayer;
    public static boolean IS_SERVICE_RUNNING = false;
    public static final int NOTIFICATION_ID = R.string.notification_id;


    @Override
    public void onCreate() {
        super.onCreate();
        mediaPlayer = MediaPlayer.create(this,R.raw.nasif);
        mediaPlayer.setLooping(true);
        mediaPlayer.setVolume(100,100);
    }

    private void showNotification() {

        String ns = Context.NOTIFICATION_SERVICE;
        NotificationManager notificationManager = (NotificationManager) getSystemService(ns);
        RemoteViews notificationView = new RemoteViews(getPackageName(),R.layout.activity_main);

        Intent notificationIntent = new Intent(this, MainActivity.class);
        notificationIntent.setAction(Constants.IAction.MAIN_ACTION);
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0,
                notificationIntent, 0);


        Intent playIntent = new Intent(this, MyService.class);
        playIntent.setAction(Constants.IAction.ACTION_PLAY);
        PendingIntent pendingPlayPauseIntent = PendingIntent.getService(this, 0,playIntent, 0);

        Intent pauseIntent = new Intent(this, MyService.class);
        playIntent.setAction(Constants.IAction.ACTION_PAUSE);
        PendingIntent pendingPauseIntent = PendingIntent.getService(this, 0,pauseIntent, 0);

        playPauseMusicInNotification(musicSituation,pendingPlayPauseIntent,notificationView);
        Notification notification   = new  Notification.Builder(this)
                .setContentTitle(getText(R.string.audio_playing))
                .setContentText(getText(R.string.start_service))
                .setSmallIcon(R.drawable.play)
                .setContentIntent(pendingIntent)
                .addAction(android.R.drawable.ic_media_play,"Play" ,pendingPlayPauseIntent)
//               .addAction(android.R.drawable.ic_media_pause,"Pause" ,pendingPauseIntent)
                .setTicker(getText(R.string.start_service))
                .build();

//        notificationManager.notify(1, notification);

        startForeground(NOTIFICATION_ID,notification);
    }

    private void playPauseMusicInNotification(int musicSituation,PendingIntent pendingPlayPauseIntent,RemoteViews notificationView) {
        if (musicSituation == MainActivity.MusicSituation.PAUSE.setMusic) {
            mediaPlayer.pause();
            notificationView.setImageViewResource(R.id.start_service_id, android.R.drawable.ic_media_pause);
            musicSituation = MainActivity.MusicSituation.PAUSE.setMusic;
        }else if(musicSituation == MainActivity.MusicSituation.PAUSE.setMusic) {
            mediaPlayer.start();
            notificationView.setImageViewResource(R.id.start_service_id,android.R.drawable.ic_media_play);
            musicSituation = MainActivity.MusicSituation.PAUSETOPLAY.setMusic;
        }
//        } else{
//            myService.play();
//            intent.setAction(Constants.IAction.ACTION_PLAY);
//            startpauseService.setImageResource(R.drawable.pausebutton);
//            musicSituation = MainActivity.MusicSituation.PAUSE.setMusic;
//        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
          return  mBinder;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        return true;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        mediaPlayer.start();
        if(intent.getAction().equals(Constants.IAction.ACTION_STARTFOREGROUND)){
            showNotification();
        }
//        }else if(intent.getAction().equals(Constants.IAction.ACTION_PAUSE)){
//            mediaPlayer.pause();
//        }

        return START_STICKY;
    }
    public int  play(){
        mediaPlayer.start();
        return START_STICKY;
    }

    public void pause(){
        mediaPlayer.pause();
    }

    public class LocalBinder extends Binder {
        MyService getService(){
            return MyService.this;
        }
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        mediaPlayer.stop();
    }

}
