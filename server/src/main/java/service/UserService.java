package service;

import Request.LoginRequest;
import Request.RegisterRequest;
import Result.LoginResult;
import Result.RegisterResult;
import dataAccess.AuthDataAccess;
import dataAccess.DataAccessException;
import dataAccess.UserDataAccess;
import model.AuthData;
import model.UserData;

import java.util.UUID;

public class UserService {
    private final UserDataAccess userDataAccess;
    private final AuthDataAccess authDataAccess;

    public UserService(UserDataAccess userDataAccess, AuthDataAccess authDataAccess){
        this.userDataAccess = userDataAccess;
        this.authDataAccess = authDataAccess;
    }

    public static String generateToken() {
        return UUID.randomUUID().toString();
    }

    public RegisterResult register(RegisterRequest registerRequest){
        try {
            if (userDataAccess.getUser(registerRequest.username()) == null) {
                String username = registerRequest.username();
                String password = registerRequest.password();
                String email = registerRequest.email();
                UserData userData = new UserData(username, password, email);
                userDataAccess.createUser(userData);
                String authToken = generateToken();
                AuthData authData = new AuthData(authToken, registerRequest.username());
                authDataAccess.createAuth(authData);
                return new RegisterResult(username, authToken, 200, null);
            }
            return new RegisterResult(null, null, 403, "Error: already taken");
        }
        catch (DataAccessException e){
            return new RegisterResult(null, null, 500, e.getMessage());
        }
    }

    public LoginResult login(LoginRequest loginRequest){
        try{
            String username = loginRequest.username();
            String password = loginRequest.password();
            UserData userData = userDataAccess.getUser(username);
            if(userData == null){
                return new LoginResult(null, null, 401, "Error: unauthorized");
            }
            if(!password.equals(userData.password())){
                return new LoginResult(null, null, 401, "Error: unauthorized");
            }
            String authToken = generateToken();
            AuthData authData = new AuthData(authToken, username);
            authDataAccess.createAuth(authData);
            return new LoginResult(username, authToken, 200, null);
        }
        catch (DataAccessException e) {
            return new LoginResult(null, null, 500, e.getMessage());
        }
    }

}
