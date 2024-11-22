package com.example.appbansach.api;

import com.example.appbansach.model.Book;
import com.example.appbansach.model.Cart;
import com.example.appbansach.model.LoginUserModel;
import com.example.appbansach.model.User;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface UserApi {

    @POST("register")
    Call<User> registerUser(
            @Body User user
    );

    @POST("login")
    Call<User> loginUser(
           @Body LoginUserModel loginUserModel
            );
}
