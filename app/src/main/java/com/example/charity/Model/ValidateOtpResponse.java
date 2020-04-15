package com.example.charity.Model;

import com.google.gson.annotations.SerializedName;

public class ValidateOtpResponse {

    @SerializedName("phoneNumber")
    private String phoneNumber;

    @SerializedName("otp")
    private String otp;

    @SerializedName("code")
    private int responseCode;

    @SerializedName("has_error")
    private boolean hasError;

    public ValidateOtpResponse(String phoneNumber, String otp) {
        this.phoneNumber = phoneNumber;
        this.otp = otp;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getOtp() {
        return otp;
    }

    public int getResponseCode() {
        return responseCode;
    }

    public boolean isHasError() {
        return hasError;
    }
}
