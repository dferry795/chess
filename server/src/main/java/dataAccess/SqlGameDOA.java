package dataAccess;

import chess.ChessGame;
import com.google.gson.Gson;
import model.GameData;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;

import static dataAccess.DatabaseManager.createDatabase;
import static dataAccess.DatabaseManager.getConnection;

public class SqlGameDOA implements GameDataInterface{
    public SqlGameDOA(){
        configure();
    }
    public ArrayList<GameData> listGames(){
        ArrayList<GameData> games = new ArrayList<>();

        try (var con = DatabaseManager.getConnection()){
            var statement = "SELECT gameID, whiteUsername, blackUsername, gameName, gameState FROM game";
            try (var ps = con.prepareStatement(statement)){
                try (var rs = ps.executeQuery()){
                    while (rs.next()){
                        var gameStateJson = rs.getString("gameState");
                        ChessGame gameState = new Gson().fromJson(gameStateJson, ChessGame.class);
                        games.add(new GameData(rs.getInt("gameID"), rs.getString("whiteUsername"), rs.getString("blackUsername"), rs.getString("gameName"), gameState));
                    }
                }
            }
        } catch (SQLException | DataAccessException e) {
            throw new RuntimeException(e);
        }
        return games;
    }

    public void createGame(GameData game){
        try (var con = DatabaseManager.getConnection()){
            var statement = "INSERT INTO game (gameID, whiteUsername, blackUsername, gameName, gameState) VALUES(?, ?, ?, ?, ?)";
            try (var ps = con.prepareStatement(statement)){
                ps.setInt(1, game.gameID());
                ps.setString(2, game.whiteUsername());
                ps.setString(3, game.blackUsername());
                ps.setString(4, game.gameName());

                var gameStateJson = new Gson().toJson(game.game(), ChessGame.class);
                ps.setString(5, gameStateJson);

                ps.executeUpdate();
            }
        } catch (SQLException | DataAccessException e) {
            throw new RuntimeException(e);
        }
    }

    public GameData getGame(int gameID){
        try (var con = DatabaseManager.getConnection()){
            var statement = "SELECT gameID, whiteUsername, blackUsername, gameName, gameState FROM game Where gameID=?";
            try (var ps = con.prepareStatement(statement)){
                ps.setInt(1, gameID);

                try (var rs = ps.executeQuery()){
                    if (rs.next()){
                        var gameStateJson = rs.getString("gameState");
                        ChessGame gameState = new Gson().fromJson(gameStateJson, ChessGame.class);
                        return new GameData(rs.getInt("gameID"), rs.getString("whiteUsername"), rs.getString("blackUsername"), rs.getString("gameName"), gameState);
                    }
                }
            }
        } catch (SQLException | DataAccessException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    public void whiteUpdateGame(int gameID, String username){
        try (var con = DatabaseManager.getConnection()){
            var statement = "UPDATE game SET whiteUsername=? WHERE gameID=?";
            try (var ps = con.prepareStatement(statement)){
                ps.setString(1, username);
                ps.setInt(2, gameID);

                ps.executeUpdate();
            }
        } catch (SQLException | DataAccessException e) {
            throw new RuntimeException(e);
        }
    }

    public void blackUpdateGame(int gameID, String username){
        try (var con = DatabaseManager.getConnection()){
            var statement = "UPDATE game SET blackUsername=? WHERE gameID=?";
            try (var ps = con.prepareStatement(statement)){
                ps.setString(1, username);
                ps.setInt(2, gameID);

                ps.executeUpdate();
            }
        } catch (SQLException | DataAccessException e) {
            throw new RuntimeException(e);
        }
    }

    public void updateGame(int gameID, ChessGame updatedGame){
        try (var con = DatabaseManager.getConnection()){
            var statement = "UPDATE game SET gameState=? WHERE gameID=?";
            try (var ps = con.prepareStatement(statement)){
                var updatedGameJson = new Gson().toJson(updatedGame, ChessGame.class);
                ps.setString(1, updatedGameJson);
                ps.setInt(2, gameID);

                ps.executeUpdate();
            }
        } catch (SQLException | DataAccessException e) {
            throw new RuntimeException(e);
        }
    }

    public void clear() {
        try (var con = getConnection()) {
            var statement = "TRUNCATE game";
            var clearTableStatement = con.prepareStatement(statement);
            clearTableStatement.executeUpdate();
        } catch (SQLException | DataAccessException e) {
            throw new RuntimeException(e);
        }
    }

    private void configure(){
        try (var con = DatabaseManager.getConnection()){
            createDatabase();


            con.setCatalog("chess");


            var createGameTable = """
           CREATE TABLE  IF NOT EXISTS game (
               gameID int NOT NULL,
               whiteUsername varchar(255) DEFAULT NULL,
               blackUsername varchar(255) DEFAULT NULL,
               gameName varchar(255) DEFAULT NULL,
               gameState longtext NOT NULL
           )""";


            try (var createTableStatement = con.prepareStatement(createGameTable)) {
                createTableStatement.executeUpdate();
            }
        } catch (SQLException | DataAccessException e) {
            throw new RuntimeException(e);
        }
    }
}
