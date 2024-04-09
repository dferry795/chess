package webSocketMessages.serverMessages;

public class Notification extends ServerMessage{

    String notificationMessage;
    public Notification(String message) {
        super(ServerMessageType.NOTIFICATION);
        this.notificationMessage = message;
    }
}
