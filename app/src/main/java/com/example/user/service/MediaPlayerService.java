package com.example.user.service;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.widget.RemoteViews;

/**
 * Created by User on 5/30/2017.
 */

public class MediaPlayerService extends Service  implements  MediaPlayer.OnPreparedListener,MediaPlayer.OnCompletionListener, MediaPlayer.OnErrorListener, MediaPlayer.OnSeekCompleteListener,
        MediaPlayer.OnInfoListener, MediaPlayer.OnBufferingUpdateListener, AudioManager.OnAudioFocusChangeListener  {

    private final IBinder mBinder = new LocalBinder();

    MediaPlayer mediaPlayer;
    private String mediaFile;
    public static boolean IS_SERVICE_RUNNING = false;
    public static final int NOTIFICATION_ID = R.string.notification_id;
    private NotificationCompat.Builder mBuilder;
    private NotificationManager mNotificationManager;


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
          return  mBinder;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        return true;
    }
    private void initeMediaPlayer() {
        mediaPlayer = MediaPlayer.create(this,R.raw.nasif);
        mediaPlayer.setLooping(true);
        mediaPlayer.setVolume(100,100);
        mediaPlayer.setOnPreparedListener(this);
    }
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if(intent.getAction().equals(Constants.IAction.ACTION_PLAY)){
            initeMediaPlayer();
            setUpNotification(intent);
            LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
        }
        else if(intent.getAction().equals(Constants.IAction.ACTION_PAUSE)){
            mediaPlayer.pause();
            setUpNotification(intent);
            LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
        }
        else if (intent.getAction().equals(Constants.IAction.ACTION_RESUME)){
            mediaPlayer.start();
            setUpNotification(intent);
            LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
        }
        return START_STICKY;
    }

    private void setUpNotification(Intent intent) {
        mNotificationManager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
        Intent intentNotif  = new Intent(this, MainActivity.class);
        intentNotif .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent pendIntent = PendingIntent.getActivity(this, 0, intentNotif , 0);

        RemoteViews mRemoteViews = new RemoteViews(getPackageName(), R.layout.notification_layout);

       if(intent.getAction().equals(Constants.IAction.ACTION_PLAY)) {
           Intent pauseIntent = new Intent(this, MediaPlayerService.class);

           pauseIntent.setAction(Constants.IAction.ACTION_PAUSE);
           PendingIntent pendingPauseIntent = PendingIntent.getService(this, 0, pauseIntent, 0);

           mRemoteViews.setOnClickPendingIntent(R.id.play_pause_music_id, pendingPauseIntent);

           // notification's icon
           mRemoteViews.setImageViewResource(R.id.play_pause_music_id, android.R.drawable.ic_media_pause);
           // notification's title
           mRemoteViews.setTextViewText(R.id.notif_title_id,getResources().getString(R.string.audio_playing));
           // notification's content
           mRemoteViews.setContentDescription(R.id.notif_content_id, getResources().getString(R.string.audio_name));

           mBuilder = new NotificationCompat.Builder(this);

           CharSequence ticker = getResources().getString(R.string.ticker_text);

           mBuilder.setSmallIcon(R.drawable.pausebutton)
                   .setAutoCancel(false)
                   .setOngoing(true)
                   .setContentIntent(pendIntent)
                   .setContent(mRemoteViews)
                   .setTicker(ticker);

           // starting service with notification in foreground mode
           startForeground(NOTIFICATION_ID, mBuilder.build());
       }else if (intent.getAction().equals(Constants.IAction.ACTION_PAUSE)){
           Intent playIntent = new Intent(this, MediaPlayerService.class);
           playIntent.setAction(Constants.IAction.ACTION_RESUME);
           PendingIntent pendingPlayIntent = PendingIntent.getService(this, 0, playIntent, 0);

           mRemoteViews.setOnClickPendingIntent(R.id.play_pause_music_id,pendingPlayIntent);
           // update the icon
           mRemoteViews.setImageViewResource(R.id.play_pause_music_id, android.R.drawable.ic_media_play);
           // update the title
           mRemoteViews.setTextViewText(R.id.notif_title_id, getResources().getString(R.string.audio_playing));
           // update the content
//           mRemoteViews.setContentDescription(R.id.notif_content_id, getResources().getString(R.string.audio_name));
           CharSequence ticker = getResources().getString(R.string.ticker_text);
           mBuilder.setSmallIcon(R.drawable.play)
                   .setAutoCancel(false)
                   .setOngoing(true)
                   .setContentIntent(pendIntent)
                   .setContent(mRemoteViews)
                   .setTicker(ticker);
           startForeground(NOTIFICATION_ID, mBuilder.build());
       }else if (intent.getAction().equals(Constants.IAction.ACTION_RESUME)){
           Intent resumeIntent = new Intent(this, MediaPlayerService.class);
           resumeIntent.setAction(Constants.IAction.ACTION_PAUSE);
           PendingIntent pendingResumeIntent = PendingIntent.getService(this, 0, resumeIntent, 0);

           mRemoteViews.setOnClickPendingIntent(R.id.play_pause_music_id,pendingResumeIntent);
           // update the icon
           mRemoteViews.setImageViewResource(R.id.play_pause_music_id, android.R.drawable.ic_media_pause);
           // update the title
           mRemoteViews.setTextViewText(R.id.notif_title_id, getResources().getString(R.string.audio_playing));
           // update the content
//           mRemoteViews.setContentDescription(R.id.notif_content_id, getResources().getString(R.string.audio_name));
           CharSequence ticker = getResources().getString(R.string.ticker_text);
           mBuilder.setSmallIcon(R.drawable.pause)
                   .setAutoCancel(false)
                   .setOngoing(true)
                   .setContentIntent(pendIntent)
                   .setContent(mRemoteViews)
                   .setTicker(ticker);
           startForeground(NOTIFICATION_ID, mBuilder.build());
       }
    }

    public void resume(){
        mediaPlayer.start();
    }
    public void pause(){
        mediaPlayer.pause();
    }
    private void stopMedia() {
        if (mediaPlayer == null) return;
        if (mediaPlayer.isPlaying()) {
            mediaPlayer.stop();
            mediaPlayer.release();
        }
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mediaPlayer == null) return;
        if (mediaPlayer.isPlaying()) {
            mediaPlayer.stop();
            mediaPlayer.release();
        }
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        mediaPlayer.start();
    }

    @Override
    public void onAudioFocusChange(int focusChange) {

    }

    @Override
    public void onBufferingUpdate(MediaPlayer mp, int percent) {

    }

    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
        return false;
    }

    @Override
    public boolean onInfo(MediaPlayer mp, int what, int extra) {
        return false;
    }

    @Override
    public void onSeekComplete(MediaPlayer mp) {

    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        
    }

    public class LocalBinder extends Binder {
        MediaPlayerService getService(){
            return MediaPlayerService.this;
        }
    }


}
