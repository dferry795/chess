package ui;

import chess.ChessGame;
import com.google.gson.Gson;
import webSocketMessages.serverMessages.ServerMessage;
import webSocketMessages.userCommands.JoinPlayer;
import javax.websocket.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

public class WebSocketFacade extends Endpoint{
    Session session;
    String authToken;

    public WebSocketFacade(String url, String authToken) throws DeploymentException, URISyntaxException, IOException {
            url = url.replace("http", "ws");
            URI socketURI = new URI(url + "/connect");

            WebSocketContainer container = ContainerProvider.getWebSocketContainer();
            this.session = container.connectToServer(this, socketURI);

            this.session.addMessageHandler(new MessageHandler.Whole<String>() {
                @Override
                public void onMessage(String message) {
                    ServerMessage serverMessage = new Gson().fromJson(message, ServerMessage.class);
                    System.out.println(serverMessage.toString());
                }
            });

    }

    public void joinPlayer(String authToken, String username, ChessGame.TeamColor color, int id) {
        if (session.isOpen()) {
            JoinPlayer joinPlayer = new JoinPlayer(authToken, username, color, String.valueOf(id));
            try {
                this.session.getBasicRemote().sendText(new Gson().toJson(joinPlayer));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } else {
            throw new RuntimeException("Error: Websocket connection is not open");
        }
    }

    @Override
    public void onOpen(Session session, EndpointConfig endpointConfig) {
    }
}
