package dataAccessTests;

import dataAccess.AuthDataInterface;
import dataAccess.MemoryUserDOA;
import dataAccess.UserDataInterface;
import model.UserData;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

public class AuthDataAccessTests {
    private AuthDataInterface authDataAcces;

    @BeforeEach
    public void setup(){
        this.authDataAcces = new MemoryUserDOA();

        authDataAcces.clear();
    }

    @AfterEach
    public void takedown(){
        authDataAcces.clear();
    }

    @Test
    public void addUserTest(){
        UserData newUser = new UserData("Bob", "1234", "anon@gmail.com");

        userDataAcces.createUser(newUser);

        assertNotNull(userDataAcces.getUser(newUser.username(), newUser.password()), "Invalid Syntax");
    }

    @Test
    public void addUserTwice(){
        UserData newUser = new UserData("Bob", "1234", "anon@gmail.com");
        UserData newUser2 = new UserData("Bob", "5678", "anon@gmail.com");


        userDataAcces.createUser(newUser);
        userDataAcces.createUser(newUser2);

        assertNotNull(userDataAcces.getUser(newUser.username(), newUser.password()));
        assertNull(userDataAcces.getUser(newUser2.username(), newUser2.password()));
    }

    @Test
    public void getUserTest(){
        UserData newUser = new UserData("Bob", "1234", "anon@gmail.com");

        userDataAcces.createUser(newUser);

        assertNotNull(userDataAcces.getUser(newUser.username(), newUser.password()), "Invalid Syntax");
    }

    @Test
    public void wrongPassword(){
        UserData newUser = new UserData("Bob", "1234", "anon@gmail.com");

        userDataAcces.createUser(newUser);

        assertNull(userDataAcces.getUser(newUser.username(), "890"), "Invalid Syntax");
    }

    @Test
    public void clearTest(){
        UserData newUser = new UserData("Bob", "1234", "anon@gmail.com");

        userDataAcces.createUser(newUser);
        assertNotNull(userDataAcces.getUser(newUser.username(), newUser.password()), "Didn't add user");

        userDataAcces.clear();
        assertNull(userDataAcces.getUser(newUser.username(), newUser.password()), "Invalid Syntax");
    }
}
