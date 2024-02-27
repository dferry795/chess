package dataAccess;

import model.GameData;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Objects;

public class gameDOA {
    public ArrayList<GameData> listGames(memoryDB data){
        return data.gameList;
    }

    public void createGame(GameData game, memoryDB data){
        data.gameList.add(game);
    }

    public GameData getGame(int gameID, memoryDB data){
        for (GameData game: data.gameList){
            if (game.gameID() == gameID){
                return game;
            }
        }
        return null;
    }

    public void updateGame(int gameID, String color, String username, memoryDB data) throws DataAccessException {
        for (GameData game: data.gameList){
            if (game.gameID() == gameID){

                if (Objects.equals(color, "WHITE")) {
                    if (game.whiteUsername() == null) {
                        data.gameList.remove(game);
                        GameData new_game = new GameData(game.gameID(), username, game.blackUsername(), game.gameName(), game.game());
                        data.gameList.add(new_game);
                    } else {
                        throw new DataAccessException("Error: already taken");
                    }
                } else if (Objects.equals(color, "BLACK")) {
                    if (game.blackUsername() == null) {
                        data.gameList.remove(game);
                        GameData new_game = new GameData(game.gameID(), game.whiteUsername(), username, game.gameName(), game.game());
                        data.gameList.add(new_game);
                    } else {
                        throw new DataAccessException("Error: already taken");
                    }
                }
            }
        }
    }

    public void clear(memoryDB data){
        data.gameList.clear();
    }
}
