package com.example.user.service;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

public class MainActivity extends AppCompatActivity{
    Boolean buttonSituation = false;
    MyService myService;
    Boolean mBound = false;
    ImageButton startpauseService;
    ImageButton stopService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
          startpauseService  = (ImageButton) findViewById(R.id.start_service_id);
          stopService  = (ImageButton) findViewById(R.id.stop_service_id);
        startpauseService.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!buttonSituation){
                    Intent intent = new Intent(MainActivity.this,MyService.class);
                    startService(intent);
                    startpauseService.setImageResource(R.drawable.pausebutton);
                    buttonSituation = true;
                }else {
                    myService.pause();
                    startpauseService.setImageResource(R.drawable.playbutton);
                    buttonSituation = false;
                }
            }
        });
        stopService.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mBound){
                    unbindService(mconnection);
                    myService.stop();
                }
            }
        });
    }
    @Override
    protected void onStart() {
        super.onStart();
            Intent intent = new Intent(MainActivity.this,MyService.class);
            bindService(intent,mconnection,Context.BIND_AUTO_CREATE);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(mBound){
            unbindService(mconnection);
            mBound = false;
        }
    }
    private ServiceConnection mconnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            MyService.LocalBinder  localBinder = (MyService.LocalBinder) service;
            myService = localBinder.getService();
            mBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            mBound = false;
        }
    };

}
