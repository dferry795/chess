package webSocketMessages.userCommands;

public class Resign extends UserGameCommand{
    public Resign(String authToken, String username) {
        super(authToken, username);
        this.commandType = CommandType.RESIGN;
    }
}
