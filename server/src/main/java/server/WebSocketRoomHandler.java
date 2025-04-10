package server;

import com.google.gson.Gson;
import org.eclipse.jetty.websocket.api.Session;
import websocket.messages.ServerMessage;

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

    public void broadcast(Integer gameId, Session excludedSession, ServerMessage serverMessage) throws Exception{
        ArrayList<Session> removeList = new ArrayList<>();
        for(Session session : rooms.get(gameId)){
            if(session.isOpen()){
                if(session!=excludedSession){
                    session.getRemote().sendString(new Gson().toJson(serverMessage));
                }
            }
            else{
                removeList.add(session);
            }
        }

        for(Session session : removeList){
            rooms.get(gameId).remove(session);
        }
    }
}
