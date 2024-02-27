package dataAccess;

import model.GameData;

import java.util.HashSet;

public class gameDOA {
    public HashSet<GameData> listGames(memoryDB data){
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

    public void updateGame(int gameID, String color, String username, memoryDB data){
        for (GameData game: data.gameList){
            if (game.gameID() == gameID){
                GameData gameInfo = game;
                data.gameList.remove(game);
                if (color == "WHITE") {
                    GameData new_game = new GameData(gameInfo.gameID(), username, gameInfo.blackUsername(), gameInfo.gameName(), gameInfo.game());
                    data.gameList.add(new_game);
                } else if (color == "BLACK") {
                    GameData new_game = new GameData(gameInfo.gameID(), gameInfo.whiteUsername(), username, gameInfo.gameName(), gameInfo.game());
                    data.gameList.add(new_game);
                }
            }
        }
    }

    public void clear(memoryDB data){
        data.gameList.clear();
    }
}
