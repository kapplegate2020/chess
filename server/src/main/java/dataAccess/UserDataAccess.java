package dataAccess;

import model.UserData;

public interface UserDataAccess {
    UserData createUser(UserData userData) throws DataAccessException;
    UserData getUser() throws DataAccessException;
    void clear() throws DataAccessException;
}
