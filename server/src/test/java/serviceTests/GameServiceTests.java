/*
package serviceTests;

import chess.ChessGame;
import dataAccess.DataAccessException;
import dataAccess.authDOA;
import dataAccess.gameDOA;
import model.AuthData;
import model.GameData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import service.GameService;

import java.util.ArrayList;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class GameServiceTests {



    private GameService gameService;
    private authDOA authDataAccess;
    private gameDOA gameDataAccess;
    private AuthData auth1;
    private AuthData auth2;

    @BeforeEach
    public void setUp() {
        this.authDataAccess = new authDOA();
        this.gameDataAccess = new gameDOA();
        gameService = new GameService(authDataAccess, gameDataAccess);

        String authToken1 = UUID.randomUUID().toString();
        String authToken2 = UUID.randomUUID().toString();
        this.auth1 = new AuthData(authToken1, "Darian");
        this.auth2 = new AuthData(authToken2, "Dracen");

        authDataAccess.createAuth(auth1);
    }

    @Test
    public void testListGames() throws DataAccessException{
        int id = Math.abs(UUID.randomUUID().hashCode());
        GameData newGame = new GameData(id, null, null, null, new ChessGame());
        gameDataAccess.createGame(newGame);
        ArrayList<GameData> testList = new ArrayList<>();
        testList.add(newGame);

        assertNotNull(gameService.listGames(auth1.authToken()), "Did not return anything");
        assertEquals(testList, gameService.listGames(auth1.authToken()), "You result did not match");
    }

    @Test
    public void unauthListGames() throws DataAccessException{
        int id = Math.abs(UUID.randomUUID().hashCode());
        GameData newGame = new GameData(id, null, null, null, new ChessGame());
        gameDataAccess.createGame(newGame);

        assertInstanceOf(DataAccessException.class, gameService.listGames(auth2.authToken()));
    }

    @Test
    public void testCreateGame() throws DataAccessException {
        int id = Math.abs(UUID.randomUUID().hashCode());
        GameData newGame = new GameData(id, null, null, "Let's Gooooo!", new ChessGame());
        gameDataAccess.createGame(newGame);

        ArrayList<GameData> testList = new ArrayList<>();
        testList.add(newGame);

        int gameID = gameService.createGame(newGame.gameName(), auth1.authToken());

        assertTrue(gameID > 0, "Invalid number");
        assertEquals(testList, gameService.listGames(auth1.authToken()));
    }

    @Test
    public void unauthorizedCreateGame() throws DataAccessException {
        assertInstanceOf(DataAccessException.class, gameService.createGame("wow", auth2.authToken()));
    }

    @Test
    public void testJoinGame() throws DataAccessException {
        int id = Math.abs(UUID.randomUUID().hashCode());
        GameData newGame = new GameData(id, null, null, null, new ChessGame());
        gameService.joinGame("BLACK", id, auth1.authToken());
        gameDataAccess.createGame(newGame);

        GameData testGame = new GameData(id, null, "Darian", null, new ChessGame());
        ArrayList<GameData> testList = new ArrayList<>();
        testList.add(testGame);

        assertEquals(testList, gameDataAccess.listGames());
    }

    @Test
    public void unauthorizedJoinGame() throws DataAccessException {
        int id = Math.abs(UUID.randomUUID().hashCode());
        GameData newGame = new GameData(id, null, null, null, new ChessGame());
        gameService.joinGame("WHITE", id, auth2.authToken());
        gameDataAccess.createGame(newGame);

        GameData testGame = new GameData(id, "Dracen", null, null, new ChessGame());
        ArrayList<GameData> testList = new ArrayList<>();
        testList.add(testGame);

        assertNotEquals(testList, gameDataAccess.listGames());
    }
}
*/