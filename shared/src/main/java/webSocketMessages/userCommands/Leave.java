package webSocketMessages.userCommands;

public class Leave extends UserGameCommand{
    String username;
    public Leave(String authToken, String username) {
        super(authToken);
        this.commandType = CommandType.LEAVE;
        this.username = username;
    }

    public String getUsername(){return username;}
}
