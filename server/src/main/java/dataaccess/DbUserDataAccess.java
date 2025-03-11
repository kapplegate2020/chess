package dataaccess;

import model.UserData;
import java.util.HashMap;
import java.sql.*;

public class DbUserDataAccess implements  UserDataAccess{
    HashMap<String, UserData> users = new HashMap<String, UserData>();

    public DbUserDataAccess(){
        try{
            configureDatabase();
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void createUser(UserData userData) throws DataAccessException {
        if(getUser(userData.username()) != null){
            throw new DataAccessException("Username Taken");
        }
        try (var conn = DatabaseManager.getConnection()) {
            String statement = "INSERT INTO user (username, password, email) VALUES (?, ?, ?)";
            try (var insertPreparedStatement = conn.prepareStatement(statement)) {
                insertPreparedStatement.setString(1, userData.username());
                insertPreparedStatement.setString(2, userData.password());
                insertPreparedStatement.setString(3, userData.email());
                insertPreparedStatement.executeUpdate();
            }
        } catch (Exception e) {
            throw new DataAccessException(e.getMessage());
        }
    }

    @Override
    public UserData getUser(String username) throws DataAccessException {
        try (var conn = DatabaseManager.getConnection()) {
            String statement = "SELECT username, password, email FROM user WHERE username=?";
            try (var preparedStatement = conn.prepareStatement(statement)){
                preparedStatement.setString(1, username);
                try (var rs = preparedStatement.executeQuery()){
                    if(rs.next()){
                        return new UserData(rs.getString("username"), rs.getString("password"), rs.getString("email"));
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
    public void clear()  throws DataAccessException {
        try {
            users.clear();
        }
        catch (Exception e){
            throw new DataAccessException(e.getMessage());
        }
    }

    public void configureDatabase() throws DataAccessException {
        DatabaseManager.createDatabase();
        try (var conn = DatabaseManager.getConnection()) {
            String statement = """
                    CREATE TABLE IF NOT EXISTS user (
                        `username` varchar(256) NOT NULL,
                        `password` varchar(256) NOT NULL,
                        `email` varchar(256),
                        PRIMARY KEY (`username`)
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
