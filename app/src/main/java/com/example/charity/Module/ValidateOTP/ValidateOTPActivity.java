package com.example.charity.Module.ValidateOTP;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.example.charity.Application.Network.APIClient;
import com.example.charity.Application.Network.APIInterface;
import com.example.charity.Model.ValidateOtpResponse;
import com.example.charity.Application.MyActivity;
import com.example.charity.Module.Signup.SignupActivity;
import com.example.charity.Utility.Constants;
import com.example.charity.R;
import com.example.charity.Utility.Utilities;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ValidateOTPActivity extends MyActivity {

    private EditText edtOtp;
    private Button btnSend;

    private APIInterface apiInterface;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_validate_otp);

        ValidateOTPActivity.this.setTitle(getResources().getString(R.string.validate_screen_title));

        apiInterface = APIClient.getRetrofitInstance().create(APIInterface.class);

        findViews();

        btnSend.setOnClickListener(v -> {
            if (!edtOtp.getText().toString().trim().isEmpty()) {
                if (!Utilities.isConnected(getApplicationContext())) {
                    Toast.makeText(getApplicationContext(),
                            getResources().getString(R.string.toast_no_internet_before_acttion),
                            Toast.LENGTH_SHORT).show();
                } else {
                    sendOtp();
                }

            } else {
                Toast.makeText(this,
                        getResources().getString(R.string.error_enter_code),
                        Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void sendOtp() {
        ValidateOtpResponse otp = new ValidateOtpResponse(
                Constants.PHONE_NUMBER,
                edtOtp.getText().toString().trim());

        Call<ValidateOtpResponse> call = apiInterface.validateOtp(otp);
        call.enqueue(new Callback<ValidateOtpResponse>() {
            @Override
            public void onResponse(Call<ValidateOtpResponse> call, Response<ValidateOtpResponse> response) {
                ValidateOtpResponse otpResponse = response.body();

                if (otpResponse != null) {
                    if (otpResponse.getResponseCode() == 100) {
                        startActivity(new Intent(ValidateOTPActivity.this,
                                SignupActivity.class));

                    } else if (otpResponse.getResponseCode() == 1) {
                        Toast.makeText(getApplicationContext(),
                                getResources().getString(R.string.error_code_1),
                                Toast.LENGTH_SHORT).show();
                    }

                } else {
                    Toast.makeText(getApplicationContext(), "null response", Toast.LENGTH_SHORT)
                            .show();
                }

            }

            @Override
            public void onFailure(Call<ValidateOtpResponse> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "onFailure called ", Toast.LENGTH_SHORT).show();
                call.cancel();
            }
        });
    }

    private void findViews() {
        edtOtp = findViewById(R.id.edt_validateOtp_otp);
        btnSend = findViewById(R.id.btn_validateOtp_send);
    }

    protected void onStop() {
        super.onStop();
        edtOtp.setText("");
    }

}
