package webSocketMessages.userCommands;

import chess.ChessPiece;
import chess.ChessPosition;

public class MakeMove extends UserGameCommand{
    String username;
    String id;
    ChessPosition startPosition;
    ChessPosition endPosition;
    ChessPiece.PieceType promotion;
    public MakeMove(String authToken, String username, String id, ChessPosition startPosition, ChessPosition endPosition, ChessPiece.PieceType promotion) {
        super(authToken);
        this.commandType = CommandType.MAKE_MOVE;
        this.username = username;
        this.id = id;
        this.startPosition = startPosition;
        this.endPosition = endPosition;
        this.promotion = promotion;
    }

    public String getUsername(){return username;}

    public String getId(){return id;}

    public ChessPosition getStartPosition(){return startPosition;}
    public ChessPosition getEndPosition(){return endPosition;}
    public ChessPiece.PieceType getPromotion(){return promotion;}
}
