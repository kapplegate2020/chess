package dataaccess;

import model.AuthData;
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
    public void createAuthSuccess(){
        AuthData authData = new AuthData("a123", "Dave");
        try {
            authDataAccess.createAuth(authData);
            AuthData returnedAuthData = authDataAccess.getAuth("a123");
            assert returnedAuthData.authToken().equals("a123");
            assert returnedAuthData.username().equals("Dave");
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
    }


    @Test
    public void createAuthFailure(){
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

    @Test
    public void deleteAuthSuccess(){
        AuthData authData = new AuthData("D4321", "Davidham");
        try {
            authDataAccess.createAuth(authData);
            AuthData returnedAuthData = authDataAccess.getAuth("D4321");
            assert returnedAuthData != null;
            authDataAccess.deleteAuth(authData);
            returnedAuthData = authDataAccess.getAuth("D4321");
            assert returnedAuthData == null;
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void deleteAuthFailure(){
        AuthData authData = new AuthData("D314", "Davidhammer");
        try {
            authDataAccess.deleteAuth(authData);
        } catch (DataAccessException e) {
            assert e.getMessage().equals("AuthData not found.");
        }
    }


    @Test
    public void clear(){
        AuthData authData = new AuthData("E55555", "Davideth");
        try {
            authDataAccess.createAuth(authData);
            AuthData returnedAuthData = authDataAccess.getAuth("E55555");
            assert returnedAuthData != null;
            authDataAccess.clear();
            returnedAuthData = authDataAccess.getAuth("E55555");
            assert returnedAuthData == null;
        } catch (DataAccessException ex) {
            throw new RuntimeException(ex);
        }
    }
}
