package ui;

import chess.ChessBoard;
import chess.ChessGame;
import com.google.gson.Gson;
import model.AuthData;
import model.GameData;
import model.UserData;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Scanner;

import static ui.EscapeSequences.*;

public class ChessClient {
    private final ServerFacade server;
    private Boolean loggedIn = false;
    private String authToken = null;
    public ChessClient(String serverUrl){
        server = new ServerFacade(serverUrl);
    }

    public void run(){
        System.out.println(RESET_BG_COLOR + RESET_TEXT_COLOR + "Welcome to 240 chess. Type Help to get started." );

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
                case "list" -> list();
                case "join" -> join(params);
                case "observe" -> observe(params);
                case "logout" -> logout();
                case "quit" -> "quit";
                default -> help();
            };
        } catch (Throwable ex){
            return ex.getMessage();
        }
    }

    public String register(String... params) throws Exception {
        if (params.length == 3){
            loggedIn = true;
            UserData inputObject = new UserData(params[0], params[1], params[2]);
            AuthData responseObj = server.register(inputObject);
            authToken = responseObj.authToken();
            return "Logged in as " + responseObj.username();
        } else {
            return SET_TEXT_COLOR_RED + "Register expected <username> <password> <email>";
        }
    }

    public String login(String... params) throws Exception {
        if (params.length == 2){
            loggedIn = true;
            AuthData responseObj = server.login(params[0], params[1]);
            authToken = responseObj.authToken();
            return "Logged in as " + responseObj.username();
        } else {
            return SET_TEXT_COLOR_RED + "Login expected <username> <password>";
        }
    }

    public String logout() throws Exception {
        if (loggedIn){
            loggedIn = false;
            server.logout(authToken);
            return "Logged out successfully";
        } else {
            return SET_TEXT_COLOR_RED + "Must be logged in for that action";
        }
    }

    public String create(String... params) throws Exception {
        if (loggedIn){
            if (params.length == 1){
                int gameID = server.create(params[0], authToken);
                return "Created " + params[0] + "with ID = " + gameID;
            } else {
                return SET_TEXT_COLOR_RED + "Create expected <gameName>";
            }
        } else {
            return SET_TEXT_COLOR_RED + "Must be logged in for that action";
        }
    }

    public String list() throws Exception {
        if (loggedIn){
            String result = "Games:\nName, White, Black, ID\n";

            HashSet<GameData> gameList = server.list(authToken);

            for (GameData game: gameList){
                result = result + game.gameName() + ", " + game.whiteUsername() + ", " + game.blackUsername() + ", " + Integer.toString(game.gameID()) + "\n";
            }

            return result;
        } else {
            return SET_TEXT_COLOR_RED + "Must be logged in for that action";
        }
    }

    public String join(String... params) throws Throwable {
        if (loggedIn){
            String result = "";
            if (params.length == 2){
                server.join(params[1], Integer.parseInt(params[0]), authToken);
                result += "Success!";
                ChessBoard board = new ChessBoard();
                board.resetBoard();
                ChessboardUI boardBuilder = new ChessboardUI(board);
                result += boardBuilder.buildBoard();
            } else if (params.length == 1) {
                server.join(null, Integer.parseInt(params[0]), authToken);
                result += "Success1";
                ChessBoard board = new ChessBoard();
                board.resetBoard();
                result += new ChessboardUI(board).buildBoard();
            } else {
                result += SET_TEXT_COLOR_RED + "Join expected <ID> [WHITE|BLACK|<empty>]";
            }
            return result;
        } else {
            return SET_TEXT_COLOR_RED + "Must be logged in for that action";
        }
    }

    public String observe(String... params){

    }

    public String help(String... params){

    }
}
