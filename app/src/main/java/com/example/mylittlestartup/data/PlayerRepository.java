package com.example.mylittlestartup.data;

import com.example.mylittlestartup.game.GameContract;

public interface PlayerRepository extends GameContract.Repository {

    void setLoggedIn(BaseCallback callback);

    void setNotLoggedIn(BaseCallback callback);

    void isLoggedIn(BaseCallback callback);

}
