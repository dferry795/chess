package server;

import spark.*;

public class Server {

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

    private Array register(Request req, Response res){

    }
}
