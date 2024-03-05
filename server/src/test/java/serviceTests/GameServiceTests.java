
package serviceTests;

import chess.ChessGame;
import dataAccess.DataAccessException;
import dataAccess.MemoryAuthDOA;
import dataAccess.MemoryGameDOA;
import model.AuthData;
import model.GameData;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import service.GameService;

import java.util.ArrayList;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class GameServiceTests {



    private GameService gameService;
    private MemoryAuthDOA authDataAccess;
    private MemoryGameDOA gameDataAccess;
    private AuthData auth1;
    private AuthData auth2;

    @BeforeEach
    public void setUp() {
        this.authDataAccess = new MemoryAuthDOA();
        this.gameDataAccess = new MemoryGameDOA();
        gameService = new GameService(authDataAccess, gameDataAccess);

        String authToken1 = UUID.randomUUID().toString();
        String authToken2 = UUID.randomUUID().toString();
        this.auth1 = new AuthData(authToken1, "Darian");
        this.auth2 = new AuthData(authToken2, "Dracen");

        authDataAccess.createAuth(auth1);
    }

    @AfterEach
    public void takeDown() {
        this.authDataAccess.clear();
        this.gameDataAccess.clear();
    }

    @Test
    public void testListGames() throws DataAccessException{
        int id = Math.abs(UUID.randomUUID().hashCode());
        GameData newGame = new GameData(id, null, null, null, new ChessGame());
        gameDataAccess.createGame(newGame);
        ArrayList<GameData> testList = new ArrayList<>();
        testList.add(newGame);

        assertNotNull(gameService.listGames(auth1.authToken()), "Did not return anything");
        assertIterableEquals(testList, gameService.listGames(auth1.authToken()), "You result did not match");
    }

    @Test
    public void unauthListGames() throws DataAccessException{
        int id = Math.abs(UUID.randomUUID().hashCode());
        GameData newGame = new GameData(id, null, null, null, new ChessGame());
        gameDataAccess.createGame(newGame);

        try {
            gameService.listGames(auth2.authToken());
        } catch (DataAccessException ex) {
            assertTrue(ex.getMessage().toLowerCase().contains("error"));
        }
    }

    @Test
    public void testCreateGame() throws DataAccessException {
        int id = Math.abs(UUID.randomUUID().hashCode());
        GameData newGame = new GameData(id, null, null, "Let's Gooooo!", new ChessGame());

        ArrayList<GameData> testList = new ArrayList<>();
        testList.add(newGame);

        int gameID = gameService.createGame(newGame.gameName(), auth1.authToken());

        assertTrue(gameID > 0, "Invalid number");
        assertTrue(gameService.listGames(auth1.authToken()).size() == 1, "Did not create game");
    }

    @Test
    public void unauthorizedCreateGame() throws DataAccessException {
        try{
            gameService.createGame("wow", auth2.authToken());
        } catch (DataAccessException ex) {
            assertTrue(ex.getMessage().toLowerCase().contains("error"));
        }
    }

    @Test
    public void testJoinGame() throws DataAccessException {
        int id = Math.abs(UUID.randomUUID().hashCode());
        GameData newGame = new GameData(id, null, null, null, new ChessGame());
        gameDataAccess.createGame(newGame);
        gameService.joinGame("BLACK", id, auth1.authToken());


        GameData testGame = new GameData(id, null, "Darian", null, new ChessGame());
        ArrayList<GameData> testList = new ArrayList<>();
        testList.add(testGame);

        assertEquals(testList, gameDataAccess.listGames());
    }

    @Test
    public void badColorJoinGame() throws DataAccessException {
        authDataAccess.createAuth(auth2);

        int id = Math.abs(UUID.randomUUID().hashCode());
        GameData newGame = new GameData(id, null, null, null, new ChessGame());

        gameDataAccess.createGame(newGame);

        GameData testGame = new GameData(id, "Dracen", null, null, new ChessGame());
        ArrayList<GameData> testList = new ArrayList<>();
        testList.add(testGame);

        try {
            gameService.joinGame("BLACK", id, auth2.authToken());

            assertNotEquals(testList, gameDataAccess.listGames());
        } catch (DataAccessException ex){
            assertTrue(ex.getMessage().toLowerCase().contains("error"));
        }
    }
}