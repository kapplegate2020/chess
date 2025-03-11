package dataaccess;


import model.UserData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class UserDataAccessTest {
    UserDataAccess userDataAccess = new DbUserDataAccess();

    @BeforeEach
    public void clearDB(){
        try {
            userDataAccess.clear();
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void getUserSuccess(){
        try (var conn = DatabaseManager.getConnection()) {
            String statement = "INSERT INTO user (username, password, email) VALUES ('Andy', 'passw0rd', 'fake@mail.com')";
            try (var insertPreparedStatement = conn.prepareStatement(statement)) {
                insertPreparedStatement.executeUpdate();
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        try{
            UserData userData = userDataAccess.getUser("Andy");
            assert userData.username().equals("Andy");
            assert userData.password().equals("passw0rd");
            assert userData.email().equals("fake@mail.com");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void getUserFailure(){
        try (var conn = DatabaseManager.getConnection()) {
            String statement = "INSERT INTO user (username, password, email) VALUES ('Andy', 'passw0rd', 'fake@mail.com')";
            try (var insertPreparedStatement = conn.prepareStatement(statement)) {
                insertPreparedStatement.executeUpdate();
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        try{
            UserData userData = userDataAccess.getUser("Dave");
            assert userData == null;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    @Test
    public void createUserSuccess(){
        UserData userData = new UserData("Adam", "secure12", "email@email.com");
        try {
            userDataAccess.createUser(userData);
            UserData returnedUserData = userDataAccess.getUser("Adam");
            assert returnedUserData.username().equals("Adam");
            assert returnedUserData.password().equals("secure12");
            assert returnedUserData.email().equals("email@email.com");
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
    }


    @Test
    public void createUserFailure(){
        UserData userData = new UserData("Aaron", "securer7", "yep@em.com");
        String errorMessage = "";
        try {
            userDataAccess.createUser(userData);
        } catch (DataAccessException ex) {
            throw new RuntimeException(ex);
        }

        try{
            userDataAccess.createUser(userData);
        } catch (DataAccessException e) {
            errorMessage = e.getMessage();
        }
        finally {
            assert errorMessage.equals("Username Taken");
        }
    }


    @Test
    public void clear(){
        UserData userData = new UserData("Ammon", "myPass99", "chessPlayer@m.com");
        try {
            userDataAccess.createUser(userData);
            UserData returnedUserData = userDataAccess.getUser("Ammon");
            assert returnedUserData != null;
            userDataAccess.clear();
            returnedUserData = userDataAccess.getUser("Ammon");
            assert returnedUserData == null;
        } catch (DataAccessException ex) {
            throw new RuntimeException(ex);
        }
    }
}
