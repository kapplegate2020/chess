package service;

import result.ClearResult;
import dataaccess.AuthDataAccess;
import dataaccess.DataAccessException;
import dataaccess.GameDataAccess;
import dataaccess.UserDataAccess;

public class ClearService {
    private final UserDataAccess userDataAccess;
    private final GameDataAccess gameDataAccess;
    private final AuthDataAccess authDataAccess;

    public ClearService(UserDataAccess userDataAccess, GameDataAccess gameDataAccess, AuthDataAccess authDataAccess){
        this.userDataAccess = userDataAccess;
        this.gameDataAccess = gameDataAccess;
        this.authDataAccess = authDataAccess;
    }

    public ClearResult clear(){
        try {
            userDataAccess.clear();
            gameDataAccess.clear();
            authDataAccess.clear();
            return new ClearResult(200, null);
        }
        catch (DataAccessException e){
            return new ClearResult(500, e.getMessage());
        }
    }
}
