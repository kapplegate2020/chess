package dataaccess;

import model.AuthData;

import java.util.HashMap;

public class DbAuthDataAccess implements AuthDataAccess{
    HashMap<String, AuthData> auths = new HashMap<String, AuthData>();
    public DbAuthDataAccess(){
        try {
            configureDatabase();
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
    }

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

    public void configureDatabase() throws DataAccessException {
        DatabaseManager.createDatabase();
        try (var conn = DatabaseManager.getConnection()) {
            String statement = """
                    CREATE TABLE IF NOT EXISTS auth (
                        `authToken` varchar(256) NOT NULL,
                        `username` varchar(256) NOT NULL,
                        PRIMARY KEY (`authToken`)
                    )
                    """;
            try (var preparedStatement = conn.prepareStatement(statement)) {
                preparedStatement.executeUpdate();
            }
        } catch (Exception e) {
            throw new DataAccessException(e.getMessage());
        }
    }
}