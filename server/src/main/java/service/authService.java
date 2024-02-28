package service;

import dataAccess.*;

public class authService {

    private final userDOA userDataAccess;
    private final gameDOA gameDataAccess;
    private final authDOA authDataAccess;

    public authService(userDOA user, gameDOA game, authDOA auth){
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
