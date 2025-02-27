package dataAccess;

import model.UserData;

import java.util.HashMap;

public class MemoryUserDataAccess implements UserDataAccess{
    HashMap<String, UserData> users = new HashMap<String, UserData>();

    @Override
    public void createUser(UserData userData) throws DataAccessException {
        if(users.containsKey(userData.username())){
            throw new DataAccessException("Username Taken");
        }
        users.put(userData.username(), userData);
    }

    @Override
    public UserData getUser(String username) throws DataAccessException {
        if(users.containsKey(username)){
            return users.get(username);
        }
        return null;
    }

    @Override
    public void clear() throws DataAccessException {
        users.clear();
    }
}
