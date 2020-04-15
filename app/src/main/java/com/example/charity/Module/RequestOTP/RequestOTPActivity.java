package com.example.charity.Module.RequestOTP;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.example.charity.Application.Network.APIClient;
import com.example.charity.Application.Network.APIInterface;
import com.example.charity.Model.RequestOtpResponse;
import com.example.charity.Module.ValidateOTP.ValidateOTPActivity;
import com.example.charity.Application.MyActivity;
import com.example.charity.R;
import com.example.charity.Utility.Constants;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RequestOTPActivity extends MyActivity {

    private EditText edtPhoneNumber;
    private Button btnSend;

    private APIInterface apiInterface;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_otp);

        RequestOTPActivity.this.setTitle(getResources().getString(R.string.request_otp_screen_title));

        apiInterface = APIClient.getRetrofitInstance().create(APIInterface.class);

        edtPhoneNumber = findViewById(R.id.edt_RequestOTP_phoneNumber);
        btnSend = findViewById(R.id.btn_RequestOTP_send);

        btnSend.setOnClickListener(v -> {
            if (edtPhoneNumber.getText().toString().trim().isEmpty()) {
                Toast.makeText(
                        getApplicationContext(),
                        getResources().getString(R.string.error_enter_phone_number),
                        Toast.LENGTH_SHORT
                ).show();

            } else if (edtPhoneNumber.getText().toString().trim().length() < 11) {
                Toast.makeText(
                        getApplicationContext(),
                        getResources().getString(R.string.error_incorrect_phone_number_count),
                        Toast.LENGTH_SHORT
                ).show();

            } else {
                Constants.PHONE_NUMBER = edtPhoneNumber.getText().toString().trim();
                startActivity(new Intent(RequestOTPActivity.this, ValidateOTPActivity.class));

//                requestOtp();
            }
        });

    }

    private void requestOtp() {
        RequestOtpResponse otpResponse = new RequestOtpResponse(edtPhoneNumber.getText().toString().trim());

        Call<RequestOtpResponse> call = apiInterface.requestOtp(otpResponse);
        call.enqueue(new Callback<RequestOtpResponse>() {
            @Override
            public void onResponse(Call<RequestOtpResponse> call, Response<RequestOtpResponse> response) {
                RequestOtpResponse otp = response.body();
                if (otp != null) {
                    if (otp.getResponseCode() == 100) {
                        Intent intent = new Intent(RequestOTPActivity.this, ValidateOTPActivity.class);
                        startActivity(intent);
                        edtPhoneNumber.setText("");
                    } else {
                        Log.i("requestOtp", "not 100 response");
                    }

                } else {
                    Toast.makeText(getApplicationContext(), "null response", Toast.LENGTH_SHORT)
                            .show();
                }
            }

            @Override
            public void onFailure(Call<RequestOtpResponse> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "onFailure called ", Toast.LENGTH_SHORT).show();
                call.cancel();
            }
        });
    }

    protected void onStop() {
        super.onStop();
        edtPhoneNumber.setText("");
    }

}
