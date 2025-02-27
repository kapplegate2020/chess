package dataAccess;

import model.AuthData;

public interface AuthDataAccess {
    AuthData createAuth(AuthData authData) throws DataAccessException;
    AuthData getAuth(AuthData authData) throws DataAccessException;
    void deleteAuth(AuthData authData) throws DataAccessException;
    void clear() throws DataAccessException;

}
