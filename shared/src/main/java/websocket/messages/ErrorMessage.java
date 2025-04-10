package websocket.messages;

import chess.ChessGame;

public class ErrorMessage {
    final private ServerMessage.ServerMessageType serverMessageType = ServerMessage.ServerMessageType.ERROR;
    final private String errorMessage;

    public ErrorMessage(String errorMessage){
        this.errorMessage = errorMessage;
    }

    public String errorMessage(){
        return errorMessage;
    }
}
