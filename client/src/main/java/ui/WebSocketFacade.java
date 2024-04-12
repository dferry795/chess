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
    String url;

    public WebSocketFacade(String url, String authToken) throws DeploymentException, URISyntaxException, IOException {
            this.url = url;
            this.authToken = authToken;
    }

    public void joinPlayer(String authToken, ChessGame.TeamColor color, int id) throws IOException {
        if (session.isOpen()) {
            JoinPlayer joinPlayer = new JoinPlayer(id, authToken, color);
            this.session.getBasicRemote().sendText(new Gson().toJson(joinPlayer));
        } else {
            throw new RuntimeException("Error: Websocket connection is not open");
        }
    }

    @Override
    public void onOpen(Session session, EndpointConfig endpointConfig) {
        url = this.url.replace("http", "ws");
        URI socketURI = null;
        try {
            socketURI = new URI(url + "/connect");
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }

        WebSocketContainer container = ContainerProvider.getWebSocketContainer();
        try {
            this.session = container.connectToServer(this, socketURI);
        } catch (DeploymentException | IOException e) {
            throw new RuntimeException(e);
        }

        this.session.addMessageHandler(new MessageHandler.Whole<String>() {
            @Override
            public void onMessage(String message) {
                ServerMessage serverMessage = new Gson().fromJson(message, ServerMessage.class);
                System.out.println(serverMessage.toString());
            }
        });
    }
}
