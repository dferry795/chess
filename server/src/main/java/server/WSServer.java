package server;


import chess.ChessGame;
import chess.ChessMove;
import com.google.gson.Gson;
import dataAccess.*;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketConnect;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketError;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import service.GameService;
import webSocketMessages.serverMessages.Error;
import webSocketMessages.serverMessages.LoadGame;
import webSocketMessages.serverMessages.Notification;
import webSocketMessages.userCommands.*;

import static dataAccess.DatabaseManager.getConnection;

import org.eclipse.jetty.websocket.api.Session;


import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static java.sql.DriverManager.getConnection;

@WebSocket
public class WSServer {
    public final HashMap<String, Connection> connections = new HashMap<>();
    public final GameService gameService = new GameService(new SqlAuthDOA(), new SqlGameDOA());
    public final GameDataInterface gameDataInterface = new SqlGameDOA();

    private void joinPlayer(JoinPlayer action, Session session) throws IOException {
        try {
            session.getRemote().sendString(new Gson().toJson(new Error("Error")));
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
            session.getRemote().sendString(new Gson().toJson(new Error("Error")));
        } catch (Throwable ex){
            session.getRemote().sendString(new Gson().toJson(new Error("Error: " + ex.getMessage())));
        }
    }

    public void resign(Resign action, Session session) throws IOException {
        try {
            session.getRemote().sendString(new Gson().toJson(new Error("Error")));
        } catch (Throwable ex){
            session.getRemote().sendString(new Gson().toJson(new Error("Error")));
        }
    }

    public void leave(Leave action, Session session) throws IOException {
        try {
            session.getRemote().sendString(new Gson().toJson(new Error("Error")));
        } catch (Throwable ex){
            session.getRemote().sendString(new Gson().toJson(new Error("Error")));
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

        switch (action.getCommandType()) {
            case JOIN_PLAYER:
                joinPlayer(new Gson().fromJson(message, JoinPlayer.class), session);
                break;
            case JOIN_OBSERVER:
                joinObserver(new Gson().fromJson(message, JoinObserver.class), session);
                break;
            case MAKE_MOVE:
                makeMove(new Gson().fromJson(message, MakeMove.class), session);
                break;
            case LEAVE:
                leave(new Gson().fromJson(message, Leave.class), session);
                break;
            case RESIGN:
                resign(new Gson().fromJson(message, Resign.class), session);
                break;
        }
    }
}
