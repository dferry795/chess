package service;

import dataAccess.*;

public class AuthService {

    private final UserDataInterface userDataAccess;
    private final GameDataInterface gameDataAccess;
    private final AuthDataInterface authDataAccess;

    public AuthService(UserDataInterface user, GameDataInterface game, AuthDataInterface auth){
        this.userDataAccess = user;
        this.gameDataAccess = game;
        this.authDataAccess = auth;
    }
    public void clearApplication(){
        userDataAccess.clear();
        gameDataAccess.clear();
        authDataAccess.clear();
    }
}
