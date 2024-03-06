package dataAccess;

import model.GameData;

import java.util.ArrayList;
import java.util.Objects;

public class MemoryGameDOA implements GameDataInterface{

    private final ArrayList<GameData> gameList = new ArrayList<>();

    public ArrayList<GameData> listGames(){
        return this.gameList;
    }

    public void createGame(GameData game){
        this.gameList.add(game);
    }

    public GameData getGame(int gameID){
        for (GameData game: this.gameList){
            if (game.gameID() == gameID){
                return game;
            }
        }
        return null;
    }

    public void whiteUpdateGame(int gameID, String username){
        ArrayList<GameData> tempList = new ArrayList<>(this.gameList);

        for (GameData game: tempList) {
            if (game.gameID() == gameID) {
                this.gameList.remove(game);
                GameData new_game = new GameData(game.gameID(), username, game.blackUsername(), game.gameName(), game.game());
                this.gameList.add(new_game);
            }
        }
    }

    public void blackUpdateGame(int gameID, String username){
        ArrayList<GameData> tempList = new ArrayList<>(this.gameList);

        for (GameData game: tempList) {
            if (game.gameID() == gameID) {
                this.gameList.remove(game);
                GameData new_game = new GameData(game.gameID(), game.whiteUsername(), username, game.gameName(), game.game());
                this.gameList.add(new_game);
            }
        }
    }

    public void updateGame(int gameID, String color, String username) throws DataAccessException {

        ArrayList<GameData> tempList = new ArrayList<>(this.gameList);


        for (GameData game: tempList){
            if (game.gameID() == gameID){

                if (Objects.equals(color, "WHITE")) {
                    if (game.whiteUsername() == null) {
                        this.gameList.remove(game);
                        GameData new_game = new GameData(game.gameID(), username, game.blackUsername(), game.gameName(), game.game());
                        this.gameList.add(new_game);
                    } else {
                        throw new DataAccessException("Error: already taken");
                    }
                } else if ("BLACK".equals(color)) {
                    if (game.blackUsername() == null) {
                        this.gameList.remove(game);
                        GameData new_game = new GameData(game.gameID(), game.whiteUsername(), username, game.gameName(), game.game());
                        this.gameList.add(new_game);
                    } else {
                        throw new DataAccessException("Error: already taken");
                    }
                }
            }
        }
    }

    public void clear(){
        this.gameList.clear();
    }
}
