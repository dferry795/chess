import chess.*;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import server.Server;
import spark.Spark;

public class Main {
    public static void main(String[] args) {
        Server server = new Server();
        var port = server.run(8080);
        System.out.println("Started test HTTP server on " + port);
    }
}