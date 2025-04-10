package client;

import chess.ChessGame;
import com.google.gson.Gson;
import ui.DrawGame;
import ui.GameClient;
import websocket.messages.ErrorMessage;
import websocket.messages.LoadGameMessage;
import websocket.messages.NotificationMessage;
import websocket.messages.ServerMessage;

public class NotificationHandler {
    ChessGame.TeamColor viewpoint;
    GameClient gameClient;

    public NotificationHandler(GameClient gameClient, ChessGame.TeamColor viewPoint){
        this.gameClient = gameClient;
        this.viewpoint = viewPoint;
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
        NotificationMessage notificationMessage = new Gson().fromJson(message, NotificationMessage.class);
        System.out.println();
        System.out.println(notificationMessage.message());
        System.out.print("[IN GAME] >>> ");
    }

    private void error(String message){
        ErrorMessage errorMessage = new Gson().fromJson(message, ErrorMessage.class);
        System.out.println();
        System.out.println(errorMessage.errorMessage());
        System.out.print("[IN GAME] >>> ");
    }

    private void load(String message){
        LoadGameMessage loadGameMessage = new Gson().fromJson(message, LoadGameMessage.class);
        System.out.println();
        DrawGame drawGame = new DrawGame(loadGameMessage.game(), viewpoint);
        drawGame.draw(null);
        gameClient.updateChessGame(loadGameMessage.game());
        System.out.print("[IN GAME] >>> ");
    }
}
