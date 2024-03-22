package clientTests;

import dataAccess.GameDataInterface;
import dataAccess.SqlGameDOA;
import dataAccess.SqlUserDOA;
import dataAccess.UserDataInterface;
import model.AuthData;
import model.UserData;
import org.junit.jupiter.api.*;
import server.Server;
import ui.ServerFacade;

import static org.junit.jupiter.api.Assertions.*;


public class ServerFacadeTests {

    private static Server server;
    private static ServerFacade facade;

    @BeforeAll
    public static void init() {
        server = new Server();
        var port = server.run(0);
        System.out.println("Started test HTTP server on " + port);
        facade = new ServerFacade("http://localhost:", port);
        try {
            facade.clear();
        } catch (Throwable e){
            System.out.println(e.getMessage());
        }
    }

    @AfterAll
    static void stopServer() {
        server.stop();
    }


    @Test
    public void clear(){
        try {
            AuthData regData = facade.register(new UserData("Dracen", "6789", "hi"));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        try {
            facade.clear();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }

        try {
            AuthData logData = facade.login("Dracen", "6789");
            assertTrue(logData.username() != "Dracen");
        } catch (Throwable ex){
            assertTrue(ex.getMessage().contains("401"));
        }
    }

    @Test
    public void registerSuccess(){
        try {
            AuthData regData = facade.register(new UserData("Dracen", "12345", "hi"));
            UserDataInterface userDataInterface = new SqlUserDOA();
            assertNotNull(userDataInterface.getUser("Dracen", "12345"));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void alreadyTaken(){
        try {
            AuthData regData = facade.register(new UserData("Dracen", "12345", "hi"));
            AuthData regData2 = facade.register(new UserData("Dracen", "6789", "yo"));
        } catch (Exception e) {
            assertTrue(e.getMessage().contains("403"));
        }
    }

    @Test
    public void loginSuccess(){
        try {
            AuthData regData = facade.register(new UserData("Dracen", "12345", "hi"));
            assertNotNull(facade.login("Dracen", "12345"));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void badPassword(){
        try {
            AuthData regData = facade.register(new UserData("Dracen", "12345", "hi"));
            facade.login("Dracen", "125");
        } catch (Exception e) {
            assertTrue(e.getMessage().contains("401"));
        }
    }



    @Test
    public void createSuccess(){
        try {
            AuthData regData = facade.register(new UserData("Dracen", "12345", "hi"));
            assertInstanceOf(Integer.class, facade.create("newGame", regData.authToken()));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void unauthCreate(){
        try {
            AuthData regData = facade.register(new UserData("Dracen", "12345", "hi"));
            facade.create("newGame", "wow");
        } catch (Exception e) {
            assertTrue(e.getMessage().contains("401"));
        }
    }

    @Test
    public void listSuccess(){
        try {
            AuthData regData = facade.register(new UserData("Dracen", "12345", "hi"));
            facade.create("newGame", regData.authToken());
            assertTrue(facade.list(regData.authToken()).size() == 1);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void unauthList(){
        try {
            AuthData regData = facade.register(new UserData("Dracen", "12345", "hi"));
            facade.create("newGame", regData.authToken());
            facade.list("wow");
        } catch (Exception e) {
            assertTrue(e.getMessage().contains("401"));
        }
    }

    @Test
    public void joinSuccess(){
        try {
            AuthData regData = facade.register(new UserData("Dracen", "12345", "hi"));
            int id = facade.create("newGame", regData.authToken());
            facade.join("WHITE", id, regData.authToken());
            GameDataInterface dataInterface = new SqlGameDOA();
            assertTrue(dataInterface.getGame(id).whiteUsername() == "Dracen");
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void joinBad(){
        try {
            AuthData regData = facade.register(new UserData("Dracen", "12345", "hi"));
            AuthData regData2 = facade.register(new UserData("Darian", "6789", "yo"));
            int id = facade.create("newGame", regData.authToken());
            facade.join("WHITE", id, regData.authToken());
            facade.join("WHITE", id, regData2.authToken());
        } catch (Throwable e) {
            assertTrue(e.getMessage().contains("Error"));
        }
    }
}
