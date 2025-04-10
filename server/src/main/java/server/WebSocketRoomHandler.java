package server;

import org.eclipse.jetty.websocket.api.Session;

import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

public class WebSocketRoomHandler {
    private ConcurrentHashMap<Integer, ArrayList<Session>> rooms = new ConcurrentHashMap<>();

    public void add(Integer gameID, Session session){
        if(!rooms.containsKey(gameID)){
            rooms.put(gameID, new ArrayList<>());
        }
        rooms.get(gameID).add(session);
    }

    public void remove(Integer gameId, Session session){
        rooms.get(gameId).remove(session);
    }
}
