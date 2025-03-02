package service;

import Request.LoginRequest;
import Request.RegisterRequest;
import Result.LoginResult;
import Result.RegisterResult;
import dataAccess.*;
import model.AuthData;
import model.UserData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

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
            AuthData authData = authDataAccess.getAuth(registerResult.authToken());
            assert authData != null;
            assert authData.username().equals("Jimmy");
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

    @Test
    public void loginTestSuccess(){
        try{
            UserData userData = new UserData("Johnny", "thisSafe12", "fake@email.com");
            userDataAccess.createUser(userData);
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
        LoginRequest loginRequest = new LoginRequest("Johnny", "thisSafe12");
        LoginResult loginResult = userService.login(loginRequest);
        assert loginResult.statusNumber() == 200;
        assert loginResult.username().equals("Johnny");
        assert loginResult.authToken() != null;
        try{
            AuthData authData = authDataAccess.getAuth(loginResult.authToken());
            assert authData != null;
            assert authData.username().equals("Johnny");
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void loginTestFailure(){
        try{
            UserData userData = new UserData("Johnny", "thisSafe12", "fake@email.com");
            userDataAccess.createUser(userData);
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
        LoginRequest loginRequest = new LoginRequest("Johnny", "wrongPassword");
        LoginResult loginResult = userService.login(loginRequest);
        assert loginResult.statusNumber() == 401;
        assert loginResult.message().equals("Error: unauthorized");
        LoginRequest loginRequest2 = new LoginRequest("Jane", "thisSafe12");
        LoginResult loginResult2 = userService.login(loginRequest2);
        assert loginResult2.statusNumber() == 401;
        assert loginResult2.message().equals("Error: unauthorized");
    }
}
