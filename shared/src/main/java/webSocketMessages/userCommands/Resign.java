package webSocketMessages.userCommands;

public class Resign extends UserGameCommand{
    String username;
    public Resign(String authToken, String username) {
        super(authToken);
        this.commandType = CommandType.RESIGN;
        this.username = username;
    }

    public String getUsername(){return username;}
}
