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
}
