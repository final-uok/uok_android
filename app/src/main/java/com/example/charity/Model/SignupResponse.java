package com.example.charity.Model;

import com.google.gson.annotations.SerializedName;

public class SignupResponse {

    @SerializedName("phoneNumber")
    private String phoneNumber;

    @SerializedName("userName")
    private String userName;

    @SerializedName("password")
    private String password;

    @SerializedName("code")
    private int responseCode;

    @SerializedName("has_error")
    private boolean hasError;

    @SerializedName("userType")
    private String userType;

    @SerializedName("clientId")
    private String clientId;

    public SignupResponse(String userName, String password, String phoneNumber, String userType) {
        this.userName = userName;
        this.password = password;
        this.phoneNumber = phoneNumber;
        this.userType = userType;
    }

    public String getUserName() {
        return userName;
    }

    public String getPassword() {
        return password;
    }

    public int getResponseCode() {
        return responseCode;
    }

    public boolean isHasError() {
        return hasError;
    }

    public String getUserType() {
        return userType;
    }

    public String getClientId() {
        return clientId;
    }
}
