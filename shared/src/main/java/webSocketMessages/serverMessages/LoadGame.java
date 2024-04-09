package webSocketMessages.serverMessages;

public class LoadGame extends ServerMessage{

    int game;
    public LoadGame(int gameID) {
        super(ServerMessageType.LOAD_GAME);
        this.game = gameID;
    }
}
