package server;


import com.google.gson.Gson;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import webSocketMessages.userCommands.JoinObserver;
import webSocketMessages.userCommands.JoinPlayer;
import webSocketMessages.userCommands.UserGameCommand;

import javax.websocket.Session;
import java.io.IOException;

@WebSocket
public class WSServer {
    @OnWebSocketMessage
    public void onMessage(Session session, String message) throws IOException {
        UserGameCommand action = new Gson().fromJson(message, UserGameCommand.class);
        switch (action.getCommandType()){
            case JOIN_PLAYER -> joinPlayer(action, session);
            case JOIN_OBSERVER -> joinObserver(action, session);
        }
    }

    private void joinPlayer(JoinPlayer action, Session session){

    }

    private void joinObserver(JoinObserver action, Session session){

    }
}
