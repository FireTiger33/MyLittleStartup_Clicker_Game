package com.example.mylittlestartup.data;

public interface UserRepository {

    void authorize(String login,
                   String pass,
                   BaseCallback authorizeCallback);

    void register(String login,
                  String pass,
                  BaseCallback authorizeCallback);

    void checkAuthorize(BaseCallback authorizeCallback);
}
