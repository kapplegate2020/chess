package server;

import chess.ChessGame;
import com.google.gson.Gson;
import dataaccess.*;
import model.AuthData;
import model.GameData;
import model.UserData;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import websocket.commands.UserGameCommand;
import websocket.messages.LoadGameMessage;
import websocket.messages.ServerMessage;


@WebSocket
public class WSServer {
    UserDataAccess userDataAccess;
    GameDataAccess gameDataAccess;
    AuthDataAccess authDataAccess;
    WebSocketRoomHandler roomHandler = new WebSocketRoomHandler();

    public WSServer(UserDataAccess userDataAccess, GameDataAccess gameDataAccess, AuthDataAccess authDataAccess){
        this.userDataAccess = userDataAccess;
        this.gameDataAccess = gameDataAccess;
        this.authDataAccess = authDataAccess;
    }

    @OnWebSocketMessage
    public void onMessage(Session session, String message) throws Exception {
        UserGameCommand userGameCommand = new Gson().fromJson(message, UserGameCommand.class);
        AuthData authData = authDataAccess.getAuth(userGameCommand.getAuthToken());
        if (authData == null) {
            return;
        }
        UserData userData = userDataAccess.getUser(authData.username());
        if (userData == null) {
            return;
        }
        GameData gameData = gameDataAccess.getGame(userGameCommand.getGameID());
        System.out.println(userGameCommand.getGameID());
        if (gameData == null) {
            return;
        }

        switch (userGameCommand.getCommandType()) {
            case UserGameCommand.CommandType.CONNECT -> connect(userGameCommand, session, gameData.game());
            case UserGameCommand.CommandType.MAKE_MOVE -> makeMove(userGameCommand, session);
            case UserGameCommand.CommandType.LEAVE -> leave(userGameCommand, session);
            case UserGameCommand.CommandType.RESIGN -> resign(userGameCommand, session);
        }
        ;
    }

    private void connect(UserGameCommand userGameCommand, Session session, ChessGame game) throws Exception{
        roomHandler.add(userGameCommand.getGameID(), session);
//        roomHandler.broadcast(userGameCommand.getGameID(), session, );
        LoadGameMessage loadGameMessage = new LoadGameMessage(game);
        session.getRemote().sendString(new Gson().toJson(loadGameMessage));
    }

    private void makeMove(UserGameCommand userGameCommand, Session session){

    }

    private void leave(UserGameCommand userGameCommand, Session session){

    }

    private void resign(UserGameCommand userGameCommand, Session session){

    }

}
