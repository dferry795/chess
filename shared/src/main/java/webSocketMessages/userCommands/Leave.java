package webSocketMessages.userCommands;

public class Leave extends UserGameCommand{
    int gameID;
    public Leave(String authToken, int id) {
        super(authToken);
        this.commandType = CommandType.LEAVE;
        this.gameID = id;
    }

    public int getGameID(){return this.gameID;}
}
