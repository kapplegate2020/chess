package dataaccess;

import model.AuthData;
import model.GameData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class AuthDataAccessTest {
    AuthDataAccess authDataAccess = new DbAuthDataAccess();

    @BeforeEach
    public void clearDB(){
        try {
            authDataAccess.clear();
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void getAuthSuccess(){
        try (var conn = DatabaseManager.getConnection()) {
            String statement = "INSERT INTO auth (authToken, username) VALUES ('a1', 'David')";
            try (var insertPreparedStatement = conn.prepareStatement(statement)) {
                insertPreparedStatement.executeUpdate();
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        try{
            AuthData authData = authDataAccess.getAuth("a1");
            assert authData.authToken().equals("a1");
            assert authData.username().equals("David");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void getAuthFailure(){
        try{
            AuthData authData = authDataAccess.getAuth("b2");
            assert authData == null;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void createGameSuccess(){
        AuthData authData = new AuthData("a123", "Dave");
        try {
            authDataAccess.createAuth(authData);
            AuthData returnedAuthData = authDataAccess.getAuth("a123");
            assert authData.authToken().equals("a123");
            assert authData.username().equals("Dave");
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
    }


    @Test
    public void createGameFailure(){
        AuthData authData = new AuthData("C123", "Davie");
        String errorMessage = "";
        try {
            authDataAccess.createAuth(authData);
        } catch (DataAccessException ex) {
            throw new RuntimeException(ex);
        }

        authData = new AuthData("C123", "Davidie");
        try{
            authDataAccess.createAuth(authData);
        } catch (DataAccessException e) {
            errorMessage = e.getMessage();
        }
        finally {
            assert errorMessage.equals("AuthToken already used.");
        }
    }
}
