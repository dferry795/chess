package ui;


import server.Server;

public class ClientMain {
    public static void main(String[] args) {
        var serverURL = "http://localhost:";
        if (args.length == 1){
            serverURL = args[0];
        }

        Server server = new Server();
        var port = server.run(0);
        System.out.println("Started test HTTP server on " + port);

        new ChessClient(serverURL, port).run();

        server.stop();
    }
}
