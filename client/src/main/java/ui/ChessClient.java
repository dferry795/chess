package ui;

import chess.ChessBoard;

import java.util.Scanner;

import static ui.EscapeSequences.*;

public class ChessClient {

    private final Object client;
    private boolean loggedIn;

    public ChessClient(){
        this.client = null;
    }
    public static void main(String[] args) {
        var serverURL = "http://localhost:8080";
        if (args.length == 1){
            serverURL = args[0];
        }

        this.client = new ServerFacade(serverURL);

        System.out.println(RESET_BG_COLOR + RESET_TEXT_COLOR + "Welcome to 240 chess. Type Help to get started.");

        Scanner scanner = new Scanner(System.in);
        var result = "";
        while (!result.equals("quit")){
            String line = scanner.nextLine();

            try {
                result = eval(line);
                System.out.print(result);
            } catch (Throwable e){
                var msg = e.toString();
                System.out.print(msg);
            }
        }
        System.out.println();
    }

    public String eval(String input){

    }
}
