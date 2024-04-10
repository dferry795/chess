package webSocketMessages.userCommands;

public class JoinPlayer extends UserGameCommand{

    String username;
    String color;
    String id;

    public JoinPlayer(String authToken, String username, String color, String id) {
        super(authToken);
        this.commandType = CommandType.JOIN_PLAYER;
        this.color = color;
        this.id = id;
        this.username = username;
    }

    public String getId(){ return this.id;}

    public String getPlayerColor(){return this.color;}
    public String getUsername(){return username;}
}
