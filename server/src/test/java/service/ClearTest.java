package service;

import dataaccess.*;
import model.AuthData;
import model.GameData;
import model.UserData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class ClearTest {
    UserDataAccess userDataAccess = new DbUserDataAccess();
    GameDataAccess gameDataAccess = new DbGameDataAccess();
    AuthDataAccess authDataAccess = new DbAuthDataAccess();
    ClearService clearService = new ClearService(userDataAccess, gameDataAccess, authDataAccess);

    @BeforeEach
    public void resetDataAccess(){
        try {
            userDataAccess.clear();
            gameDataAccess.clear();
            authDataAccess.clear();
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void clearTest(){
        String authToken = UserService.generateToken();
        try{
            UserData userData = new UserData("user123", "pass1234", "fake@fake.org");
            userDataAccess.createUser(userData);
            AuthData authData = new AuthData(authToken, "coolUser");
            authDataAccess.createAuth(authData);
            GameData gameData = new GameData(43, "Jimm", null, "funnn", null);
            gameDataAccess.createGame(gameData);
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
        clearService.clear();
        try{
            UserData userData = userDataAccess.getUser("user123");
            assert userData == null;
            AuthData authData = authDataAccess.getAuth(authToken);
            assert authData == null;
            GameData gameData = gameDataAccess.getGame(43);
            assert gameData == null;
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }

    }
}
