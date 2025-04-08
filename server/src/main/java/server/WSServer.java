package server;

import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;

import javax.websocket.Session;

@WebSocket
public class WSServer {

    @OnWebSocketMessage
    public void onMessage(Session session, String message){

    }

}
