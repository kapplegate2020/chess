package client;

import com.google.gson.Gson;
import websocket.messages.LoadGameMessage;
import websocket.messages.ServerMessage;

public class NotificationHandler {
    public void notify(String message){
        ServerMessage serverMessage = new Gson().fromJson(message, ServerMessage.class);
        switch (serverMessage.getServerMessageType()) {
            case ServerMessage.ServerMessageType.NOTIFICATION -> notification(message);
            case ServerMessage.ServerMessageType.ERROR -> error(message);
            case ServerMessage.ServerMessageType.LOAD_GAME -> load(message);
        };
    }

    private void notification(String message){

    }

    private void error(String message){

    }

    private void load(String message){

    }
}
