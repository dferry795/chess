package webSocketMessages.userCommands;

import chess.ChessGame;

public class JoinPlayer extends UserGameCommand{

    String username;
    ChessGame.TeamColor color;
    String id;

    public JoinPlayer(String authToken, String username, ChessGame.TeamColor color, String id) {
        super(authToken);
        this.commandType = CommandType.JOIN_PLAYER;
        this.color = color;
        this.id = id;
        this.username = username;
    }

    public String getId(){ return this.id;}

    public ChessGame.TeamColor getPlayerColor(){return this.color;}
    public String getUsername(){return username;}
}
