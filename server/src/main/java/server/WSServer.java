package server;


import com.google.gson.Gson;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import webSocketMessages.serverMessages.Error;
import webSocketMessages.serverMessages.Notification;
import webSocketMessages.userCommands.JoinObserver;
import webSocketMessages.userCommands.JoinPlayer;
import webSocketMessages.userCommands.UserGameCommand;

import javax.websocket.Session;
import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

@WebSocket
public class WSServer {
    public final ConcurrentHashMap<String, Connection> connections = new ConcurrentHashMap<>();
    @OnWebSocketMessage
    public void onMessage(Session session, String message){
        try {
            UserGameCommand action = new Gson().fromJson(message, UserGameCommand.class);
            switch (action.getCommandType()) {
                case JOIN_PLAYER -> joinPlayer(new Gson().fromJson(message, JoinPlayer.class), session);
                case JOIN_OBSERVER -> joinObserver(new Gson().fromJson(message, JoinObserver.class), session);
            }
        } catch (IOException ex){
            Error error = new Error(ex.getMessage());
        }
    }

    private void joinPlayer(JoinPlayer action, Session session) throws IOException {
        Connection connection = new Connection(action.getUsername(), session);
        connections.put(action.getUsername(), connection);
        var message = action.getAuthString() + " joined " + action.getId() + " as " + action.getPlayerColor();
        var notification = new Notification(message);
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
