package service;

import request.CreateGameRequest;
import request.JoinGameRequest;
import request.ListGamesRequest;
import result.CreateGameResult;
import result.JoinGameResult;
import result.ListGamesResult;
import chess.ChessGame;
import dataaccess.*;
import model.AuthData;
import model.GameData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class GameServiceTests {
    GameDataAccess gameDataAccess = new DbGameDataAccess();
    AuthDataAccess authDataAccess = new DbAuthDataAccess();
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
        assert listGamesResult.games().getFirst().gameID() == 4;
        assert listGamesResult.games().get(1).gameID() == 6;
    }

    @Test
    public void listGamesFailure(){
        String authToken = UserService.generateToken();
        ListGamesRequest listGamesRequest = new ListGamesRequest(authToken);
        ListGamesResult listGamesResult = gameService.listGames(listGamesRequest);
        assert listGamesResult.statusNumber() == 401;
        assert listGamesResult.games() == null;
        assert listGamesResult.message().equals("Error: unauthorized");
    }

    @Test
    public void createGameSuccess(){
        String authToken = UserService.generateToken();
        try{
            AuthData authData = new AuthData(authToken, "Sammy");
            authDataAccess.createAuth(authData);
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
        CreateGameRequest createGameRequest = new CreateGameRequest(authToken, "Stevie's Game");
        CreateGameResult createGameResult = gameService.createGame(createGameRequest);
        assert createGameResult.statusNumber() == 200;
        assert createGameResult.gameID() != null;
        try{
            GameData gameData = gameDataAccess.getGame(createGameResult.gameID());
            assert gameData != null;
            assert gameData.gameName().equals("Stevie's Game");
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void createGameFailure(){
        String authToken = UserService.generateToken();
        CreateGameRequest createGameRequest = new CreateGameRequest(authToken, "myGame");
        CreateGameResult createGameResult = gameService.createGame(createGameRequest);
        assert createGameResult.statusNumber() == 401;
        assert createGameResult.gameID() == null;
        assert createGameResult.message().equals("Error: unauthorized");
    }

    @Test
    public void joinGameSuccess(){
        String authToken = UserService.generateToken();
        try{
            AuthData authData = new AuthData(authToken, "Sammy");
            authDataAccess.createAuth(authData);
            GameData gameData = new GameData(0, null, "Jerry", "theGame", null);
            gameDataAccess.createGame(gameData);
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
        JoinGameRequest joinGameRequest = new JoinGameRequest(authToken, 0, ChessGame.TeamColor.WHITE);
        JoinGameResult joinGameResult = gameService.joinGame(joinGameRequest);
        assert joinGameResult.statusNumber() == 200;
        try{
            GameData gameData = gameDataAccess.getGame(0);
            assert gameData != null;
            assert gameData.gameName().equals("theGame");
            assert gameData.whiteUsername().equals("Sammy");
            assert gameData.blackUsername().equals("Jerry");
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void joinGameFailure(){
        String authToken = UserService.generateToken();
        try{
            AuthData authData = new AuthData(authToken, "Sammy");
            authDataAccess.createAuth(authData);
            GameData gameData = new GameData(543, "Sarah", null, "theCoolGame", null);
            gameDataAccess.createGame(gameData);
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
        JoinGameRequest joinGameRequest = new JoinGameRequest(authToken, 543, ChessGame.TeamColor.WHITE);
        JoinGameResult joinGameResult = gameService.joinGame(joinGameRequest);
        assert joinGameResult.statusNumber() == 403;
        try{
            GameData gameData = gameDataAccess.getGame(543);
            assert gameData != null;
            assert gameData.gameName().equals("theCoolGame");
            assert gameData.whiteUsername().equals("Sarah");
            assert gameData.blackUsername() == null;
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
    }

}
