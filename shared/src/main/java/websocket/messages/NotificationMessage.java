package websocket.messages;

public class NotificationMessage {
    final private ServerMessage.ServerMessageType serverMessageType = ServerMessage.ServerMessageType.NOTIFICATION;
    final private String message;

    public NotificationMessage(String errorMessage){
        this.message = errorMessage;
    }

    public String message(){
        return message;
    }
}
