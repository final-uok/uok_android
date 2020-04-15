package com.example.charity.Model;

import com.google.gson.annotations.SerializedName;

public class Charity {

    @SerializedName("charityId")
    private String id;

    @SerializedName("phoneNumber")
    private String phoneNumber;

    @SerializedName("userName")
    private String userName;

    @SerializedName("address")
    private String address;

    @SerializedName("foodId")
    private String foodId;

    @SerializedName("createAt")
    private String createFoodTimestamp;

    @SerializedName("reserveAt")
    private String reserveFoodTimestamp;

    @SerializedName("philanthropistId")
    private String philanthropistId;

    @SerializedName("foodCode")
    private int foodCode;


    public String getId() {
        return id;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getUserName() {
        return userName;
    }

    public String getAddress() {
        return address;
    }

    public String getFoodId() {
        return foodId;
    }

    public String getPhilanthropistId() {
        return philanthropistId;
    }

    public String getCreateFoodTimestamp() {
        return createFoodTimestamp;
    }

    public String getReserveFoodTimestamp() {
        return reserveFoodTimestamp;
    }

    public int getFoodCode() {
        return foodCode;
    }
}
