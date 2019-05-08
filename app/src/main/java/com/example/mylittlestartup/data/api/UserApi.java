package com.example.mylittlestartup.data.api;

import java.util.Comparator;
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

        public String getUsername() {
            return username;
        }

        public String getScore() {
            return score;
        }

        public UserPlain(int id, String username, String score) {
            this.id = id;
            this.username = username;
            this.score = score;
        }
    }

    class SortByScore implements Comparator<UserPlain> {
        @Override
        public int compare(UserPlain o1, UserPlain o2) {

            int score1 = Integer.parseInt(o1.score);
            int score2 = Integer.parseInt(o2.score);

            return score2 - score1;
        }
    }

    class User {
        public String username;
        public String password;

        public User(String username, String password) {
            this.username = username;
            this.password = password;
        }
    }

    @GET("/user?limit=99&offset=0")
    Call<List<UserPlain>> getAll();

    @GET("/user/{id}")
    Call<UserPlain> get(@Path("id") int id);

    @POST("/user")
    Call<Void> register(@Body User user);
}
