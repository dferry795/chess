package service;

import dataAccess.*;
import model.AuthData;
import model.GameData;
import model.UserData;

import java.util.ArrayList;
import java.util.HashSet;

public class gameService {

    private final authDOA authDataAccess;
    private final userDOA userDataAccess;
    private final gameDOA gameDataAccess;

    public gameService(){
        this.authDataAccess = new authDOA();
        this.userDataAccess = new userDOA();
        this.gameDataAccess = new gameDOA();
    }
    public HashSet<GameData> listGames(String auth, memoryDB data){
        if (authDataAccess.getAuth(auth, data) != null){
            return gameDataAccess.listGames(data);
        }
    }

    public String createGame(String name, String authToken, memoryDB data){
        if (authDataAccess.getAuth(authToken, data) != null){

        }
    }

    public void joinGame(String color, String gameID){

    }
}
