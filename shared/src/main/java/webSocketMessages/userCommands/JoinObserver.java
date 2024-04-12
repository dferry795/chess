package webSocketMessages.userCommands;

public class JoinObserver extends UserGameCommand{
    int gameID;
    String observer;
    public JoinObserver(String authToken, int id) {
        super(authToken);
        this.commandType = CommandType.JOIN_OBSERVER;
        this.gameID = id;
    }

    public int getId(){return this.gameID;}

}
