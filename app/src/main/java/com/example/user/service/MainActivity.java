package com.example.user.service;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

public class MainActivity extends AppCompatActivity implements FragmentExample.FragmentListener{

    int musicSituation = MusicSituation.PLAY.setMusic;
    MediaPlayerService mediaPlayerService;
    Boolean mBound = false;
    ImageButton startpauseService;
    ImageButton stopService;
    Intent intent;

    BroadcastReceiver broadcastReceiver = new MyBroadcastReceiver();

    @Override
    protected void onDestroy() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(
                broadcastReceiver);
        super.onDestroy();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Actions
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Constants.IAction.ACTION_PAUSE);
        intentFilter.addAction(Constants.IAction.ACTION_PLAY);
        intentFilter.addAction(Constants.IAction.ACTION_RESUME);
        intentFilter.addAction(Constants.IAction.ACTION_STOP);

        LocalBroadcastManager.getInstance(this).registerReceiver(broadcastReceiver,intentFilter);

          startpauseService  = (ImageButton) findViewById(R.id.start_service_id);
          stopService  = (ImageButton) findViewById(R.id.stop_service_id);

        //play and pause Music
         startpauseService.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (musicSituation == MusicSituation.PLAY.setMusic ){
                     intent = new Intent(MainActivity.this,MediaPlayerService.class);
                    intent.setAction(Constants.IAction.ACTION_PLAY);
                    startService(intent);
                    bindService(intent,mconnection,Context.BIND_AUTO_CREATE);
                    LocalBroadcastManager.getInstance(MainActivity.this).sendBroadcast(intent);
                    startpauseService.setImageResource(R.drawable.pausebutton);
                    musicSituation = MusicSituation.PAUSE.setMusic;
                }else if(musicSituation == MusicSituation.PAUSE.setMusic){
                    mediaPlayerService.pause();
                    intent.setAction(Constants.IAction.ACTION_PAUSE);
                    startpauseService.setImageResource(R.drawable.playbutton);
                    musicSituation = MusicSituation.RESUME.setMusic;
                    LocalBroadcastManager.getInstance(MainActivity.this).sendBroadcast(intent);
                } else{
                    mediaPlayerService.resume();
                    intent.setAction(Constants.IAction.ACTION_RESUME);
                    LocalBroadcastManager.getInstance(MainActivity.this).sendBroadcast(intent);
                    startpauseService.setImageResource(R.drawable.pausebutton);
                    musicSituation = MusicSituation.PAUSE.setMusic;
                }
            }
        });

        //stop music
        stopService.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mBound){
                    unbindService(mconnection);
                    mBound = false;
                }
              stopService(intent);
                intent.setAction(Constants.IAction.ACTION_STOP);
                LocalBroadcastManager.getInstance(MainActivity.this).sendBroadcast(intent);
                musicSituation = MusicSituation.PLAY.setMusic;
                startpauseService.setImageResource(R.drawable.playbutton);
            }
        });
    }

    //Bind service
    private ServiceConnection mconnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            MediaPlayerService.LocalBinder  localBinder = (MediaPlayerService.LocalBinder) service;
            mediaPlayerService = localBinder.getService();
            mBound = true;
        }
        @Override
        public void onServiceDisconnected(ComponentName name) {
            mBound = false;
        }
    };

    @Override
    public void onFragmentCreated() {

    }

    @Override
    public void onFragmentDestroyed() {

    }
    public enum  MusicSituation{
        PLAY(0),
        PAUSE(1),
        RESUME(2),
        STOP(3)
        ;
     public int setMusic;
        MusicSituation(int i) {
            setMusic = i;
        }

    }
}
