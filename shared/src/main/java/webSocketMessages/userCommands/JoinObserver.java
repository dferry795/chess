package webSocketMessages.userCommands;

public class JoinObserver extends UserGameCommand{
    String id;
    public JoinObserver(String authToken, String observer, String id) {
        super(authToken, observer);
        this.commandType = CommandType.JOIN_OBSERVER;
        this.id = id;
    }

    public String getId(){return this.id;}
}
