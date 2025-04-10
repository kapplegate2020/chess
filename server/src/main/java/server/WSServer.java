package server;

import chess.ChessGame;
import chess.ChessMove;
import chess.ChessPiece;
import chess.InvalidMoveException;
import com.google.gson.Gson;
import dataaccess.*;
import model.AuthData;
import model.GameData;
import model.UserData;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import websocket.commands.UserGameCommand;
import websocket.messages.ErrorMessage;
import websocket.messages.LoadGameMessage;
import websocket.messages.NotificationMessage;
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
            ErrorMessage errorMessage = new ErrorMessage("Error: not authenticated.");
            session.getRemote().sendString(new Gson().toJson(errorMessage));
            return;
        }
        UserData userData = userDataAccess.getUser(authData.username());
        if (userData == null) {
            ErrorMessage errorMessage = new ErrorMessage("Error: invalid username.");
            session.getRemote().sendString(new Gson().toJson(errorMessage));
            return;
        }
        GameData gameData = gameDataAccess.getGame(userGameCommand.getGameID());
        if (gameData == null) {
            ErrorMessage errorMessage = new ErrorMessage("Error: invalid gameID.");
            session.getRemote().sendString(new Gson().toJson(errorMessage));
            return;
        }

        switch (userGameCommand.getCommandType()) {
            case UserGameCommand.CommandType.CONNECT -> connect(userGameCommand, session, gameData, userData.username());
            case UserGameCommand.CommandType.MAKE_MOVE -> makeMove(userGameCommand, session, gameData, userData.username());
            case UserGameCommand.CommandType.LEAVE -> leave(userGameCommand, session, gameData, userData.username());
            case UserGameCommand.CommandType.RESIGN -> resign(userGameCommand, session, gameData, userData.username());
        }
        ;
    }

    private void connect(UserGameCommand userGameCommand, Session session, GameData gameData, String username) throws Exception{
        roomHandler.add(userGameCommand.getGameID(), session);
        LoadGameMessage loadGameMessage = new LoadGameMessage(gameData.game());
        session.getRemote().sendString(new Gson().toJson(loadGameMessage));

        NotificationMessage joinMessage;
        if(username.equals(gameData.whiteUsername())){
            joinMessage = new NotificationMessage(username + " has joined the game as white.");
        }
        else if(username.equals(gameData.blackUsername())){
            joinMessage = new NotificationMessage(username + " has joined the game as black.");
        }
        else{
            joinMessage = new NotificationMessage(username + " has joined the game as an observer.");
        }
        roomHandler.broadcast(userGameCommand.getGameID(), session, joinMessage);
    }

    private void makeMove(UserGameCommand userGameCommand, Session session, GameData gameData, String username) throws Exception{
        ChessGame game = gameData.game();
        boolean checkMate = false;
        boolean staleMate = false;
        boolean check = false;
        String opponent;
        if(username.equals(gameData.whiteUsername())){
            if(game.getTeamTurn()!= ChessGame.TeamColor.WHITE){
                ErrorMessage errorMessage = new ErrorMessage("Error: it is not your turn.");
                session.getRemote().sendString(new Gson().toJson(errorMessage));
                return;
            }
            try{
                game.makeMove(userGameCommand.getMove());
            } catch (InvalidMoveException e) {
                ErrorMessage errorMessage = new ErrorMessage("Error: "+e.getMessage());
                session.getRemote().sendString(new Gson().toJson(errorMessage));
                return;
            }
            checkMate = game.isInCheckmate(ChessGame.TeamColor.BLACK);
            staleMate = game.isInStalemate(ChessGame.TeamColor.BLACK);
            check = game.isInCheck(ChessGame.TeamColor.BLACK);
            opponent = gameData.blackUsername();
        }
        else if(username.equals(gameData.blackUsername())){
            if(game.getTeamTurn()!= ChessGame.TeamColor.BLACK){
                ErrorMessage errorMessage = new ErrorMessage("Error: it is not your turn.");
                session.getRemote().sendString(new Gson().toJson(errorMessage));
                return;
            }
            try{
                game.makeMove(userGameCommand.getMove());
            } catch (InvalidMoveException e) {
                ErrorMessage errorMessage = new ErrorMessage("Error: "+e.getMessage());
                session.getRemote().sendString(new Gson().toJson(errorMessage));
                return;
            }
            checkMate = game.isInCheckmate(ChessGame.TeamColor.WHITE);
            staleMate = game.isInStalemate(ChessGame.TeamColor.WHITE);
            check = game.isInCheck(ChessGame.TeamColor.WHITE);
            opponent = gameData.whiteUsername();
        }
        else{
            ErrorMessage errorMessage = new ErrorMessage("Error: Observers cannot make moves.");
            session.getRemote().sendString(new Gson().toJson(errorMessage));
            return;
        }
        gameData = gameData.updateGame(game);
        gameDataAccess.updateGame(gameData);
        LoadGameMessage loadGameMessage = new LoadGameMessage(game);
        roomHandler.broadcast(userGameCommand.getGameID(), null, loadGameMessage);
        NotificationMessage moveMessage = new NotificationMessage(generateMoveMessage(username, userGameCommand.getMove()));
        roomHandler.broadcast(userGameCommand.getGameID(), session, moveMessage);

        if(checkMate){
            NotificationMessage message = new NotificationMessage(opponent+" is in checkmate.");
            roomHandler.broadcast(userGameCommand.getGameID(), null, message);
        }
        else if(staleMate){
            NotificationMessage message = new NotificationMessage(opponent+" is in stalemate.");
            roomHandler.broadcast(userGameCommand.getGameID(), null, message);
        }
        else if(check){
            NotificationMessage message = new NotificationMessage(opponent+" is in check.");
            roomHandler.broadcast(userGameCommand.getGameID(), null, message);
        }
    }

    private void leave(UserGameCommand userGameCommand, Session session, GameData gameData, String username) throws Exception{
        roomHandler.remove(gameData.gameID(), session);
        if(username.equals(gameData.whiteUsername())){
            gameData = gameData.addWhiteUsername(null);
            gameDataAccess.updateGame(gameData);
        }
        else if(username.equals(gameData.blackUsername())){
            gameData = gameData.addBlackUsername(null);
            gameDataAccess.updateGame(gameData);
        }
        NotificationMessage leaveMessage = new NotificationMessage(username+" has left the game.");
        roomHandler.broadcast(userGameCommand.getGameID(), session, leaveMessage);

    }

    private void resign(UserGameCommand userGameCommand, Session session, GameData gameData, String username) throws Exception{
        ChessGame game = gameData.game();
        if(game.isResigned()){
            ErrorMessage errorMessage = new ErrorMessage("Error: Game is already over.");
            session.getRemote().sendString(new Gson().toJson(errorMessage));
            return;
        }
        if(username.equals(gameData.whiteUsername())){
            game.resign();
            gameData = gameData.updateGame(game);
            gameDataAccess.updateGame(gameData);
        }
        else if(username.equals(gameData.blackUsername())){
            game.resign();
            gameData = gameData.updateGame(game);
            gameDataAccess.updateGame(gameData);
        }
        else{
            ErrorMessage errorMessage = new ErrorMessage("Error: Observers cannot resign.");
            session.getRemote().sendString(new Gson().toJson(errorMessage));
            return;
        }
        NotificationMessage resignMessage = new NotificationMessage(username+" has resigned.");
        roomHandler.broadcast(userGameCommand.getGameID(), null, resignMessage);
    }

    private String generateMoveMessage(String username, ChessMove move){
        String[] letters = {"A", "B", "C", "D", "E", "F", "G", "H"};
        String moveStr = username + " moved a piece from ";
        moveStr += letters[move.getStartPosition().getColumn()-1]+move.getStartPosition().getRow();
        moveStr += " to ";
        moveStr += letters[move.getEndPosition().getColumn()-1]+move.getEndPosition().getRow();
        return moveStr+".";


    }

}
