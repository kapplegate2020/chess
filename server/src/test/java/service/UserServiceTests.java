package service;

import Request.RegisterRequest;
import Result.RegisterResult;
import dataAccess.*;
import model.UserData;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import server.Server;

public class UserServiceTests {
    UserDataAccess userDataAccess = new MemoryUserDataAccess();
    AuthDataAccess authDataAccess = new MemoryAuthDataAccess();
    UserService userService = new UserService(userDataAccess, authDataAccess);

    @BeforeEach
    public void resetDataAccess(){
        try {
            userDataAccess.clear();
            authDataAccess.clear();
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void registerTestSuccess(){
        RegisterRequest registerRequest = new RegisterRequest("Jimmy", "test1234", "me@fake.com");
        RegisterResult registerResult = userService.register(registerRequest);
        assert registerResult.statusNumber() == 200;
        assert registerResult.username().equals("Jimmy");
        assert registerResult.authToken() != null;
        try{
            UserData userData = userDataAccess.getUser("Jimmy");
            assert userData != null;
            assert userData.username().equals("Jimmy");
            assert userData.password().equals("test1234");
            assert userData.email().equals("me@fake.com");
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void registerTestFailure(){
        try{
            UserData userData = new UserData("Jimmy", "fakepass", "fake@email.com");
            userDataAccess.createUser(userData);
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
        RegisterRequest registerRequest = new RegisterRequest("Jimmy", "test1234", "me@fake.com");
        RegisterResult registerResult = userService.register(registerRequest);
        assert registerResult.statusNumber() == 403;
        assert registerResult.message().equals("Error: already taken");
        assert registerResult.authToken() == null;
        assert registerResult.username() == null;
        try{
            UserData userData = userDataAccess.getUser("Jimmy");
            assert userData != null;
            assert userData.username().equals("Jimmy");
            assert userData.password().equals("fakepass");
            assert userData.email().equals("fake@email.com");
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
    }
}
