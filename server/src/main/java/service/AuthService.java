package service;

import dataAccess.*;

public class AuthService {

    private final UserDOA userDataAccess;
    private final GameDOA gameDataAccess;
    private final AuthDOA authDataAccess;

    public AuthService(UserDOA user, GameDOA game, AuthDOA auth){
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
