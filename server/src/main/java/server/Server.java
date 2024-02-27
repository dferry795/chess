package server;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import dataAccess.DataAccessException;
import dataAccess.memoryDB;
import model.AuthData;
import model.GameData;
import model.UserData;
import service.authService;
import service.userService;
import service.gameService;
import spark.*;

import java.util.HashSet;

public class Server {

    private authService authServ;
    private gameService gameServ;
    private memoryDB dataBase;
    private userService userServ;
    private AuthData userAuth;

    public Server(){
        this.dataBase = new memoryDB();
        this.authServ = new authService();
        this.gameServ = new gameService();
        this.userServ = new userService();
        this.userAuth = null;
    }

    public int run(int desiredPort) {
        Spark.port(desiredPort);

        Spark.staticFiles.location("web");

        // Register your endpoints and handle exceptions here.
        Spark.post("/user", this::register);
        Spark.post("/session/:username/:password", (req, res) -> login(req, res));
        Spark.delete("/session/:authToken", this::logout);
        Spark.get("/game/", this::listGames);
        Spark.post("/game/:gameName", this::createGame);
        Spark.put("/game/:ClientColor/:gameID", this::joinGame);
        Spark.delete("/db", this::clearApplication);

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
            this.userAuth = auth;
            return new Gson().toJson(auth);
        } catch (DataAccessException ex){
            throw ex;
        }
    }

    private String login(Request req, Response res) throws DataAccessException {
        var serializer = new Gson();
        try {
            AuthData user_auth = userServ.login(req.params(":username"), req.params(":password"), dataBase);
            this.userAuth = user_auth;
            return serializer.toJson(user_auth);
        } catch (DataAccessException ex){
            throw ex;
        }
    }

    private Object logout(Request req, Response res) throws DataAccessException {
        userServ.logout(this.userAuth.authToken(), dataBase);
        var serializer = new Gson();
        var json = serializer.toJson(null);
        return json;
    }

    private HashSet<GameData> listGames(Request req, Response res) throws DataAccessException {
        try {
            return gameServ.listGames(this.userAuth.authToken(), dataBase);
        } catch (DataAccessException ex){
            throw ex;
        }
    }

    private int createGame(Request req, Response res) throws DataAccessException {
        return gameServ.createGame(req.params(":gameName"), this.userAuth.authToken(), dataBase);
    }

    private Object joinGame(Request req, Response res) throws DataAccessException {
        gameServ.joinGame(req.params(":ClientColor"), Integer.valueOf(req.params(":gameID")), this.userAuth, dataBase);
        var serializer = new Gson();
        var json = serializer.toJson(null);
        return json;
    }

    private Object clearApplication(Request req, Response res){
        authServ.clearApplication(dataBase);
        return new Gson().toJson(this.dataBase);
    }
}
