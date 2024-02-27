package service;

import dataAccess.memoryDB;
import dataAccess.*;
import model.AuthData;

public class authService {

    private final userDOA userDataAccess;
    private final gameDOA gameDataAccess;
    private final authDOA authDataAccess;

    public authService(){
        this.userDataAccess = new userDOA();
        this.gameDataAccess = new gameDOA();
        this.authDataAccess = new authDOA();
    }
    public void clearApplication(memoryDB data){
        userDataAccess.clear(data);
        gameDataAccess.clear(data);
        authDataAccess.clear(data);
    }
}
