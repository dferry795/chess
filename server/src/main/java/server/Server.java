package server;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import dataAccess.*;
import model.*;
import service.AuthService;
import service.UserService;
import service.GameService;
import spark.*;

import java.sql.SQLException;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;

public class Server {

    private final AuthService authServ;
    private final GameService gameServ;
    private final UserService userServ;

    public Server() {
        AuthDataInterface authDataAccess = new SqlAuthDOA();
        GameDataInterface gameDataAccess = new SqlGameDOA();
        UserDataInterface userDataAccess = new SqlUserDOA();
        this.authServ = new AuthService(userDataAccess, gameDataAccess, authDataAccess);
        this.gameServ = new GameService(authDataAccess, gameDataAccess);
        this.userServ = new UserService(userDataAccess, authDataAccess);
    }

    public int run(int desiredPort) {
        Spark.port(desiredPort);

        Spark.staticFiles.location("web");

        // Register your endpoints and handle exceptions here.
        Spark.post("/user", this::register);
        Spark.post("/session", this::login);
        Spark.delete("/session", this::logout);
        Spark.get("/game", this::listGames);
        Spark.post("/game", this::createGame);
        Spark.put("/game", this::joinGame);
        Spark.delete("/db", this::clearApplication);
        Spark.exception(DataAccessException.class, this::exeptionHandler);

        Spark.awaitInitialization();
        return Spark.port();
    }

    public void stop() {
        Spark.stop();
        Spark.awaitStop();
    }

    private Object register(Request req, Response res){
        try {
            UserData user = new Gson().fromJson(req.body(), UserData.class);
            AuthData auth = userServ.register(user);
            return new Gson().toJson(auth);
        } catch (DataAccessException ex){
           return exeptionHandler(ex, req, res);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    private Object login(Request req, Response res){
        try {
            var json = new Gson().fromJson(req.body(), JsonObject.class);
            String username = json.get("username").getAsString();
            String password = json.get("password").getAsString();
            AuthData user_auth = userServ.login(username, password);
            return new Gson().toJson(user_auth);
        } catch (DataAccessException ex) {
            return exeptionHandler(ex, req, res);
        }
    }

    private Object logout(Request req, Response res) {
        try {
            String authToken = req.headers("Authorization");
            userServ.logout(authToken);
            return new Gson().toJson(null);
        } catch (DataAccessException ex) {
            return exeptionHandler(ex, req, res);
        }
    }

    private Object listGames(Request req, Response res){
        try {
            String authToken = req.headers("Authorization");
            var list = gameServ.listGames(authToken);
            HashSet<GameData> new_list = new HashSet<>(list);
            return new Gson().toJson(Map.of("games", new_list));
        } catch (DataAccessException ex){
            return exeptionHandler(ex, req, res);
        }
    }

    private Object createGame(Request req, Response res){
        try {
            String authToken = req.headers("Authorization");
            var json = new Gson().fromJson(req.body(), JsonObject.class);
            String gameName = null;
            if (json.get("gameName") != null){
                gameName = json.get("gameName").getAsString();
            }
            var id = gameServ.createGame(gameName, authToken);
            String idString = Integer.toString(id);
            return new Gson().toJson(Map.of("gameID", idString));
        } catch (DataAccessException ex){
            return exeptionHandler(ex, req, res);
        }
    }

    private Object joinGame(Request req, Response res){
        try {
            String authToken = req.headers("Authorization");
            var json = new Gson().fromJson(req.body(), JsonObject.class);
            String color = null;
            if (json.get("playerColor") != null){
                color = json.get("playerColor").getAsString().toUpperCase();
            }
            int gameID = json.get("gameID").getAsInt();
            gameServ.joinGame(color, gameID, authToken);
            return new Gson().toJson(null);
        } catch(DataAccessException ex){
            return exeptionHandler(ex, req, res);
        }
    }

    private Object clearApplication(Request req, Response res){
            authServ.clearApplication();
            return "{}";
    }

    private Object exeptionHandler(DataAccessException ex, Request req, Response res) {
        if (Objects.equals(ex.getMessage(), "Error: unauthorized")) {
            res.status(401);
            return new Gson().toJson(Map.of("message", "Error: unauthorized"));
        } else if (Objects.equals(ex.getMessage(), "Error: bad request")) {
            res.status(400);
            return new Gson().toJson(Map.of("message", "Error: bad request"));
        } else if (Objects.equals(ex.getMessage(), "Error: already taken")) {
            res.status(403);
            return new Gson().toJson(Map.of("message", "Error: already taken"));
        } else {
            res.status(500);
            return new Gson().toJson(Map.of("message", "Error: description"));
        }
    }
}
