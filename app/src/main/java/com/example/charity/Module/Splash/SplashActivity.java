package com.example.charity.Module.Splash;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.example.charity.Module.Home.HomeActivity;
import com.example.charity.Module.Login.LoginActivity;
import com.example.charity.Application.MyActivity;
import com.example.charity.R;
import com.example.charity.Utility.Constants;
import com.example.charity.Utility.Utilities;

public class SplashActivity extends MyActivity {

    private static int SPLASH_TIMEOUT = 3000;

    private TextView txtSplash;

    SharedPreferences preferences;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // hide status bar
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_splash);

        txtSplash = findViewById(R.id.txt_splash);
        startAnimation();

        preferences = getSharedPreferences(Constants.SHARED_PREFERENCE_NAME, MODE_PRIVATE);

        if (!Utilities.isConnected(this)) {
            Toast.makeText(this, getResources().getString(R.string.toast_no_internet),
                    Toast.LENGTH_LONG).show();
        }

        if (preferences.getBoolean("isLogin", false)) {
            setConstants();
            gotoHome();
        } else {
            gotoLogin();
        }

    }

    private void gotoLogin() {
        new Handler().postDelayed(() -> {
            startActivity(new Intent(SplashActivity.this, LoginActivity.class));
            finish();
        }, this.SPLASH_TIMEOUT);
    }

    private void gotoHome() {
        new Handler().postDelayed(() -> {
            startActivity(new Intent(SplashActivity.this, HomeActivity.class));
            finish();
        }, this.SPLASH_TIMEOUT);
    }

    private void setConstants() {
        Constants.CLIENT_ID = preferences.getString(Constants.CLIENT_ID_TEXT, "");
        Constants.PHONE_NUMBER = preferences.getString(Constants.PHONE_NUMBER_TEXT, "");
        Constants.USER_TYPE = preferences.getString(Constants.USER_TYPE_TEXT, "");
    }

    private void startAnimation() {
        final AnimatorSet mAnimatorSet = new AnimatorSet();
        mAnimatorSet.playTogether(
                ObjectAnimator.ofFloat(txtSplash, "scaleX",
                        1, 0.9f, 0.9f, 1.5f, 1.5f, 1),

                ObjectAnimator.ofFloat(txtSplash, "scaleY",
                        1, 0.9f, 0.9f, 1.5f, 1.5f, 1),

                ObjectAnimator.ofFloat(txtSplash, "rotation",
                        0, -10, -10, 10, -10, 10, -10, 10, -10, 10, -10, 0)
        );

        mAnimatorSet.setStartDelay(250);
        mAnimatorSet.setDuration(2000);
        mAnimatorSet.start();
    }


}

