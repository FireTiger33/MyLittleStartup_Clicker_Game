package com.example.mylittlestartup.data;

import com.example.mylittlestartup.game.GameContract;

public interface PlayerRepository extends GameContract.Repository {
    void setLoggedIn(int id, BaseCallback callback);
    void setNotLoggedIn(BaseCallback callback);
    void isLoggedIn(BaseCallback callback);

    int getUserID();
    int getK();
    int getKSpec();
    void setK(int k);
    void setKSpec(int k);

    boolean isMusicSoundState();
    void setMusicSoundStateOn();
    void setMusicSoundStateOff();

}
