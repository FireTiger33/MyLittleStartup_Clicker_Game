package com.example.mylittlestartup.data.api;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface UserApi {
    class UserPlain {
        public int id;
        public String username;
        public String score;
    }

    class User {
        public String username;
        public String password;

        public User(String username, String password) {
            this.username = username;
            this.password = password;
        }
    }

    @GET("/user")
    Call<List<UserPlain>> getAll();

    @GET("/user/{id}")
    Call<UserPlain> get(@Path("id") int id);

    @POST("/user")
    Call<Void> register(@Body User user);
}
