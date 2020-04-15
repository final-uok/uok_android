package com.example.charity.Model;

import com.google.gson.annotations.SerializedName;

public class BaseResponse {

    @SerializedName("has_error")
    private boolean hasError;

    @SerializedName("code")
    private int code;

    public boolean isHasError() {
        return hasError;
    }

    public int getCode() {
        return code;
    }
}