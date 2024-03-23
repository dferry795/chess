package ui;


public class ClientMain {
    public static void main(String[] args) {
        var serverURL = "http://localhost:";
        if (args.length == 1){
            serverURL = args[0];
        }

        new ChessClient(serverURL, 8080).run();

    }
}
