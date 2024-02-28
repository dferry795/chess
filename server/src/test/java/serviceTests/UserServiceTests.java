package serviceTests;

import dataAccess.AuthDOA;
import dataAccess.DataAccessException;
import dataAccess.UserDOA;
import model.AuthData;
import model.UserData;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import service.UserService;

import static org.junit.jupiter.api.Assertions.*;

public class UserServiceTests {

    private UserDOA userDataAccess;
    private AuthDOA authDataAccess;
    private UserService userService;

    @BeforeEach
    public void setup(){
        this.userDataAccess = new UserDOA();
        this.authDataAccess = new AuthDOA();
        userService = new UserService(userDataAccess, authDataAccess);
    }

    @AfterEach
    public void takedown(){
        this.userDataAccess.clear();
        this.authDataAccess.clear();
    }

    @Test
    public void testRegister() throws DataAccessException {
        UserData newUser = new UserData("Derbear", "1234", "anon@gmail.com");

        assertNotNull(userService.register(newUser));
        assertTrue(userDataAccess.getUser("Derbear", "1234") == newUser);
    }

    @Test
    public void usernameTaken() throws DataAccessException{
        UserData newUser = new UserData("Derbear", "1234", "anon@gmail.com");
        userService.register(newUser);

        try {
            userService.register(newUser);
        } catch (DataAccessException ex){
            assertTrue(ex.getMessage().toLowerCase().contains("taken"));
        }
    }

    @Test
    public void testLogin() throws DataAccessException{
        UserData newUser = new UserData("Derbear", "1234", "anon@gmail.com");
        userService.register(newUser);

        AuthData auth = userService.login("Derbear", "1234");

        assertNotNull(auth);
        assertNotNull(authDataAccess.getAuth(auth.authToken()));
    }

    @Test
    public void forgotPassword() throws DataAccessException{
        UserData newUser = new UserData("Derbear", "1234", "anon@gmail.com");
        userService.register(newUser);

        try{
            AuthData auth = userService.login("Derbear", "5678");
            assertNull(auth);
        } catch (DataAccessException ex){
            assertTrue(ex.getMessage().toLowerCase().contains("auth"));
        }
    }

    @Test
    public void testLogout() throws DataAccessException{
        UserData newUser = new UserData("Derbear", "1234", "anon@gmail.com");
        AuthData auth = userService.register(newUser);

        userService.logout(auth.authToken());

        assertNull(authDataAccess.getAuth(auth.authToken()));
    }

    @Test
    public void invalidLogout() throws DataAccessException{
        UserData newUser = new UserData("Derbear", "1234", "anon@gmail.com");
        AuthData auth = userService.register(newUser);

        try{
            userService.logout("token");
            assertNotNull(authDataAccess.getAuth(auth.authToken()));
        } catch (DataAccessException ex){
            assertTrue(ex.getMessage().toLowerCase().contains("auth"));
        }
    }
}
