package dataAccess;

import model.UserData;

import java.util.HashMap;

public class MemoryUserDataAccess implements UserDataAccess{
    HashMap<String, UserData> users = new HashMap<String, UserData>();

    @Override
    public void createUser(UserData userData) throws DataAccessException {
        try {
            if (users.containsKey(userData.username())) {
                throw new DataAccessException("Username Taken");
            }
            users.put(userData.username(), userData);
        }
        catch (Exception e){
            throw new DataAccessException(e.getMessage());
        }
    }

    @Override
    public UserData getUser(String username) throws DataAccessException {
        try{
            if(users.containsKey(username)){
                return users.get(username);
            }
            return null;
        }
            catch (Exception e){
            throw new DataAccessException(e.getMessage());
        }
    }

    @Override
    public void clear()  throws DataAccessException {
        try {
            users.clear();
        }
        catch (Exception e){
            throw new DataAccessException(e.getMessage());
        }
    }
}
