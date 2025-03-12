package service;

import org.mindrot.jbcrypt.BCrypt;
import request.LoginRequest;
import request.LogoutRequest;
import request.RegisterRequest;
import result.LoginResult;
import result.LogoutResult;
import result.RegisterResult;
import dataaccess.AuthDataAccess;
import dataaccess.DataAccessException;
import dataaccess.UserDataAccess;
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
        if(registerRequest.password()==null){
            return new RegisterResult(null, null, 400, "Error: bad request");
        }
        try {
            if (userDataAccess.getUser(registerRequest.username()) == null) {
                String username = registerRequest.username();
                String password = registerRequest.password();
                password = BCrypt.hashpw(password, BCrypt.gensalt());
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
            if(!BCrypt.checkpw(password, userData.password())){
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

    public LogoutResult logout(LogoutRequest logoutRequest){
        try{
            String authToken = logoutRequest.authToken();
            AuthData authData = authDataAccess.getAuth(authToken);
            if(authData == null){
                return new LogoutResult(401, "Error: unauthorized");
            }
            authDataAccess.deleteAuth(authData);
            return new LogoutResult(200, null);
        }
        catch (DataAccessException e) {
            return new LogoutResult(500, e.getMessage());
        }
    }

}
