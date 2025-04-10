package websocket.messages;

import chess.ChessGame;

public class LoadGameMessage {
    private ServerMessage.ServerMessageType serverMessageType = ServerMessage.ServerMessageType.LOAD_GAME;
    final private ChessGame game;

    public LoadGameMessage(ChessGame game){
        this.game = game;
    }

    public ChessGame game(){
        return game;
    }
}
