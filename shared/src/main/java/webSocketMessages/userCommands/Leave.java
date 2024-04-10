package webSocketMessages.userCommands;

public class Leave extends UserGameCommand{
    public Leave(String authToken, String username) {
        super(authToken, username);
        this.commandType = CommandType.LEAVE;
    }
}
