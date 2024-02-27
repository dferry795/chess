package service;

import chess.ChessGame;
import dataAccess.*;
import model.AuthData;
import model.GameData;
import model.UserData;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;

public class gameService {

    private final authDOA authDataAccess;
    private final userDOA userDataAccess;
    private final gameDOA gameDataAccess;

    public gameService(){
        this.authDataAccess = new authDOA();
        this.userDataAccess = new userDOA();
        this.gameDataAccess = new gameDOA();
    }
    public HashSet<GameData> listGames(String auth, memoryDB data) throws DataAccessException {
        if (authDataAccess.getAuth(auth, data) != null){
            return gameDataAccess.listGames(data);
        } else {
            throw new DataAccessException("Error: Description");
        }
    }

    public int createGame(String name, String authToken, memoryDB data) throws DataAccessException {
        if (authDataAccess.getAuth(authToken, data) != null){
            Random rand = new Random();
            int upperbound = 1000;
            int gameID = rand.nextInt(upperbound);
            ChessGame new_game = new ChessGame();
            GameData game = new GameData(gameID, null, null, name, new_game);
            gameDataAccess.createGame(game, data);
            return gameID;
        } else {
            throw new DataAccessException("Error: Description");
        }
    }

    public void joinGame(String color, int gameID, AuthData auth, memoryDB data) throws DataAccessException {
        if (authDataAccess.getAuth(auth.authToken(), data) != null && gameDataAccess.getGame(gameID, data) != null){
            gameDataAccess.updateGame(gameID, color, auth.username(), data);
        }
    }
}
