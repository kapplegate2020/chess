package dataaccess;

import model.GameData;
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
            assert gameData.gameName().equals("funGame");
            assert gameData.blackUsername() == null;
            assert gameData.game() == null;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
