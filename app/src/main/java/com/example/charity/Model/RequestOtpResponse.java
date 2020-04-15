package com.example.charity.Model;

import com.google.gson.annotations.SerializedName;

public class RequestOtpResponse {

    @SerializedName("code")
    private int responseCode;

    @SerializedName("has_error")
    private boolean hasError;

    @SerializedName("phoneNumber")
    private String phoneNumber;

    public RequestOtpResponse(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public int getResponseCode() {
        return responseCode;
    }

    public boolean isHasError() {
        return hasError;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }
}
