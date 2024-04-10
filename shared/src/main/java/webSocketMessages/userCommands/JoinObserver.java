package webSocketMessages.userCommands;

public class JoinObserver extends UserGameCommand{
    String id;
    String observer;
    public JoinObserver(String authToken, String observer, String id) {
        super(authToken);
        this.commandType = CommandType.JOIN_OBSERVER;
        this.id = id;
        this.observer = observer;
    }

    public String getId(){return this.id;}

    public String getUsername(){return observer;}
}
