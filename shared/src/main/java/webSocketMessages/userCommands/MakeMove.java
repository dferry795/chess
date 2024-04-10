package webSocketMessages.userCommands;

public class MakeMove extends UserGameCommand{
    public MakeMove(String authToken, String username) {
        super(authToken, username);
        this.commandType = CommandType.MAKE_MOVE;
    }
}
