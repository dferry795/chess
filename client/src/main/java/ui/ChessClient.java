package ui;

import com.google.gson.Gson;
import model.UserData;

import java.util.Arrays;
import java.util.Scanner;

import static ui.EscapeSequences.RESET_BG_COLOR;
import static ui.EscapeSequences.RESET_TEXT_COLOR;

public class ChessClient {
    private final ServerFacade server;
    private Boolean loggedIn = false;
    public ChessClient(String serverUrl){
        server = new ServerFacade(serverUrl);
    }

    public void run(){
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
        try {
            var tokens = input.toLowerCase().split(" ");
            var cmd = (tokens.length > 0) ? tokens[0] : "help";
            var params = Arrays.copyOfRange(tokens, 1, tokens.length);

            return switch (cmd){
                case "login" -> login(params);
                case "register" -> register(params);
                case "create" -> create(params);
                case "list" -> list(params);
                case "join" -> join(params);
                case "observe" -> observe(params);
                case "logout" -> logout(params);
                case "quit" -> "quit";
                default -> help();
            };
        } catch (Throwable ex){
            return ex.getMessage();
        }
    }

    public String register(String... params){
        if (params.length == 3){
            loggedIn = true;
            Object inputObject = new Gson().toJson(new UserData(params[0], params[1], params[2]));
        }
    }
}
