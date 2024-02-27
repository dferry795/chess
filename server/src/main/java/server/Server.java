package server;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import dataAccess.DataAccessException;
import dataAccess.memoryDB;
import model.*;
import service.authService;
import service.userService;
import service.gameService;
import spark.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;

public class Server {

    private final authService authServ;
    private final gameService gameServ;
    private final memoryDB dataBase;
    private final userService userServ;
    private ArrayList<AuthData> userAuth;

    public Server(){
        this.dataBase = new memoryDB();
        this.authServ = new authService();
        this.gameServ = new gameService();
        this.userServ = new userService();
        this.userAuth = new ArrayList<>();
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

    private Object register(Request req, Response res) throws DataAccessException {
        try {
            UserData user = new Gson().fromJson(req.body(), UserData.class);
            AuthData auth = userServ.register(user, dataBase);
            this.userAuth.add(auth);
            return new Gson().toJson(auth);
        } catch (DataAccessException ex){
           return exeptionHandler(ex, req, res);
        }

    }

    private Object login(Request req, Response res) throws DataAccessException {
        try {
            LoginRequest loginReq = new Gson().fromJson(req.body(), LoginRequest.class);
            AuthData user_auth = userServ.login(loginReq.username(), loginReq.password(), dataBase);
            this.userAuth.add(user_auth);
            return new Gson().toJson(user_auth);
        } catch (DataAccessException ex) {
            return exeptionHandler(ex, req, res);
        }
    }

    private Object logout(Request req, Response res) {
        try {
            userServ.logout(this.userAuth.getFirst().authToken(), dataBase);
            this.userAuth.removeFirst();
            return new Gson().toJson(null);
        } catch (DataAccessException ex) {
            return exeptionHandler(ex, req, res);
        }
    }

    private Object listGames(Request req, Response res) throws DataAccessException {
        try {
            var list = gameServ.listGames(this.userAuth.getLast().authToken(), dataBase);

            return new Gson().toJson(Map.of("games", list));
        } catch (DataAccessException ex){
            return exeptionHandler(ex, req, res);
        }
    }

    private Object createGame(Request req, Response res) throws DataAccessException {
        try {
            var name = req.params(":GameName");
            var id = gameServ.createGame(name, this.userAuth.getLast().authToken(), dataBase);
            String idString = Integer.toString(id);
            return new Gson().toJson(Map.of("gameID", idString));
        } catch (DataAccessException ex){
            return exeptionHandler(ex, req, res);
        }
    }

    private Object joinGame(Request req, Response res) throws DataAccessException {
        try {
            joinRequest joinReq = new Gson().fromJson(req.body(), joinRequest.class);
            gameServ.joinGame(joinReq.playerColor(), joinReq.gameID(), this.userAuth.getLast(), dataBase);
            var serializer = new Gson();
            var json = serializer.toJson(null);
            return json;
        } catch(DataAccessException ex){
            return exeptionHandler(ex, req, res);
        }
    }

    private Object clearApplication(Request req, Response res){
        authServ.clearApplication(dataBase);
        return new Gson().toJson(null);
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
