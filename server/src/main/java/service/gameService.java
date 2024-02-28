package service;

import chess.ChessGame;
import dataAccess.*;
import model.GameData;

import java.util.ArrayList;
import java.util.UUID;

public class gameService {

    private final authDOA authDataAccess;
    private final gameDOA gameDataAccess;

    public gameService(authDOA auth, gameDOA game) {
        this.authDataAccess = auth;
        this.gameDataAccess = game;
    }

    public ArrayList<GameData> listGames(String authToken) throws DataAccessException {
        if (authToken != null && authDataAccess.getAuth(authToken) != null) {
            return gameDataAccess.listGames();
        } else {
            throw new DataAccessException("Error: unauthorized");
        }
    }

    public int createGame(String name, String authToken) throws DataAccessException {
        if (authToken != null && authDataAccess.getAuth(authToken) != null) {
            int gameID = Math.abs(UUID.randomUUID().hashCode());
            ChessGame new_game = new ChessGame();
            GameData game = new GameData(gameID, null, null, name, new_game);
            gameDataAccess.createGame(game);
            return gameID;
        } else {
            throw new DataAccessException("Error: unauthorized");
        }
    }

    public void joinGame(String color, int gameID, String authToken) throws DataAccessException {
            if (authToken != null && authDataAccess.getAuth(authToken) != null) {
                if (gameDataAccess.getGame(gameID) != null) {
                    String username = authDataAccess.getAuth(authToken).username();
                    gameDataAccess.updateGame(gameID, color, username);
                } else {
                    throw new DataAccessException("Error: bad request");
                }
            } else {
                throw new DataAccessException("Error: unauthorized");
            }
    }
}