package com.example.user.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

/**
 * Created by User on 6/5/2017.
 */

public class MyBroadcastReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(Constants.IAction.ACTION_PAUSE)){
            Toast.makeText(context,"Pause Music",Toast.LENGTH_SHORT).show();
        }else if(intent.getAction().equals(Constants.IAction.ACTION_PLAY)) {
            Toast.makeText(context, "Play Music", Toast.LENGTH_SHORT).show();
        }else if(intent.getAction().equals(Constants.IAction.ACTION_RESUME)){
                Toast.makeText(context,"Resume Music",Toast.LENGTH_SHORT).show();
        }else {
            Toast.makeText(context, "Stop Music", Toast.LENGTH_SHORT).show();
        }
    }
}
