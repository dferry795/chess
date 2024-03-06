package dataAccess;

import model.GameData;

import java.util.ArrayList;

public interface GameDataInterface {
    ArrayList<GameData> listGames();
    void createGame(GameData game);
    GameData getGame(int gameID);
    void whiteUpdateGame(int gameID, String username);
    void blackUpdateGame(int gameID, String username);
    void updateGame(int gameID, String color, String username) throws DataAccessException;
    void clear();
}
