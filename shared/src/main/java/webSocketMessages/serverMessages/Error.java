package webSocketMessages.serverMessages;

public class Error extends ServerMessage{

    String errorMessage;

    public Error(String message) {
        super(ServerMessageType.ERROR, message);
        this.errorMessage = message;
    }
}
