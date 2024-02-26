package server;

import dataAccess.DataAccessException;
import dataAccess.memoryDB;
import model.AuthData;
import model.UserData;
import service.authService;
import service.userService;
import service.gameService;
import spark.*;

public class Server {

    private authService authServ;
    private gameService gameServ;
    private memoryDB dataBase;
    private userService userServ;
    public void server(){
        this.dataBase = new memoryDB();
        this.authServ = new authService();
        this.gameServ = new gameService();
        this.userServ = new userService();
    }

    public int run(int desiredPort) {
        Spark.port(desiredPort);

        Spark.staticFiles.location("web");

        // Register your endpoints and handle exceptions here.
        Spark.post("/user/:username/:password/:email", this::register);
        Spark.post("/session/:username/:password", this::login);
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

    private AuthData register(Request req, Response res) throws DataAccessException {
        UserData user = new UserData(req.params(":username"), req.params(":password"), req.params(":email"));
        return userServ.register(user, dataBase);
    }

    private AuthData login(Request req, Response res) throws DataAccessException {
        return userServ.login(req.params(":username"), req.params(":password"), dataBase);
    }

    private void logout(Request req, Response res){
        ...
    }

    private void listGames(Request req, Response res){
        ...
    }

    private void createGame(Request req, Response res){
        ...
    }

    private void joinGame(Request req, Response res){
        ...
    }

    private void clearApplication(Request req, Response res){
        ...
    }
}
