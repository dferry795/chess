package service;

import dataAccess.*;

public class AuthService {

    private final MemoryUserDOA userDataAccess;
    private final MemoryGameDOA gameDataAccess;
    private final MemoryAuthDOA authDataAccess;

    public AuthService(MemoryUserDOA user, MemoryGameDOA game, MemoryAuthDOA auth){
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
