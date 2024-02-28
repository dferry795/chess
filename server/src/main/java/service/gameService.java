package service;

import chess.ChessGame;
import dataAccess.*;
import model.GameData;

import java.util.ArrayList;
import java.util.UUID;

public class gameService {

    private final authDOA authDataAccess;
    private final gameDOA gameDataAccess;

    public gameService() {
        this.authDataAccess = new authDOA();
        this.gameDataAccess = new gameDOA();
    }

    public ArrayList<GameData> listGames(String authToken, memoryDB data) throws DataAccessException {
        if (authToken != null && authDataAccess.getAuth(authToken, data) != null) {
            return gameDataAccess.listGames(data);
        } else {
            throw new DataAccessException("Error: unauthorized");
        }
    }

    public int createGame(String name, String authToken, memoryDB data) throws DataAccessException {
        if (authToken != null && authDataAccess.getAuth(authToken, data) != null) {
            int gameID = Math.abs(UUID.randomUUID().hashCode());
            ChessGame new_game = new ChessGame();
            GameData game = new GameData(gameID, null, null, name, new_game);
            gameDataAccess.createGame(game, data);
            return gameID;
        } else {
            throw new DataAccessException("Error: unauthorized");
        }
    }

    public void joinGame(String color, int gameID, String authToken, memoryDB data) throws DataAccessException {
            if (authToken != null && authDataAccess.getAuth(authToken, data) != null) {
                if (gameDataAccess.getGame(gameID, data) != null) {
                    String username = authDataAccess.getAuth(authToken, data).username();
                    gameDataAccess.updateGame(gameID, color, username, data);
                } else {
                    throw new DataAccessException("Error: bad request");
                }
            } else {
                throw new DataAccessException("Error: unauthorized");
            }
    }
}