package service;

import Result.ClearResult;
import dataAccess.AuthDataAccess;
import dataAccess.DataAccessException;
import dataAccess.GameDataAccess;
import dataAccess.UserDataAccess;

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
            return new ClearResult(null, null);
        }
        catch (DataAccessException e){
            return new ClearResult(500, e.getMessage());
        }
    }
}
