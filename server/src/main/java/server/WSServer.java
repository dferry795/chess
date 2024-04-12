package server;

import chess.ChessGame;
import chess.InvalidMoveException;
import com.google.gson.Gson;
import dataAccess.*;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import service.GameService;
import webSocketMessages.serverMessages.Error;
import webSocketMessages.serverMessages.LoadGame;
import webSocketMessages.serverMessages.Notification;
import webSocketMessages.userCommands.*;
import org.eclipse.jetty.websocket.api.Session;


import java.io.IOException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;


@WebSocket
public class WSServer {
    public final Map<String, Connection> connections = new ConcurrentHashMap<>();
    public final Map<Integer, Set<String>> gameConnections = new ConcurrentHashMap<>();
    public final SqlGameDOA gameDataInterface = new SqlGameDOA();
    public final SqlAuthDOA authDataAccess = new SqlAuthDOA();

    @OnWebSocketMessage
    public void onMessage(Session session, String message) throws Exception {
        UserGameCommand action = new Gson().fromJson(message, UserGameCommand.class);

        switch (action.getCommandType()) {
            case JOIN_PLAYER -> joinPlayer(new Gson().fromJson(message, JoinPlayer.class), session);
            case JOIN_OBSERVER -> joinObserver(new Gson().fromJson(message, JoinObserver.class), session);
            case MAKE_MOVE -> makeMove(new Gson().fromJson(message, MakeMove.class), session);
            case LEAVE -> leave(new Gson().fromJson(message, Leave.class), session);
            case RESIGN -> resign(new Gson().fromJson(message, Resign.class), session);
        }
    }

    private void joinPlayer(JoinPlayer action, Session session) throws IOException {
        try {
            var authData = authDataAccess.getAuth(action.getAuthString());

            if (authData == null){
                throw new RuntimeException("Unauthorized");
            } else {
                var username = authData.username();
                var gameData = gameDataInterface.getGame(action.getId());

                if (gameData == null){
                    session.getRemote().sendString(new Gson().toJson(new Error("Error: Game doesn't exist")));
                } else {
                    var game = gameData.game();

                    String color = null;
                    if (action.getPlayerColor() == ChessGame.TeamColor.WHITE){
                        color = "white";
                    } else if (action.getPlayerColor() == ChessGame.TeamColor.BLACK) {
                        color = "black";
                    }

                    if ((color.equals("black") && (!gameData.blackUsername().equals(username) || gameData.blackUsername() == null)) || (color.equals("white") && (!gameData.whiteUsername().equals(username) || gameData.whiteUsername() == null))){
                        session.getRemote().sendString(new Gson().toJson(new Error("Error: Position unavailable")));
                    } else {
                        Connection connection = new Connection(username, session);
                        connections.put(username, connection);

                        session.getRemote().sendString(new Gson().toJson(new LoadGame(game, "Success!")));

                        String message = username + " joined game " + String.valueOf(action.getId());
                        Notification notification = new Notification(message);
                        broadcast(action.getId(), username, notification);

                        Set<String> gamePlayers = gameConnections.getOrDefault(action.getId(), new HashSet<>());
                        gamePlayers.add(username);
                        gameConnections.put(action.getId(), gamePlayers);
                    }
                }
            }
        } catch (Throwable ex){
            session.getRemote().sendString(new Gson().toJson(new Error("Error: " + ex.getMessage())));
        }
    }


    public void joinObserver(JoinObserver action, Session session) throws IOException {
        var authData = authDataAccess.getAuth(action.getAuthString());

        if (authData == null){
            session.getRemote().sendString(new Gson().toJson(new Error("Error: unauthorized")));
        } else {
            var username = authData.username();
            var gameData = gameDataInterface.getGame(action.getId());

            if (gameData == null){
                session.getRemote().sendString(new Gson().toJson(new Error("Error: game doesn't exist")));
            } else {
                var game = gameData.game();

                Connection connection = new Connection(username, session);
                connections.put(username, connection);

                session.getRemote().sendString(new Gson().toJson(new LoadGame(game, "Success!")));

                String message = username + " joined game " + String.valueOf(action.getId()) + " as observer";
                Notification notification = new Notification(message);
                broadcast(action.getId(), username, notification);

                Set<String> gamePlayers = gameConnections.getOrDefault(action.getId(), new HashSet<>());
                gamePlayers.add(username);
                gameConnections.put(action.getId(), gamePlayers);
            }
        }
    }

