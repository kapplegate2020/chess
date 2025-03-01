package dataAccess;

import model.AuthData;

import java.util.HashMap;

public class MemoryAuthDataAccess implements AuthDataAccess{
    HashMap<String, AuthData> auths = new HashMap<String, AuthData>();

    @Override
    public void createAuth(AuthData authData) throws DataAccessException {
        try{
            if(auths.containsKey(authData.authToken())){
                throw new DataAccessException("AuthToken already used.");
            }
            auths.put(authData.authToken(), authData);
        }
        catch (Exception e){
            throw new DataAccessException(e.getMessage());
        }
    }

    @Override
    public AuthData getAuth(String authToken) throws DataAccessException {
        try{
            if(auths.containsKey(authToken)){
                return auths.get(authToken);
            }
            return null;
        }
        catch (Exception e){
            throw new DataAccessException(e.getMessage());
        }
    }

    @Override
    public void deleteAuth(AuthData authData) throws DataAccessException {
        try{
            if(auths.containsKey(authData.authToken())){
                auths.remove(authData.authToken());
            }
            else{
                throw new DataAccessException("AuthData not found.");
            }
        }
        catch (Exception e){
            throw new DataAccessException(e.getMessage());
        }
    }

    @Override
    public void clear() throws DataAccessException {
        try {
            auths.clear();
        }
        catch (Exception e){
            throw new DataAccessException(e.getMessage());
        }
    }
}
