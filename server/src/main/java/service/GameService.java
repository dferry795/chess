package service;

import chess.ChessGame;
import dataAccess.*;
import model.GameData;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.UUID;

public class GameService {

    private final AuthDataInterface authDataAccess;
    private final GameDataInterface gameDataAccess;

    public GameService(AuthDataInterface auth, GameDataInterface game) {
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

                    if ("WHITE".equals(color)) {
                        if (gameDataAccess.getGame(gameID).whiteUsername() == null) {
                            gameDataAccess.whiteUpdateGame(gameID, username);
                        } else {
                            throw new DataAccessException("Error: already taken");
                        }
                    } else if ("BLACK".equals(color)) {
                        if (gameDataAccess.getGame(gameID).blackUsername() == null) {
                            gameDataAccess.blackUpdateGame(gameID, username);
                        } else {
                            throw new DataAccessException("Error: already taken");
                        }
                    }
                } else {
                    throw new DataAccessException("Error: bad request");
                }
            } else {
                throw new DataAccessException("Error: unauthorized");
            }
    }
}