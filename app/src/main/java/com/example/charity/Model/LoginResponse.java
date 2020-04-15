package com.example.charity.Model;

import com.google.gson.annotations.SerializedName;

public class LoginResponse {

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

    @SerializedName("phoneNumber")
    private String phoneNumber;

    public LoginResponse(String userName, String password, String userType) {
        this.userName = userName;
        this.password = password;
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

    public String getPhoneNumber() {
        return phoneNumber;
    }
}
