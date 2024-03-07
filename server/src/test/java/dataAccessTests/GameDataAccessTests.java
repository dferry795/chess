package dataAccessTests;

import chess.ChessGame;
import dataAccess.GameDataInterface;
import dataAccess.MemoryGameDOA;
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
}
