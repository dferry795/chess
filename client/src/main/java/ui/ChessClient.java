package ui;

import static ui.EscapeSequences.*;

public class ChessClient {
    public static void main(String[] args) {
        var serverURL = "http://localhost:8080";
        if (args.length == 1){
            serverURL = args[0];
        }

        System.out.println(SET_BG_COLOR_BLACK + "Welcome to 240 chess. Type Help to get started.");
    }
}
