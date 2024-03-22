package clientTests;

import dataAccess.*;
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
    }

    @AfterAll
    static void stopServer(){
        server.stop();
    }

    @BeforeEach
    public void setup(){
        try {
            facade.clear();
        } catch (Throwable e){
            System.out.println(e.getMessage());
        }
    }


    @Test
    public void clear(){
        try {
            AuthData regData = facade.register(new UserData("Dracen", "6789", "hi"));
            assertNotNull(regData);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        try {
            facade.clear();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }

        try {
            UserDataInterface userDataInterface = new SqlUserDOA();
            assertNull(userDataInterface.getUser("Dracen", "6789"));
        } catch (Throwable ex){
            throw new RuntimeException();
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
    public void logoutSuccess(){
        try {
            AuthData regData = facade.register(new UserData("Dracen", "12345", "hi"));
            facade.logout(regData.authToken());
            AuthDataInterface authDataInterface = new SqlAuthDOA();
            assertNull(authDataInterface.getAuth(regData.authToken()));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void unauthLogout(){
        try {
            AuthData regData = facade.register(new UserData("Dracen", "12345", "hi"));
            facade.logout("wow");
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
            facade.join(null, id, regData.authToken());
            GameDataInterface dataInterface = new SqlGameDOA();
            assertNotNull(dataInterface.getGame(id));
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
            assertTrue(e.getMessage().contains("403"));
        }
    }
}
