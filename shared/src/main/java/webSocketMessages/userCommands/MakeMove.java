package webSocketMessages.userCommands;

public class MakeMove extends UserGameCommand{
    String username;
    public MakeMove(String authToken, String username) {
        super(authToken);
        this.commandType = CommandType.MAKE_MOVE;
        this.username = username;
    }

    public String getUsername(){return username;}
}
