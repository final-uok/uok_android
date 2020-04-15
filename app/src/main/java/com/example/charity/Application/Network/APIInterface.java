package com.example.charity.Application.Network;

import com.example.charity.Model.BaseResponse;
import com.example.charity.Model.Food;
import com.example.charity.Model.Home;
import com.example.charity.Model.LoginResponse;
import com.example.charity.Model.Polling;
import com.example.charity.Model.RequestOtpResponse;
import com.example.charity.Model.ReserveFood;
import com.example.charity.Model.SignupResponse;
import com.example.charity.Model.ValidateOtpResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface APIInterface {

    @POST("/user/login")
    Call<LoginResponse> login(@Body LoginResponse login);

    @POST("/user/signUp")
    Call<SignupResponse> signup(@Body SignupResponse signup);

    @POST("/user/requestOtp")
    Call<RequestOtpResponse> requestOtp(@Body RequestOtpResponse otp);

    @POST("/user/checkOtp")
    Call<ValidateOtpResponse> validateOtp(@Body ValidateOtpResponse otp);

    @POST("/food/philanthropistHomeList")
    Call<Home> philanthropistHomeList(@Body Home home);

    @POST("/food/charityHomeList")
    Call<Home> charityHomeList(@Body Home home);

    @POST("/food/foodDetails")
    Call<Food> foodDetails(@Body Food food);

    @POST("/food/addFood")
    Call<BaseResponse> addFood(@Body Food food);

    @POST("/food/reserveFood")
    Call<ReserveFood> reserveFood(@Body ReserveFood reserveFood);

    @GET("/food/newFoodsList")
    Call<Home> newFoodsList();

    @POST("/check/charity")
    Call<Polling> checkCharityPoling(@Body Polling polling);
}
