package service;

import dataAccess.AuthDataAccess;
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




    public void clear(){
        userDataAccess.clear();
        gameDataAccess.clear();
        authDataAccess.clear();
    }
}
