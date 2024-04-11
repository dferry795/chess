package server;


import chess.ChessGame;
import com.google.gson.Gson;
import dataAccess.DataAccessException;
import dataAccess.DatabaseManager;
import dataAccess.GameDataInterface;
import dataAccess.SqlGameDOA;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketError;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import webSocketMessages.serverMessages.Error;
import webSocketMessages.serverMessages.LoadGame;
import webSocketMessages.serverMessages.Notification;
import webSocketMessages.userCommands.JoinObserver;
import webSocketMessages.userCommands.JoinPlayer;
import webSocketMessages.userCommands.MakeMove;
import webSocketMessages.userCommands.UserGameCommand;
import static dataAccess.DatabaseManager.getConnection;

import org.eclipse.jetty.websocket.api.Session;


import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

import static java.sql.DriverManager.getConnection;

@WebSocket
public class WSServer {
    public final ConcurrentHashMap<String, Connection> connections = new ConcurrentHashMap<>();
    public final GameDataInterface gameDataInterface = new SqlGameDOA();


    private void joinPlayer(JoinPlayer action, Session session) throws IOException {
        try {
            Connection connection = new Connection(action.getUsername(), session);
            connections.put(action.getUsername(), connection);
            String color;

            if (action.getPlayerColor() == ChessGame.TeamColor.WHITE) {
                color = "white";
            } else if (action.getPlayerColor() == ChessGame.TeamColor.BLACK) {
                color = "black";
            } else {
                color = "observer";
            }
            String message = action.getAuthString() + " joined " + action.getId() + " as " + color;
            Notification notification = new Notification(message);


            LoadGame loadGame = new LoadGame(new ChessGame());
            session.getRemote().sendString(new Gson().toJson(loadGame));
        } catch (Throwable ex){
            session.getRemote().sendString(new Gson().toJson(new Error("Error: " + ex.getMessage())));
        }
    }


    public void joinObserver(JoinObserver action, Session session) throws IOException {
        try {
            Connection connection = new Connection(action.getUsername(), session);
            connections.put(action.getUsername(), connection);
            var message = action.getAuthString() + " joined " + action.getId() + " as an observer";
            var notification = new Notification(message);
            broadcast(action.getUsername(), notification);
        } catch (Throwable ex){
            session.getRemote().sendString(new Gson().toJson(new Error("Error: " + ex.getMessage())));
        }
    }

    public void makeMove(MakeMove action, Session session) throws IOException {
        try {
            Connection connection = new Connection(action.getUsername(), session);
            connections.put(action.getUsername(), connection);
        } catch (Throwable ex){
            session.getRemote().sendString(new Gson().toJson(new Error("Error: " + ex.getMessage())));
        }
    }

    private void broadcast(String excludeRootUser, Notification notification) throws IOException {
        var removeList = new ArrayList<Connection>();
        if (!connections.isEmpty()) {
            for (var c : connections.values()) {
                if (c.session.isOpen()) {
                    if (!c.username.equals(excludeRootUser)) {
                        c.send(notification.toString());
                    }
                } else {
                    removeList.add(c);
                }
            }

            for (var c : removeList) {
                connections.remove(c.username);
            }
        }
    }

    @OnWebSocketMessage
    public void onMessage(Session session, String message) throws Exception {
        UserGameCommand action = new Gson().fromJson(message, UserGameCommand.class);

        var conn = getConnection();
        if (conn != null) {
            switch (action.getCommandType()) {
                case JOIN_PLAYER:
                    joinPlayer(new Gson().fromJson(message, JoinPlayer.class), session);
                    break;
                case JOIN_OBSERVER:
                    joinObserver(new Gson().fromJson(message, JoinObserver.class), session);
                    break;
            }
        } else {
            session.getRemote().sendString(new Gson().toJson(new Error("Error: unknown user")));
        }
    }
}
