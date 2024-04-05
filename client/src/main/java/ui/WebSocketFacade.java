package ui;

import com.google.gson.Gson;
import webSocketMessages.serverMessages.ServerMessage;
import webSocketMessages.userCommands.UserGameCommand;

import javax.management.Notification;
import javax.websocket.*;
import java.net.URI;

public class WebSocketFacade {
    Session session;
    UserGameCommand commands;
    ServerMessage message;

    public WebSocketFacade(String url){
        try{
            url = url.replace("http", "ws");
            URI socketURI = new URI(url + "/connect");

            WebSocketContainer container = ContainerProvider.getWebSocketContainer();
            this.session = container.connectToServer(this, socketURI);

            this.session.addMessageHandler(new MessageHandler() {
                @Override
                public void onMessage(String message){
                    Notification notification = new Gson().fromJson(message, Notification.class);
                    
                }
            });

        } catch (Throwable e){

        }
    }
}
