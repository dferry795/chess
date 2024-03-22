package clientTests;

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


}
