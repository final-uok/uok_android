package com.example.charity.Utility;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.view.View;
import android.view.ViewGroup;

import jp.wasabeef.blurry.Blurry;

public class Utilities {

    public static void blurAll(boolean enable, View view) {
        if (!enable) {
            Blurry.delete((ViewGroup) view);
        } else {

            Blurry.with(view.getContext())
                    .radius(25)
                    .sampling(2)
                    .onto((ViewGroup) view);
        }

    }

    public static boolean isConnected(Context context) {
        ConnectivityManager connectivityManager =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = connectivityManager.getActiveNetworkInfo();
        return (netInfo != null && netInfo.isConnectedOrConnecting());
    }

}
