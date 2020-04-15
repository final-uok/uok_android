package com.example.charity.Model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Food {

    @SerializedName("has_error")
    private boolean hasError;

    @SerializedName("code")
    private int code;

    @SerializedName("foodList")
    private List<FoodDetail> foodDetails;

    @SerializedName("clientId")
    private String clientId;

    @SerializedName("foodId")
    private String foodId;

    public Food(String clientId, List<FoodDetail> foodDetails) {
        this.clientId = clientId;
        this.foodDetails = foodDetails;
    }

    public Food(String foodId) {
        this.foodId = foodId;
    }

    public boolean isHasError() {
        return hasError;
    }

    public int getCode() {
        return code;
    }

    public List<FoodDetail> getFoodDetails() {
        return foodDetails;
    }
}
