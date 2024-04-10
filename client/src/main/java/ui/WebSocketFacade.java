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

public class WebSocketFacade extends Endpoint{
    Session session;
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
                    ServerMessage serverMessage = new Gson().fromJson(message, ServerMessage.class);
                    observer.notify(serverMessage);
                }
            });

            this.authToken = authToken;
        } catch (Throwable e) {
            throw e;
        }

    }

    public void joinPlayer(String authToken, String username, String color, int id){
        JoinPlayer joinPlayer = new JoinPlayer(authToken, username, color, String.valueOf(id));
        try {
            this.session.getBasicRemote().sendText(new Gson().toJson(joinPlayer));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void onOpen(Session session, EndpointConfig endpointConfig) {
    }
}
