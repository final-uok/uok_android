package com.example.charity.Module.Signup;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.example.charity.Application.Network.APIClient;
import com.example.charity.Application.Network.APIInterface;
import com.example.charity.Model.SignupResponse;
import com.example.charity.Application.MyActivity;
import com.example.charity.Module.Home.HomeActivity;
import com.example.charity.R;
import com.example.charity.Utility.Constants;
import com.example.charity.Utility.Utilities;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignupActivity extends MyActivity {

    private EditText edtUserName;
    private EditText edtPassword;
    private EditText edtRePassword;

    private Button btnSend;

    private APIInterface apiInterface;

    SharedPreferences preferences;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        SignupActivity.this.setTitle(getResources().getString(R.string.signup_screen_title));

        apiInterface = APIClient.getRetrofitInstance().create(APIInterface.class);

        findViews();

        btnSend.setOnClickListener(v -> {
            if (isValidateEditTexts()) {
                if (!Utilities.isConnected(getApplicationContext())) {
                    Toast.makeText(getApplicationContext(),
                            getResources().getString(R.string.toast_no_internet_before_acttion),
                            Toast.LENGTH_SHORT).show();
                } else {
                    signupRequest();
                }
            }
        });

    }

    private void signupRequest() {
        SignupResponse signup = new SignupResponse(
                edtUserName.getText().toString().trim(),
                edtPassword.getText().toString().trim(),
                Constants.PHONE_NUMBER,
                "1"); // always 1 for sign up

        Call<SignupResponse> call = apiInterface.signup(signup);
        call.enqueue(new Callback<SignupResponse>() {
            @Override
            public void onResponse(Call<SignupResponse> call, Response<SignupResponse> response) {
                SignupResponse signupResponse = response.body();

                if (signupResponse != null) {
                    if (signupResponse.getResponseCode() == 100) {
                        Toast.makeText(SignupActivity.this,
                                getResources().getString(R.string.toast_signup_successful),
                                Toast.LENGTH_SHORT).show();
                        setConstants(signupResponse);
                        setPreferences();
                        startActivity(new Intent(SignupActivity.this, HomeActivity.class));

                    } else if (signupResponse.getResponseCode() == 4) {
                        Toast.makeText(
                                getApplicationContext(),
                                getResources().getString(R.string.error_code_4),
                                Toast.LENGTH_SHORT
                        ).show();

                    } else if (signupResponse.getResponseCode() == 10) {
                        Toast.makeText(
                                getApplicationContext(),
                                getResources().getString(R.string.error_code_10),
                                Toast.LENGTH_SHORT
                        ).show();
                    }

                } else {
                    Toast.makeText(getApplicationContext(), "null response", Toast.LENGTH_SHORT)
                            .show();
                }
            }

            @Override
            public void onFailure(Call<SignupResponse> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "onFailure called ", Toast.LENGTH_SHORT).show();
                call.cancel();
            }
        });
    }

    private void setConstants(SignupResponse signup) {
        Constants.USER_TYPE = signup.getUserType();
        Constants.CLIENT_ID = signup.getClientId();
    }

    private void setPreferences() {
        preferences = getSharedPreferences(Constants.SHARED_PREFERENCE_NAME, MODE_PRIVATE);
        editor = preferences.edit();

        editor.putBoolean("isLogin", true);
        editor.putString(Constants.CLIENT_ID_TEXT, Constants.CLIENT_ID);
        editor.putString(Constants.PHONE_NUMBER_TEXT, Constants.PHONE_NUMBER);
        editor.putString(Constants.USER_TYPE_TEXT, Constants.USER_TYPE);
        editor.apply();
    }

    private boolean isValidateEditTexts() {
        if (isEmpty()) {
            Toast.makeText(
                    this,
                    getResources().getString(R.string.error_incomplete_input),
                    Toast.LENGTH_SHORT
            ).show();

            return false;

        } else if (isConflictPasswords()) {
            Toast.makeText(
                    this,
                    getResources().getString(R.string.error_conflict_password),
                    Toast.LENGTH_SHORT
            ).show();

            return false;
        }

        return true;
    }

    private boolean isConflictPasswords() {
        if (edtPassword.getText().toString().trim().equals(edtRePassword.getText().toString().trim())) {
            return false;
        }
        return true;
    }

    private boolean isEmpty() {
        if (edtUserName.getText().toString().trim().isEmpty()
                || edtPassword.getText().toString().trim().isEmpty()
                || edtRePassword.getText().toString().trim().isEmpty()) {
            return true;
        }

        return false;
    }

    private void findViews() {
        edtUserName = findViewById(R.id.edt_signup_userName);
        edtPassword = findViewById(R.id.edt_signup_password);
        edtRePassword = findViewById(R.id.edt_signup_rePassword);

        btnSend = findViewById(R.id.btn_signup_send);
    }

    @Override
    protected void onStop() {
        super.onStop();
        edtUserName.setText("");
        edtPassword.setText("");
        edtRePassword.setText("");
    }
}
