package server;

import com.google.gson.Gson;
import dataaccess.*;
import model.AuthData;
import model.GameData;
import model.UserData;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import websocket.commands.UserGameCommand;
import websocket.messages.ServerMessage;


@WebSocket
public class WSServer {
    UserDataAccess userDataAccess;
    GameDataAccess gameDataAccess;
    AuthDataAccess authDataAccess;

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
            session.getRemote().sendString("");
            return;
        }
        UserData userData = userDataAccess.getUser(authData.username());
        if (userData == null) {
            session.getRemote().sendString("");
            return;
        }
        GameData gameData = gameDataAccess.getGame(userGameCommand.getGameID());
        if (gameData == null) {
            session.getRemote().sendString("");
            return;
        }

        switch (userGameCommand.getCommandType()) {
            case UserGameCommand.CommandType.CONNECT -> connect(userGameCommand);
            case UserGameCommand.CommandType.MAKE_MOVE -> makeMove(userGameCommand);
            case UserGameCommand.CommandType.LEAVE -> leave(userGameCommand);
            case UserGameCommand.CommandType.RESIGN -> resign(userGameCommand);
        }
        ;
    }

    private void connect(UserGameCommand userGameCommand){

    }

    private void makeMove(UserGameCommand userGameCommand){

    }

    private void leave(UserGameCommand userGameCommand){

    }

    private void resign(UserGameCommand userGameCommand){

    }

}
