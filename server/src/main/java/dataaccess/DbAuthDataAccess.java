package dataaccess;

import chess.ChessGame;
import com.google.gson.Gson;
import model.AuthData;
import model.GameData;

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
        if(getAuth(authData.authToken()) != null){
            throw new DataAccessException("AuthToken already used.");
        }
        try (var conn = DatabaseManager.getConnection()) {
            String statement = "INSERT INTO auth (authToken, username) VALUES (?, ?)";
            try (var insertPreparedStatement = conn.prepareStatement(statement)) {
                insertPreparedStatement.setString(1, authData.authToken());
                insertPreparedStatement.setString(2, authData.username());
                insertPreparedStatement.executeUpdate();
            }
        } catch (Exception e) {
            throw new DataAccessException(e.getMessage());
        }
    }

    @Override
    public AuthData getAuth(String authToken) throws DataAccessException {
        try (var conn = DatabaseManager.getConnection()) {
            String statement = "SELECT authToken, username FROM auth WHERE authToken=?";
            try (var preparedStatement = conn.prepareStatement(statement)){
                preparedStatement.setString(1, authToken);
                try (var rs = preparedStatement.executeQuery()){
                    if(rs.next()){
                        return new AuthData(rs.getString(1), rs.getString(2));
                    }
                    else{
                        return null;
                    }
                }
            }
        } catch (Exception e) {
            throw new DataAccessException(e.getMessage());
        }
    }

    @Override
    public void deleteAuth(AuthData authData) throws DataAccessException {
        if(getAuth(authData.authToken()) == null){
            throw new DataAccessException("AuthData not found.");
        }
        try (var conn = DatabaseManager.getConnection()) {
            String statement = "DELETE FROM auth WHERE authToken = ?";
            try (var insertPreparedStatement = conn.prepareStatement(statement)) {
                insertPreparedStatement.setString(1, authData.authToken());
                insertPreparedStatement.executeUpdate();
            }
        } catch (Exception e) {
            throw new DataAccessException(e.getMessage());
        }
    }

    @Override
    public void clear() throws DataAccessException {
        try (var conn = DatabaseManager.getConnection()) {
            String statement = "TRUNCATE auth";
            try (var insertPreparedStatement = conn.prepareStatement(statement)) {
                insertPreparedStatement.executeUpdate();
            }
        } catch (Exception e) {
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