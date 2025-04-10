package client;

import com.google.gson.Gson;
import websocket.commands.UserGameCommand;

import javax.websocket.*;
import java.net.URI;
import java.net.URISyntaxException;

public class WebSocketFacade extends Endpoint {
    Session session;

    public WebSocketFacade(String url){
        try {
            url = url.replace("http", "ws");
            URI socketURI = new URI(url + "/ws");
            WebSocketContainer container = ContainerProvider.getWebSocketContainer();
            this.session = container.connectToServer(this, socketURI);

            this.session.addMessageHandler(new MessageHandler.Whole<String>() {
                public void onMessage(String message) {
                    System.out.println(message);
                }
            });
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void connect(String authToken, int gameID) throws Exception{
        UserGameCommand userGameCommand = new UserGameCommand(UserGameCommand.CommandType.CONNECT, authToken, gameID);
        this.session.getBasicRemote().sendText(new Gson().toJson(userGameCommand));
    }

    @Override
    public void onOpen(Session session, EndpointConfig endpointConfig){}
}
