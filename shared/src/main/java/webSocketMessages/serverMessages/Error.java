package webSocketMessages.serverMessages;

public class Error extends ServerMessage{

    String errorMessage;
    public Error(String message) {
        super(ServerMessageType.ERROR);
        this.errorMessage = message;
    }

    public String toString(){return this.errorMessage;}
}
