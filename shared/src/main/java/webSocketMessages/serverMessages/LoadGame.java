package webSocketMessages.serverMessages;

import chess.ChessGame;

public class LoadGame extends ServerMessage{

    ChessGame game;
    public LoadGame(ChessGame game, String message) {
        super(ServerMessageType.LOAD_GAME, message);
        this.game = game;
    }
}
