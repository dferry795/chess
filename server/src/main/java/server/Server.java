package server;

import dataAccess.memoryDB;
import model.UserData;
import service.userService;
import service.gameService;
import spark.*;

public class Server {

    private Object dataBase;
    public server(){
        this.dataBase = new memoryDB();
    }

    public int run(int desiredPort) {
        Spark.port(desiredPort);

        Spark.staticFiles.location("web");

        // Register your endpoints and handle exceptions here.
        Spark.post("/user/:username/:password/:email", userService::register);
        Spark.post("/session/:username/:password", userService::login);
        Spark.delete("/session/:authToken", userService::logout);
        Spark.get("/game/", gameService::listGames);
        Spark.post("/game/:gameName", gameService::createGame);
        Spark.put("/game/:ClientColor/:gameID", gameService::joinGame);
        Spark.delete("/db", this::clearApplication);

        Spark.awaitInitialization();
        return Spark.port();
    }

    public void stop() {
        Spark.stop();
        Spark.awaitStop();
    }
}
