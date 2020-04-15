package com.example.charity.Model;

import com.google.gson.annotations.SerializedName;

public class Polling {

    @SerializedName("has_error")
    private boolean hasError;

    @SerializedName("code")
    private int responseCode;

    @SerializedName("foodCount")
    private int foodCount;

    @SerializedName("charityId")
    private String charityId;

    @SerializedName("foodId")
    private String foodId;

    public Polling(String charityId) {
        this.charityId = charityId;
    }

    public boolean isHasError() {
        return hasError;
    }

    public int getResponseCode() {
        return responseCode;
    }

    public int getFoodCount() {
        return foodCount;
    }

    public String getCharityId() {
        return charityId;
    }

    public String getFoodId() {
        return foodId;
    }
}
