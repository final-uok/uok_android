package com.example.charity.Application;

import android.app.Application;

import com.example.charity.R;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

public class MyApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        CalligraphyConfig.initDefault(
                new CalligraphyConfig.Builder()
                        .setDefaultFontPath("fonts/BNAZANIN.TTF")
                        .setFontAttrId(R.attr.fontPath)
                        .build()
        );

    }

}
