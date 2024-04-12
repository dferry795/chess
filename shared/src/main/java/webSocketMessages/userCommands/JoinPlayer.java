package webSocketMessages.userCommands;

import chess.ChessGame;

public class JoinPlayer extends UserGameCommand{

    ChessGame.TeamColor playerColor;
    int gameID;

    public JoinPlayer(int id, String authToken, ChessGame.TeamColor color) {
        super(authToken);
        this.commandType = CommandType.JOIN_PLAYER;
        this.playerColor = color;
        this.gameID = id;
    }

    public int getId(){ return this.gameID;}

    public ChessGame.TeamColor getPlayerColor(){return this.playerColor;}
}
