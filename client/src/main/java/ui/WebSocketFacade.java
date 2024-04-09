package ui;

import com.google.gson.Gson;
import com.sun.nio.sctp.NotificationHandler;
import webSocketMessages.serverMessages.Notification;
import webSocketMessages.serverMessages.ServerMessage;
import webSocketMessages.userCommands.JoinPlayer;
import webSocketMessages.userCommands.UserGameCommand;

import javax.swing.*;
import javax.websocket.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

public class WebSocketFacade {
    Session session;
    UserGameCommand commands;
    NotificationHandler notificationHandler;
    ServerMessage message;
    public final ConcurrentHashMap<String, Connection> connections = new ConcurrentHashMap<>();

    String authToken;

    public WebSocketFacade(String url, String authToken) throws DeploymentException, URISyntaxException, IOException {
        try {
            url = url.replace("http", "ws");
            URI socketURI = new URI(url + "/connect");

            WebSocketContainer container = ContainerProvider.getWebSocketContainer();
            this.session = container.connectToServer(this, socketURI);

            this.session.addMessageHandler(new MessageHandler.Whole<String>() {
                @Override
                public void onMessage(String message) {
                    Notification notification = new Gson().fromJson(message, Notification.class);
                    System.out.println(notification);
                }
            });

            this.authToken = authToken;
        } catch (Throwable e) {
            throw e;
        }

    }


    public void onOpen(Session session, Endpoint endpointConfig){
    }

    public void joinBlack(String username, int id){
        try {
            var message = username + " joined as player black";
            var notification = new Notification(message);

        } catch (Throwable ex){

        }
    }

    public void broadcast(String excludeVisitorName, Notification notification){
        var removeList = new ArrayList<Connection>();
        for (var c : connections.values()){
            if (c.)
        }
    }
}
