package com.example.charity.Application;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.example.charity.Utility.Constants;
import com.example.charity.Utility.Utilities;

import static android.content.Context.MODE_PRIVATE;

public class ConnectivityReceiver extends BroadcastReceiver {

    SharedPreferences preferences;

    @Override
    public void onReceive(Context context, Intent intent) {
        preferences = context.getSharedPreferences(Constants.SHARED_PREFERENCE_NAME, MODE_PRIVATE);


        if (preferences.getBoolean("isLogin", false) && Utilities.isConnected(context)) {
            context.startService(new Intent(context, CharityPollingService.class));
        } else {
            context.stopService(new Intent(context, CharityPollingService.class));
        }
    }

}
