package dataAccessTests;

import dataAccess.AuthDataInterface;
import dataAccess.MemoryAuthDOA;
import dataAccess.MemoryUserDOA;
import dataAccess.UserDataInterface;
import model.AuthData;
import model.UserData;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class AuthDataAccessTests {
    private AuthDataInterface authDataAcces;

    @BeforeEach
    public void setup(){
        this.authDataAcces = new MemoryAuthDOA();

        authDataAcces.clear();
    }

    @AfterEach
    public void takedown(){
        authDataAcces.clear();
    }

    @Test
    public void addAuthTest(){
        AuthData newAuth = new AuthData("token", "Joseph");

        authDataAcces.createAuth(newAuth);

        assertNotNull(authDataAcces.getAuth("token"));
    }

    @Test
    public void addAuthTwice(){
        AuthData newAuth = new AuthData("token", "Joseph");
        AuthData newAuth2 = new AuthData("token", "Jared");

        authDataAcces.createAuth(newAuth);
        authDataAcces.createAuth(newAuth2);

        assertTrue(authDataAcces.getAuth("token").username() == "Joseph");
        assertFalse(authDataAcces.getAuth("token").username() == "Jared");
    }

    @Test
    public void getAuthTest(){
        AuthData newAuth = new AuthData("token", "Joseph");

        authDataAcces.createAuth(newAuth);

        assertNotNull(authDataAcces.getAuth("token"));
    }

    @Test
    public void wrongToken(){
        AuthData newAuth = new AuthData("token", "Joseph");

        authDataAcces.createAuth(newAuth);

        assertNull(authDataAcces.getAuth("tok3n"));
    }

    @Test
    public void deleteTest(){
        AuthData newAuth = new AuthData("token", "Joseph");

        authDataAcces.createAuth(newAuth);
        assertNotNull(authDataAcces.getAuth("token"));

        authDataAcces.deleteAuth(newAuth);
        assertNull(authDataAcces.getAuth("token"));
    }

    @Test
    public void deleteWrongAuth(){
        AuthData newAuth = new AuthData("token", "Joseph");

        authDataAcces.createAuth(newAuth);
        assertNotNull(authDataAcces.getAuth("token"));

        authDataAcces.deleteAuth(new AuthData("tok3n", "Joseph"));
        assertNotNull(authDataAcces.getAuth("token"));
    }

    @Test
    public void clearTest(){
        AuthData newAuth = new AuthData("token", "Joseph");

        authDataAcces.createAuth(newAuth);
        assertNotNull(authDataAcces.getAuth("token"));

        authDataAcces.clear();
        assertNull(authDataAcces.getAuth("token"));
    }
}
