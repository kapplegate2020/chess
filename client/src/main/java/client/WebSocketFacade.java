package client;

import javax.websocket.*;
import java.net.URI;
import java.net.URISyntaxException;

public class WebSocketFacade extends Endpoint {
    Session session;

    public WebSocketFacade(String url){
        try {
            url = url.replace("http", "ws");
            URI socketURI = new URI(url + "ws");
            WebSocketContainer container = ContainerProvider.getWebSocketContainer();
            this.session = container.connectToServer(this, socketURI);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void onOpen(Session session, EndpointConfig endpointConfig){}
}
