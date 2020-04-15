package com.example.charity.Model;

import com.google.gson.annotations.SerializedName;

public class ReserveFood {

    @SerializedName("code")
    private int responseCode;

    @SerializedName("has_error")
    private boolean hasError;

    @SerializedName("foodId")
    private String foodId;

    @SerializedName("clientId")
    private String clientId;

    public ReserveFood(String foodId, String clientId) {
        this.foodId = foodId;
        this.clientId = clientId;
    }

    public int getResponseCode() {
        return responseCode;
    }

    public boolean isHasError() {
        return hasError;
    }
}
