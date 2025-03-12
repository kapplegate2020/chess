package dataaccess;

import model.GameData;
import model.UserData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

public class GameDataAccessTest {
    GameDataAccess gameDataAccess = new DbGameDataAccess();

    @BeforeEach
    public void clearDB(){
        try {
            gameDataAccess.clear();
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void getGameSuccess(){
        try (var conn = DatabaseManager.getConnection()) {
            String statement = "INSERT INTO game (gameID, whiteUsername, gameName) VALUES (4, 'Sam', 'funGame')";
            try (var insertPreparedStatement = conn.prepareStatement(statement)) {
                insertPreparedStatement.executeUpdate();
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        try{
            GameData gameData = gameDataAccess.getGame(4);
            assert gameData.gameID() == 4;
            assert gameData.whiteUsername().equals("Sam");
            assert gameData.blackUsername() == null;
            assert gameData.gameName().equals("funGame");
            assert gameData.game() == null;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void getGameFailure(){
        try{
            GameData gameData = gameDataAccess.getGame(13);
            assert gameData == null;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void createGameSuccess(){
        GameData gameData = new GameData(23, null, "Sid", "coolGame", null);
        try {
            gameDataAccess.createGame(gameData);
            GameData returnedGameData = gameDataAccess.getGame(23);
            assert gameData.gameID() == 23;
            assert gameData.whiteUsername() == null;
            assert gameData.blackUsername().equals("Sid");
            assert gameData.gameName().equals("coolGame");
            assert gameData.game() == null;
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
    }


    @Test
    public void createGameFailure(){
        GameData gameData = new GameData(15, null, "Seth", "coolGame2", null);
        String errorMessage = "";
        try {
            gameDataAccess.createGame(gameData);
        } catch (DataAccessException ex) {
            throw new RuntimeException(ex);
        }

        gameData = new GameData(15, "Steven", null, "notCoolGame2", null);
        try{
            gameDataAccess.createGame(gameData);
        } catch (DataAccessException e) {
            errorMessage = e.getMessage();
        }
        finally {
            assert errorMessage.equals("GameID is already taken.");
        }
    }


    @Test
    public void clear(){
        GameData gameData = new GameData(111, "Simon", null, "thisName", null);
        try {
            gameDataAccess.createGame(gameData);
            GameData returnedGameData = gameDataAccess.getGame(111);
            assert returnedGameData != null;
            gameDataAccess.clear();
            returnedGameData = gameDataAccess.getGame(111);
            assert returnedGameData == null;
        } catch (DataAccessException ex) {
            throw new RuntimeException(ex);
        }
    }

    @Test
    public void updateGameSuccess(){
        GameData gameData = new GameData(213, "Sean", null, "gameName", null);
        GameData gameDataUpdate = new GameData(213, "Sean", "Silas", "gameName", null);
        try {
            gameDataAccess.createGame(gameData);
            GameData returnedGameData = gameDataAccess.getGame(213);
            assert returnedGameData.blackUsername() == null;
            gameDataAccess.updateGame(gameDataUpdate);
            returnedGameData = gameDataAccess.getGame(213);
            assert returnedGameData.blackUsername().equals("Silas");
        } catch (DataAccessException ex) {
            throw new RuntimeException(ex);
        }
    }

    @Test
    public void updateGameFailure(){
        GameData gameData = new GameData(314, "Sebus", null, "betterGameName", null);
        try {
            gameDataAccess.updateGame(gameData);
        } catch (DataAccessException ex) {
            assert ex.getMessage().equals("GameID does not exist.");
        }
    }

    @Test
    public void listGamesSuccess(){
        GameData gameData1 = new GameData(334, null, null, "coolGame1", null);
        GameData gameData2 = new GameData(341, null, "Shiblon", "coolGame2", null);
        try {
            gameDataAccess.createGame(gameData1);
            gameDataAccess.createGame(gameData2);
            ArrayList<GameData> games = gameDataAccess.listGames();
            assert games.getFirst().gameID() == 334;
            assert games.getFirst().whiteUsername() == null;
            assert games.getFirst().blackUsername() == null;
            assert games.getFirst().gameName().equals("coolGame1");
            assert games.get(1).gameID() == 341;
            assert games.get(1).whiteUsername() == null;
            assert games.get(1).blackUsername().equals("Shiblon");
            assert games.get(1).gameName().equals("coolGame2");
        } catch (DataAccessException ex) {
            throw new RuntimeException(ex);
        }
    }

    @Test
    public void listGamesFailure(){
        try {
            ArrayList<GameData> games = gameDataAccess.listGames();
            assert games == null;
        } catch (DataAccessException ex) {
            throw new RuntimeException(ex);
        }
    }
}
