package com.example.charity.Model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Home {

    @SerializedName("has_error")
    private boolean hasError;

    @SerializedName("code")
    private int responseCode;

    @SerializedName("count")
    private int count;

    @SerializedName("charities")
    private List<Charity> charities;

    @SerializedName("clientId")
    private String clientId;

    @SerializedName("userType")
    private String userType;

    public Home() {
    }

    public Home(String clientId, String userType) {
        this.clientId = clientId;
        this.userType = userType;
    }

    public boolean isHasError() {
        return hasError;
    }

    public int getResponseCode() {
        return responseCode;
    }

    public int getCount() {
        return count;
    }

    public List<Charity> getCharities() {
        return charities;
    }

    public String getClientId() {
        return clientId;
    }

    public String getUserType() {
        return userType;
    }
}
