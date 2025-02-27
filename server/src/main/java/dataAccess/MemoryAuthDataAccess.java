package dataAccess;

import model.AuthData;

import java.util.HashMap;

public class MemoryAuthDataAccess implements AuthDataAccess{
    HashMap<String, AuthData> auths = new HashMap<String, AuthData>();

    @Override
    public void createAuth(AuthData authData) throws DataAccessException {
        if(auths.containsKey(authData.authToken())){
            throw new DataAccessException("AuthToken already used.");
        }
        auths.put(authData.authToken(), authData);
    }

    @Override
    public AuthData getAuth(String authToken) throws DataAccessException {
        if(auths.containsKey(authToken)){
            return auths.get(authToken);
        }
        return null;
    }

    @Override
    public void deleteAuth(AuthData authData) throws DataAccessException {
        if(auths.containsKey(authData.authToken())){
            auths.remove(authData.authToken());
        }
        else{
            throw new DataAccessException("AuthData not found.");
        }
    }

    @Override
    public void clear() {
        auths.clear();
    }
}
