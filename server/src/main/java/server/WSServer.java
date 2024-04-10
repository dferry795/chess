package server;


import com.google.gson.Gson;
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
import webSocketMessages.userCommands.UserGameCommand;

import org.eclipse.jetty.websocket.api.Session;
import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

import static java.sql.DriverManager.getConnection;

@WebSocket
public class WSServer {
    public final ConcurrentHashMap<String, Connection> connections = new ConcurrentHashMap<>();
    public final GameDataInterface gameDataInterface = new SqlGameDOA();
    @OnWebSocketMessage
    public void onMessage(Session session, String message) throws Exception {
            UserGameCommand action = new Gson().fromJson(message, UserGameCommand.class);
            var conn = getConnection(action.getAuthString(), session);

            if (conn != null) {
                switch (action.getCommandType()) {
                    case JOIN_PLAYER:
                        joinPlayer(new Gson().fromJson(message, JoinPlayer.class), session);
                        break;
                    case JOIN_OBSERVER:
                        joinObserver(new Gson().fromJson(message, JoinObserver.class), session);
                        break;
                }
            }
    }

    @OnWebSocketError
    public void onError(Session session, Throwable ex){
        Error error = new Error(ex.getMessage());
    }

    private void joinPlayer(JoinPlayer action, Session session) throws IOException {
        Connection connection = new Connection(action.getUsername(), session);
        connections.put(action.getUsername(), connection);
        var message = action.getAuthString() + " joined " + action.getId() + " as " + action.getPlayerColor();
        var notification = new Notification(message);
        LoadGame loadGame = new LoadGame(gameDataInterface.getGame(Integer.parseInt(action.getId())).game());
        session.getRemote().sendString(new Gson().toJson(loadGame));
        broadcast(action.getUsername(), notification);
    }

    private void joinObserver(JoinObserver action, Session session) throws IOException {
        Connection connection = new Connection(action.getUsername(), session);
        connections.put(action.getUsername(), connection);
        var message = action.getAuthString() + " joined " + action.getId() + " as an observer";
        var notification = new Notification(message);
        broadcast(action.getUsername(), notification);
    }

    private void broadcast(String excludeRootUser, Notification notification) throws IOException {
        var removeList = new ArrayList<Connection>();
        for (var c : connections.values()){
            if (c.session.isOpen()){
                if (!c.username.equals(excludeRootUser)){
                    c.send(notification.toString());
                }
            } else {
                removeList.add(c);
            }
        }

        for (var c : removeList){
            connections.remove(c.username);
        }
    }
}
