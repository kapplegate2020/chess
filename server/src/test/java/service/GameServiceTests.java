package service;

import Request.ListGamesRequest;
import Result.ListGamesResult;
import dataAccess.*;
import model.AuthData;
import model.GameData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class GameServiceTests {
    GameDataAccess gameDataAccess = new MemoryGameDataAccess();
    AuthDataAccess authDataAccess = new MemoryAuthDataAccess();
    GameService gameService = new GameService(gameDataAccess, authDataAccess);

    @BeforeEach
    public void resetDataAccess(){
        try {
            gameDataAccess.clear();
            authDataAccess.clear();
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void listGamesSuccess(){
        String authToken = UserService.generateToken();
        try{
            AuthData authData = new AuthData(authToken, "Sammy");
            authDataAccess.createAuth(authData);
            GameData gameData1 = new GameData(4, "user1", null, "game", null);
            GameData gameData2 = new GameData(6, null, "Gerald", "alsoagame", null);
            gameDataAccess.createGame(gameData1);
            gameDataAccess.createGame(gameData2);
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
        ListGamesRequest listGamesRequest = new ListGamesRequest(authToken);
        ListGamesResult listGamesResult = gameService.listGames(listGamesRequest);
        assert listGamesResult.statusNumber() == 200;
        assert listGamesResult.gameList().getFirst().gameID() == 4;
        assert listGamesResult.gameList().get(1).gameID() == 6;
    }

    @Test
    public void listGamesFailure(){
        String authToken = UserService.generateToken();
        ListGamesRequest listGamesRequest = new ListGamesRequest(authToken);
        ListGamesResult listGamesResult = gameService.listGames(listGamesRequest);
        assert listGamesResult.statusNumber() == 401;
        assert listGamesResult.gameList() == null;
        assert listGamesResult.message().equals("Error: unauthorized");
    }

}
