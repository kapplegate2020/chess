package service;

import request.CreateGameRequest;
import request.JoinGameRequest;
import request.ListGamesRequest;
import result.CreateGameResult;
import result.JoinGameResult;
import result.ListGamesResult;
import chess.ChessGame;
import dataAccess.AuthDataAccess;
import dataAccess.DataAccessException;
import dataAccess.GameDataAccess;
import model.AuthData;
import model.GameData;

import java.util.ArrayList;
import java.util.Random;

public class GameService {
    private final GameDataAccess gameDataAccess;
    private final AuthDataAccess authDataAccess;
    Random random = new Random();

    public GameService(GameDataAccess gameDataAccess, AuthDataAccess authDataAccess){
        this.gameDataAccess = gameDataAccess;
        this.authDataAccess = authDataAccess;
    }

    public ListGamesResult listGames(ListGamesRequest listGamesRequest){
        try{
            String authToken = listGamesRequest.authToken();
            AuthData authData = authDataAccess.getAuth(authToken);
            if(authData == null){
                return new ListGamesResult(null, 401, "Error: unauthorized");
            }
            ArrayList<GameData> gameList = gameDataAccess.listGames();
            return new ListGamesResult(gameList, 200, null);
        } catch (DataAccessException e) {
            return new ListGamesResult(null, 500, e.getMessage());
        }
    }

    public CreateGameResult createGame(CreateGameRequest createGameRequest){
        try{
            String authToken = createGameRequest.authToken();
            AuthData authData = authDataAccess.getAuth(authToken);
            if(authData == null){
                return new CreateGameResult(null, 401, "Error: unauthorized");
            }
            int gameID = random.nextInt(1000000);
            while (gameDataAccess.getGame(gameID)!=null){
                gameID = random.nextInt(1000000);
            }
            GameData gameData = new GameData(gameID, null, null, createGameRequest.gameName(), null);
            gameDataAccess.createGame(gameData);
            return new CreateGameResult(gameID, 200, null);
        } catch (DataAccessException e) {
            return new CreateGameResult(null, 500, e.getMessage());
        }
    }

    public JoinGameResult joinGame(JoinGameRequest joinGameRequest){
        if(joinGameRequest.gameID()==null){
            return new JoinGameResult(400, "Error: bad request");
        }
        try{
            String authToken = joinGameRequest.authToken();
            AuthData authData = authDataAccess.getAuth(authToken);
            if(authData == null){
                return new JoinGameResult(401, "Error: unauthorized");
            }
            GameData gameData = gameDataAccess.getGame(joinGameRequest.gameID());
            if(gameData == null){
                return new JoinGameResult(400, "Error: bad request");
            }
            ChessGame.TeamColor playerColor = joinGameRequest.playerColor();
            if(playerColor!= ChessGame.TeamColor.BLACK && playerColor != ChessGame.TeamColor.WHITE){
                return new JoinGameResult(400, "Error: bad request");
            }
            if(playerColor == ChessGame.TeamColor.BLACK && gameData.blackUsername()==null){
                gameData = gameData.addBlackUsername(authData.username());
                gameDataAccess.updateGame(gameData);
                return new JoinGameResult(200, null);
            }
            if(playerColor == ChessGame.TeamColor.WHITE && gameData.whiteUsername()==null){
                gameData = gameData.addWhiteUsername(authData.username());
                gameDataAccess.updateGame(gameData);
                return new JoinGameResult(200, null);
            }
            return new JoinGameResult(403, "Error: already taken");
        } catch (DataAccessException e) {
            return new JoinGameResult(500, e.getMessage());
        }
    }
}
