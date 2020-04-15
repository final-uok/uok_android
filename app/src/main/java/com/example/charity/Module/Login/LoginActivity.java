package com.example.charity.Module.Login;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.example.charity.Application.Network.APIClient;
import com.example.charity.Application.Network.APIInterface;
import com.example.charity.Model.LoginResponse;
import com.example.charity.Module.Home.HomeActivity;
import com.example.charity.Module.RequestOTP.RequestOTPActivity;
import com.example.charity.Application.MyActivity;
import com.example.charity.R;
import com.example.charity.Application.CharityPollingService;
import com.example.charity.Utility.Constants;
import com.example.charity.Utility.Utilities;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends MyActivity {

    private EditText edtUserName;
    private EditText edtPassword;

    private RadioGroup radioGroup;

    private Button btnSend;
    private Button btnGoToSignupActivity;

    private ProgressBar progressBar;

    private RelativeLayout relativeLayout;

    private APIInterface apiInterface;

    SharedPreferences preferences;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        hideBackButton();

        LoginActivity.this.setTitle(getResources().getString(R.string.login_screen_title));

        apiInterface = APIClient.getRetrofitInstance().create(APIInterface.class);

        findViews();

        radioGroup.setOnCheckedChangeListener((group, checkedId) -> {
            switch (checkedId) {
                case R.id.radio_login_charity:
                    Constants.USER_TYPE = "0";
                    break;

                case R.id.radio_login_philanthropist:
                    Constants.USER_TYPE = "1";
                    break;
            }
        });

        handleTaps();
    }

    private void handleTaps() {
        btnGoToSignupActivity.setOnClickListener(v -> {
            startActivity(new Intent(LoginActivity.this, RequestOTPActivity.class));
        });

        btnSend.setOnClickListener(v -> {
            if (!isEmpty()) {
                if (!Utilities.isConnected(getApplicationContext())) {
                    Toast.makeText(getApplicationContext(),
                            getResources().getString(R.string.toast_no_internet_before_acttion),
                            Toast.LENGTH_SHORT).show();
                } else {
                    showProgressbar();
                    sendLoginRequest();
                }

            } else {
                Toast.makeText(
                        this,
                        getResources().getString(R.string.error_incomplete_input),
                        Toast.LENGTH_SHORT
                ).show();
            }

        });
    }

    private void showProgressbar() {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

        Utilities.blurAll(true, relativeLayout);
        progressBar.setVisibility(View.VISIBLE);
    }

    private void sendLoginRequest() {
        final LoginResponse login = new LoginResponse(
                edtUserName.getText().toString().trim(),
                edtPassword.getText().toString().trim(),
                Constants.USER_TYPE
        );

        Call<LoginResponse> call = apiInterface.login(login);
        call.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                Utilities.blurAll(false, relativeLayout);
                progressBar.setVisibility(View.INVISIBLE);

                LoginResponse loginResponse = response.body();

                if (loginResponse != null) {
                    if (loginResponse.getResponseCode() == 100) {
                        setConstants(loginResponse);

                        setPreferences(loginResponse);

                        Toast.makeText(
                                LoginActivity.this,
                                getResources().getString(R.string.toast_wellcome),
                                Toast.LENGTH_SHORT
                        ).show();

                        startPolling();

                        startActivity(new Intent(LoginActivity.this, HomeActivity.class));

                    } else if (loginResponse.getResponseCode() == 2) {
                        Toast.makeText(
                                LoginActivity.this,
                                getResources().getString(R.string.error_code_2),
                                Toast.LENGTH_SHORT
                        ).show();
                    }

                } else {
                    Toast.makeText(getApplicationContext(), "null response", Toast.LENGTH_SHORT)
                            .show();
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "onFailure called ", Toast.LENGTH_SHORT).show();
                call.cancel();
            }
        });
    }

    private void startPolling() {
        if (Constants.USER_TYPE == "0") { // charity
            startService(new Intent(getApplicationContext(), CharityPollingService.class));
        }
    }

    private void setConstants(LoginResponse loginResponse) {
        Constants.CLIENT_ID = loginResponse.getClientId();
        Constants.PHONE_NUMBER = loginResponse.getPhoneNumber();
    }

    private void setPreferences(LoginResponse loginResponse) {
        preferences = getSharedPreferences(Constants.SHARED_PREFERENCE_NAME, MODE_PRIVATE);
        editor = preferences.edit();

        editor.putBoolean("isLogin", true);
        editor.putString(Constants.CLIENT_ID_TEXT, loginResponse.getClientId());
        editor.putString(Constants.PHONE_NUMBER_TEXT, loginResponse.getPhoneNumber());
        editor.putString(Constants.USER_TYPE_TEXT, loginResponse.getUserType());
        editor.apply();
    }

    private boolean isEmpty() {
        if (edtUserName.getText().toString().trim().isEmpty()
                || edtPassword.getText().toString().trim().isEmpty()) {
            return true;
        }

        return false;
    }

    private void findViews() {
        edtUserName = findViewById(R.id.edt_login_userName);
        edtPassword = findViewById(R.id.edt_login_password);

        radioGroup = findViewById(R.id.radioGroup_login);

        btnSend = findViewById(R.id.btn_login_send);
        btnGoToSignupActivity = findViewById(R.id.btn_login_signup);

        progressBar = findViewById(R.id.progressBar_login);

        relativeLayout = findViewById(R.id.relativeLayout_login);
    }

    @Override
    protected void onStop() {
        super.onStop();

        edtUserName.setText("");
        edtPassword.setText("");
    }

}