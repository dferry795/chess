package webSocketMessages.userCommands;

public class JoinObserver extends UserGameCommand{
    public JoinObserver(String authToken) {
        super(authToken);
        this.commandType = CommandType.JOIN_OBSERVER;
    }
}
