package com.example.mylittlestartup.data.api;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface SessionApi {
    class SessionResponse {
        public int id;
    }

    @POST("/session")
    Call<Void> login(@Body UserApi.User user);

    @GET("/session")
    Call<SessionResponse> checkLogin();

    @DELETE("/session")
    Call<Void> logout();
}
