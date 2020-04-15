package com.example.charity.Model;

import com.google.gson.annotations.SerializedName;

public class FoodDetail {

    @SerializedName("_id")
    private String id;

    @SerializedName("name")
    private String name;

    @SerializedName("count")
    private int count;

    public FoodDetail(String name, int count) {
        this.name = name;
        this.count = count;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getCount() {
        return count;
    }
}
