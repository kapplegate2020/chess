package dataaccess;

import model.UserData;

import java.util.HashMap;

public class DbUserDataAccess implements  UserDataAccess{
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
        try (var conn = DatabaseManager.getConnection()) {
            String statement = "INSERT INTO user (username, password, email) VALUES (?, ?, ?)";
            try (var preparedStatement = conn.prepareStatement(statement)) {
                preparedStatement.setString(1, userData.username());
                preparedStatement.setString(2, userData.password());
                preparedStatement.setString(3, userData.email());
                preparedStatement.executeQuery();
            }
        } catch (Exception e) {
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

    public void configureDatabase() throws DataAccessException {
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
                var rs = preparedStatement.executeQuery();
                rs.next();
                System.out.println(rs.getInt(1));
            }
        } catch (Exception e) {
            throw new DataAccessException(e.getMessage());
        }
    }
}
