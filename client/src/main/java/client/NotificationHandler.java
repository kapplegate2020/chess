package client;

import chess.ChessGame;
import com.google.gson.Gson;
import ui.DrawGame;
import websocket.messages.LoadGameMessage;
import websocket.messages.ServerMessage;

public class NotificationHandler {
    ChessGame.TeamColor viewpoint;

    public NotificationHandler(ChessGame.TeamColor viewpoint){
        this.viewpoint = viewpoint;
    }

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
        LoadGameMessage loadGameMessage = new Gson().fromJson(message, LoadGameMessage.class);
        System.out.println();
        DrawGame drawGame = new DrawGame(loadGameMessage.game(), viewpoint);
        drawGame.draw();
    }
}
