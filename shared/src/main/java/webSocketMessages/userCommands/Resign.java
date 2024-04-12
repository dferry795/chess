package webSocketMessages.userCommands;

public class Resign extends UserGameCommand{
    int gameID;
    public Resign(String authToken, int id) {
        super(authToken);
        this.commandType = CommandType.RESIGN;
        this.gameID = id;
    }

    public int getGameID(){return this.gameID;}
}
