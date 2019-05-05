package com.example.mylittlestartup.data.api;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface GameApi {
//    class ShopPlain {
//
//    }

    class ScorePlain {
        public int score;
    }

//    class AchievementPlain {
//
//    }

    @POST("/game")
    Call<ScorePlain> sync(@Body ScorePlain scorePlain);
}
