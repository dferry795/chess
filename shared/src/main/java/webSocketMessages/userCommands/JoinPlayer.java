package webSocketMessages.userCommands;

public class JoinPlayer extends UserGameCommand{

    String username;
    String color;
    String id;

    public JoinPlayer(String authToken, String username, String color, String id) {
        super(authToken);
        this.commandType = CommandType.JOIN_PLAYER;
        this.username = username
    }
}
