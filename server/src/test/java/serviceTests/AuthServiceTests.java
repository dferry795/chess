package serviceTests;

import chess.ChessGame;
import dataAccess.AuthDOA;
import dataAccess.DataAccessException;
import dataAccess.GameDOA;
import dataAccess.UserDOA;
import model.AuthData;
import model.GameData;
import model.UserData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import service.AuthService;
import service.UserService;

import static org.junit.jupiter.api.Assertions.*;

public class AuthServiceTests {

    private UserDOA userDataAccess;
    private AuthDOA authDataAccess;
    private GameDOA gameDataAccess;
    private AuthService authService;
    private UserService userService;

    @BeforeEach
    public void setup(){
        this.userDataAccess = new UserDOA();
        this.authDataAccess = new AuthDOA();
        this.gameDataAccess = new GameDOA();
        authService = new AuthService(userDataAccess, gameDataAccess, authDataAccess);
        userService = new UserService(userDataAccess, authDataAccess);

        UserData user = new UserData("person", "password", "idk@gmail.com");
        userDataAccess.createUser(user);
        userDataAccess.createUser(user);
        userDataAccess.createUser(user);

        AuthData auth = new AuthData("token", "person");
        authDataAccess.createAuth(auth);
        authDataAccess.createAuth(auth);
        authDataAccess.createAuth(auth);

        GameData game = new GameData(1, null, null, null, new ChessGame());
        gameDataAccess.createGame(game);
        gameDataAccess.createGame(game);
        gameDataAccess.createGame(game);
    }

    @Test
    public void testClear(){
        authService.clearApplication();

        assertNull(userDataAccess.getUser("person", "password"));
        assertNull(authDataAccess.getAuth("token"));
        assertNull(gameDataAccess.getGame(1));
    }

    @Test
    public void loginAfterClear() throws DataAccessException {
        authService.clearApplication();

        try{
            userService.login("person", "password");
        } catch (DataAccessException ex){
            assertTrue(ex.getMessage().toLowerCase().contains("error"));
        }
    }
}
