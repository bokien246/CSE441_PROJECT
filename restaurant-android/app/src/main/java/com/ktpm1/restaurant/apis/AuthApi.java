package com.ktpm1.restaurant.apis;

import com.ktpm1.restaurant.dtos.requests.LoginForm;
import com.ktpm1.restaurant.dtos.requests.RegisterRequest;
import com.ktpm1.restaurant.dtos.responses.LoginResponse;
import com.ktpm1.restaurant.dtos.responses.ResponseMessage;
import com.ktpm1.restaurant.models.User;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Query;

public interface AuthApi {
    @POST("/auth/login")
    Call<LoginResponse> login(@Body LoginForm loginForm);

    @POST("auth/signup")
    Call<ResponseMessage> signup(@Body RegisterRequest registerRequest);

    @GET("/auth/get/profile")
    Call<User> getProfile(@Header("Authorization") String token);

    @PUT("/auth/update-verify-code")
    Call<ResponseMessage> updateVerifyCode(@Query("username") String username,
                                           @Query("code") String code);

    @GET("/auth/verify")
    Call<ResponseMessage> verify(@Query("code") String code);
}