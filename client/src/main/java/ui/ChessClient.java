package ui;

import chess.ChessBoard;
import chess.ChessGame;
import com.google.gson.Gson;
import model.AuthData;
import model.GameData;
import model.UserData;

import java.util.*;

import static ui.EscapeSequences.*;

public class ChessClient {
    private final ServerFacade server;
    private Boolean loggedIn = false;
    private Boolean inGame = false;
    private ArrayList<String> authTokenList = new ArrayList<>();
    public ChessClient(String serverUrl, int port){
        server = new ServerFacade(serverUrl, port);
    }

    public void run(){
        System.out.println(RESET_BG_COLOR + RESET_TEXT_COLOR + SET_TEXT_BOLD + SET_TEXT_COLOR_YELLOW + "Welcome to 240 chess. Type Help to get started." );

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
            var tokens = input.split(" ");
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
                case "clear" -> clear();
                case "quit" -> "quit";
                default -> help();
            };
        } catch (Throwable ex){
            return ex.getMessage() + "\n";
        }
    }

    public String register(String... params) throws Exception {
        if (params.length == 3){
            UserData inputObject = new UserData(params[0], params[1], params[2]);
            AuthData responseObj = server.register(inputObject);
            authTokenList.add(responseObj.authToken());
            loggedIn = true;
            return SET_TEXT_COLOR_YELLOW + "Logged in as " + responseObj.username() + "\n";
        } else {
            return SET_TEXT_COLOR_RED + "Register expected <username> <password> <email>\n";
        }
    }

    public String login(String... params) throws Exception {
        if (params.length == 2){
            AuthData responseObj = server.login(params[0], params[1]);
            authTokenList.add(responseObj.authToken());
            loggedIn = true;
            return SET_TEXT_COLOR_YELLOW + "Logged in as " + responseObj.username() + "\n";
        } else {
            return SET_TEXT_COLOR_RED + "Login expected <username> <password>\n";
        }
    }

    public String logout() throws Exception {
        if (loggedIn){
            loggedIn = false;
            String targetToken = authTokenList.getLast();
            server.logout(targetToken);
            authTokenList.remove(targetToken);
            return SET_TEXT_COLOR_YELLOW + "Logged out successfully\n";
        } else {
            return SET_TEXT_COLOR_RED + "Must be logged in for that action\n";
        }
    }

    public String create(String... params) throws Exception {
        if (loggedIn){
            if (params.length == 1){
                int gameID = server.create(params[0], authTokenList.getLast());
                return SET_TEXT_COLOR_YELLOW + "Created " + params[0] + " with ID = " + gameID + "\n";
            } else {
                return SET_TEXT_COLOR_RED + "Create expected <gameName>\n";
            }
        } else {
            return SET_TEXT_COLOR_RED + "Must be logged in for that action\n";
        }
    }

    public String list() throws Exception {
        if (loggedIn){
            String result = "Games:\nName, White, Black, ID\n";

            HashMap<String, ArrayList<GameData>> gameList = server.list(authTokenList.getLast());


            if (!gameList.isEmpty()) {
                for (ArrayList<GameData> gameDataList : gameList.values()) {
                    for (GameData game : gameDataList){
                        result = result + game.gameName() + ", " + game.whiteUsername() + ", " + game.blackUsername() + ", " + Integer.toString(game.gameID()) + "\n";
                    }
                }
            }
            return SET_TEXT_COLOR_YELLOW + result + "\n";
        } else {
            return SET_TEXT_COLOR_RED + "Must be logged in for that action\n";
        }
    }

    public String join(String... params) throws Throwable {
        if (loggedIn){
            String result = "";
            if (params.length == 2){
                server.join(params[1], Integer.parseInt(params[0]), authTokenList.getLast());
                result += SET_TEXT_COLOR_YELLOW + "Success!\n";
                ChessBoard board = new ChessBoard();
                board.resetBoard();
                ChessboardUI boardBuilder = new ChessboardUI(board);
                result += boardBuilder.buildBoard();
            } else if (params.length == 1) {
                server.join(null, Integer.parseInt(params[0]), authTokenList.getLast());
                result += SET_TEXT_COLOR_YELLOW + "Success!\n";
                ChessBoard board = new ChessBoard();
                board.resetBoard();
                result += new ChessboardUI(board).buildBoard();
            } else {
                result += SET_TEXT_COLOR_RED + "Join expected <ID> [WHITE|BLACK|<empty>]\n";
            }
            return result;
        } else {
            return SET_TEXT_COLOR_RED + "Must be logged in for that action\n";
        }
    }

    public String observe(String... params) throws Throwable {
        if (loggedIn){
            if (params.length == 1){
                server.join(null, Integer.parseInt(params[0]), authTokenList.getLast());
                String result = "";
                result += "Success!\n";
                ChessBoard board = new ChessBoard();
                board.resetBoard();
                result += new ChessboardUI(board).buildBoard();
                return result + "\n";
            } else {
                return SET_TEXT_COLOR_RED + "Observe expected <ID>\n";
            }
        } else {
            return SET_TEXT_COLOR_RED + "Must be logged in for that action\n";
        }
    }

    public String help(){
        String result = SET_TEXT_COLOR_BLUE;
        if (inGame){
            result += SET_TEXT_COLOR_BLUE + "move <ID> <start(row,column)> <end(row,column)> " + SET_TEXT_COLOR_MAGENTA + "- a chess piece to position\n";
            result += SET_TEXT_COLOR_BLUE + "redraw " + SET_TEXT_COLOR_MAGENTA + "- the chess board\n";
            result += SET_TEXT_COLOR_BLUE + "leave " + SET_TEXT_COLOR_MAGENTA + "- the game\n";
            result += SET_TEXT_COLOR_BLUE + "resign " + SET_TEXT_COLOR_MAGENTA + "- forfeit the game\n";
            result += SET_TEXT_COLOR_BLUE + "highlight <piece(row,column)>" + SET_TEXT_COLOR_MAGENTA + "- highlight all the moves a piece can make\n";
            result += SET_TEXT_COLOR_BLUE + "help " + SET_TEXT_COLOR_MAGENTA + "- with commands\n";
        } else if (loggedIn){
            result += SET_TEXT_COLOR_BLUE + "create <gameName> " + SET_TEXT_COLOR_MAGENTA + "- a game\n";
            result += SET_TEXT_COLOR_BLUE + "list " + SET_TEXT_COLOR_MAGENTA + "- games\n";
            result += SET_TEXT_COLOR_BLUE + "join <ID> [WHITE|BLACK|<empty] " + SET_TEXT_COLOR_MAGENTA + "- a game\n";
            result += SET_TEXT_COLOR_BLUE + "observe <ID> " + SET_TEXT_COLOR_MAGENTA + "- a game\n";
            result += SET_TEXT_COLOR_BLUE + "logout " + SET_TEXT_COLOR_MAGENTA + "- when you're done\n";
            result += SET_TEXT_COLOR_BLUE + "quit " + SET_TEXT_COLOR_MAGENTA + "- playing chess\n";
            result += SET_TEXT_COLOR_BLUE + "help " + SET_TEXT_COLOR_MAGENTA + "- with commands\n";
        } else {
            result += "register <username> <password> <email> " + SET_TEXT_COLOR_MAGENTA + "- to create an account\n";
            result += SET_TEXT_COLOR_BLUE + "login <username> <password> " + SET_TEXT_COLOR_MAGENTA + "- to play chess\n";
            result += SET_TEXT_COLOR_BLUE + "quit " + SET_TEXT_COLOR_MAGENTA + "- playing chess\n";
            result += SET_TEXT_COLOR_BLUE + "help " + SET_TEXT_COLOR_MAGENTA + "- with commands\n";
        }
        return result;
    }

    public String clear() throws Throwable {
        server.clear();
        return SET_TEXT_COLOR_YELLOW + "Data cleared\n";
    }
}
