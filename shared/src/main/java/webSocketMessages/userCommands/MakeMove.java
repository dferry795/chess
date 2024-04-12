package webSocketMessages.userCommands;

import chess.ChessMove;
import chess.ChessPiece;
import chess.ChessPosition;

public class MakeMove extends UserGameCommand{
    int gameID;
    ChessMove move;
    public MakeMove(String authToken, int gameID, ChessMove move) {
        super(authToken);
        this.gameID = gameID;
        this.move = move;
    }

    public int getId(){return gameID;}
    public ChessMove getMove(){return move;}

}
