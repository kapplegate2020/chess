package service;

import Request.ListGamesRequest;
import Result.ListGamesResult;
import dataAccess.AuthDataAccess;
import dataAccess.DataAccessException;
import dataAccess.GameDataAccess;
import dataAccess.UserDataAccess;
import model.AuthData;
import model.GameData;

import java.util.ArrayList;

public class GameService {
    private final GameDataAccess gameDataAccess;
    private final AuthDataAccess authDataAccess;

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
}
