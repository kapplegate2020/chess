package dataaccess;

import model.GameData;
import model.UserData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

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
    public void createUserFailure(){
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
}
