package dataAccessTests;

import chess.*;
import com.google.gson.Gson;
import dataAccess.GameDataInterface;
import dataAccess.MemoryGameDOA;
import dataAccess.SqlGameDOA;
import model.GameData;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class GameDataAccessTests {

    private GameDataInterface gameDataAccess;

    @BeforeEach
    public void setup(){
        this.gameDataAccess = new MemoryGameDOA();

        gameDataAccess.clear();
    }

    @AfterEach
    public void takedown(){
        gameDataAccess.clear();
    }

    @Test
    public void addGameTest(){
        ChessGame gameSetup = new ChessGame();

        GameData newGame = new GameData(1, null, null, null, gameSetup);

        gameDataAccess.createGame(newGame);

        assertNotNull(gameDataAccess.getGame(1));
    }

    @Test
    public void addGameTwice(){
        ChessGame gameSetup = new ChessGame();

        GameData newGame = new GameData(1, null, null, "right", gameSetup);
        GameData newGame2 = new GameData(1, null, null, "wrong", gameSetup);

        gameDataAccess.createGame(newGame);

        assertTrue(gameDataAccess.getGame(1).gameName() == "right");
        assertFalse(gameDataAccess.getGame(1).gameName() == "wrong");
    }

    @Test
    public void getGameTest(){
        ChessGame gameSetup = new ChessGame();

        GameData newGame = new GameData(1, null, null, null, gameSetup);

        gameDataAccess.createGame(newGame);

        assertNotNull(gameDataAccess.getGame(1));
    }

    @Test
    public void wrongID(){
        ChessGame gameSetup = new ChessGame();

        GameData newGame = new GameData(1, null, null, null, gameSetup);

        gameDataAccess.createGame(newGame);

        assertNull(gameDataAccess.getGame(2));
    }

    @Test
    public void listGameTest(){
        ChessGame gameSetup = new ChessGame();

        GameData newGame = new GameData(1, null, null, null, gameSetup);
        GameData newGame2 = new GameData(2, null, null, null, gameSetup);
        GameData newGame3 = new GameData(3, null, null, null, gameSetup);

        gameDataAccess.createGame(newGame);
        gameDataAccess.createGame(newGame2);
        gameDataAccess.createGame(newGame3);

        assertTrue(gameDataAccess.listGames().size() == 3);
    }

    @Test
    public void listGameTwice(){
        ChessGame gameSetup = new ChessGame();

        GameData newGame = new GameData(1, null, null, null, gameSetup);
        GameData newGame2 = new GameData(2, null, null, null, gameSetup);
        GameData newGame3 = new GameData(2, null, null, null, gameSetup);

        gameDataAccess.createGame(newGame);
        gameDataAccess.createGame(newGame2);
        gameDataAccess.createGame(newGame3);

        assertFalse(gameDataAccess.listGames().size() == 3);
    }

    @Test
    public void whiteUpdateTest(){
        ChessGame gameSetup = new ChessGame();

        GameData newGame = new GameData(1, null, null, null, gameSetup);

        gameDataAccess.createGame(newGame);
        gameDataAccess.whiteUpdateGame(1, "Phillip");

        assertTrue(gameDataAccess.getGame(1).whiteUsername() == "Phillip");
    }

    @Test
    public void whiteUpdateWrong(){
        ChessGame gameSetup = new ChessGame();

        GameData newGame = new GameData(1, null, null, null, gameSetup);

        gameDataAccess.createGame(newGame);
        gameDataAccess.whiteUpdateGame(2, "Phillip");

        assertFalse(gameDataAccess.getGame(1).whiteUsername() == "Phillip");
    }

    @Test
    public void blackUpdateTest(){
        ChessGame gameSetup = new ChessGame();

        GameData newGame = new GameData(1, null, null, null, gameSetup);

        gameDataAccess.createGame(newGame);
        gameDataAccess.blackUpdateGame(1, "Phillip");

        assertTrue(gameDataAccess.getGame(1).blackUsername() == "Phillip");
    }

    @Test
    public void blackUpdateWrong(){
        ChessGame gameSetup = new ChessGame();

        GameData newGame = new GameData(1, null, null, null, gameSetup);

        gameDataAccess.createGame(newGame);
        gameDataAccess.blackUpdateGame(2, "Phillip");

        assertFalse(gameDataAccess.getGame(1).blackUsername() == "Phillip");
    }

    @Test
    public void clearTest(){
        ChessGame gameSetup = new ChessGame();

        GameData newGame = new GameData(1, null, null, null, gameSetup);

        gameDataAccess.createGame(newGame);
        assertNotNull(gameDataAccess.getGame(1));

        gameDataAccess.clear();
        assertNull(gameDataAccess.getGame(1));
    }

    @Test
    public void updateTest(){
        ChessGame gameSetup = new ChessGame();
        ChessGame newGameSetup = new ChessGame();


        GameData newGame = new GameData(1, null, null, null, gameSetup);


        gameDataAccess.createGame(newGame);
        newGameSetup.setTeamTurn(ChessGame.TeamColor.BLACK);
        gameDataAccess.updateGame(1, newGameSetup);




        assertEquals(gameDataAccess.getGame(1).game().getTeamTurn(), ChessGame.TeamColor.BLACK);
    }


    @Test
    public void updateWrong(){
        ChessGame gameSetup = new ChessGame();
        ChessGame newGameSetup = new ChessGame();


        GameData newGame = new GameData(1, null, null, null, gameSetup);


        gameDataAccess.createGame(newGame);
        newGameSetup.setTeamTurn(ChessGame.TeamColor.BLACK);
        gameDataAccess.updateGame(2, newGameSetup);


        assertNotEquals(gameDataAccess.getGame(1).game().getTeamTurn(), ChessGame.TeamColor.BLACK);
    }

}