    public void makeMove(MakeMove action, Session session) throws IOException {
        var username = authDataAccess.getAuth(action.getAuthString()).username();
        var gameData = gameDataInterface.getGame(action.getId());
        var game = gameData.game();

        if (!game.isInCheckmate(ChessGame.TeamColor.WHITE) && !game.isInCheckmate(ChessGame.TeamColor.BLACK)) {
            if (game.getTeamTurn() == ChessGame.TeamColor.WHITE && gameData.whiteUsername().equals(username)) {
                if (game.validMoves(action.getMove().getStartPosition()) != null && game.validMoves(action.getMove().getStartPosition()).contains(action.getMove())) {
                    try {
                        game.makeMove(action.getMove());
                        gameDataInterface.updateGame(action.getId(), game);

                        session.getRemote().sendString(new Gson().toJson(new LoadGame(game, "Success!")));
                        broadcast(action.getId(), username, new LoadGame(game, "Success!"));
                        broadcast(action.getId(), username, new Notification(username + " made move: " + action.getMove().toString()));

                    } catch (InvalidMoveException e) {
                        session.getRemote().sendString(new Gson().toJson(new Error("Error: invalid move")));
                    }

                } else {
                    session.getRemote().sendString(new Gson().toJson(new Error("Error: invalid move")));
                }


            } else if (game.getTeamTurn() == ChessGame.TeamColor.BLACK && gameData.blackUsername().equals(username)) {
                if (game.validMoves(action.getMove().getStartPosition()) == null && game.validMoves(action.getMove().getStartPosition()).contains(action.getMove())) {
                    try {
                        game.makeMove(action.getMove());
                    } catch (InvalidMoveException e) {
                        session.getRemote().sendString(new Gson().toJson(new Error("Error: invalid move")));
                    }
                    gameDataInterface.updateGame(action.getId(), game);

                    session.getRemote().sendString(new Gson().toJson(new LoadGame(game, "Success!")));
                    broadcast(action.getId(), username, new LoadGame(game, "Success!"));
                    broadcast(action.getId(), username, new Notification(username + " made move: " + action.getMove().toString()));
                } else {
                    session.getRemote().sendString(new Gson().toJson(new Error("Error: invalid move")));
                }
            } else {
                session.getRemote().sendString(new Gson().toJson(new Error("Error: either it is not your turn or you are an observer")));
            }
        } else {
            session.getRemote().sendString(new Gson().toJson(new Error("Error: game is over mate")));
        }
    }

    public void resign(Resign action, Session session) throws IOException {
        var username = authDataAccess.getAuth(action.getAuthString()).username();
        var gameData = gameDataInterface.getGame(action.getGameID());
        var game = gameData.game();

        if (!game.resign) {
            if (gameData.whiteUsername().equals(username)) {
                game.resign = true;
                gameDataInterface.updateGame(action.getGameID(), game);

                String message = username + " has forfeited the game";
                Notification notification = new Notification(message);

                session.getRemote().sendString(new Gson().toJson(notification));
                broadcast(action.getGameID(), username, notification);
            } else if (gameData.blackUsername().equals(username)) {
                game.resign = true;
                gameDataInterface.updateGame(action.getGameID(), game);

                String message = username + " has forfeited the game";
                Notification notification = new Notification(message);

                session.getRemote().sendString(new Gson().toJson(notification));
                broadcast(action.getGameID(), username, notification);
            }else {
                session.getRemote().sendString(new Gson().toJson(new Error("Error: already cannot resign as observer")));
            }
        } else {
            session.getRemote().sendString(new Gson().toJson(new Error("Error: already resigned")));
        }
    }

    public void leave(Leave action, Session session) throws IOException {
        var username = authDataAccess.getAuth(action.getAuthString()).username();
        var gameData = gameDataInterface.getGame(action.getGameID());

        if (gameData.blackUsername() == username){
            gameDataInterface.blackUpdateGame(action.getGameID(), null);

            String message = username + " has left the game";
            Notification notification = new Notification(message);
            broadcast(action.getGameID(), username, notification);

            connections.remove(username);
        } else if (gameData.whiteUsername() == username ) {
            gameDataInterface.whiteUpdateGame(action.getGameID(), null);

            String message = username + " has left the game";
            Notification notification = new Notification(message);
            broadcast(action.getGameID(), username, notification);

            connections.remove(username);
        } else {
            String message = username + " has left the game";
            Notification notification = new Notification(message);
            broadcast(action.getGameID(), username, notification);

            connections.remove(username);
        }
    }

    private void broadcast(int gameId, String excludeRootUser, Object notification) throws IOException {
        Set<String> gamePlayers = gameConnections.getOrDefault(gameId, Collections.emptySet());
        for (String player : gamePlayers) {
            if (player.equals(excludeRootUser)) {
                continue; // Skip sending message to root user
            }
            Connection connection = connections.get(player);
            if (connection != null && connection.session.isOpen()) {
                connection.send(new Gson().toJson(notification));
            }
        }
    }


}
