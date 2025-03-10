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
            String configureStatement = """
                    CREATE TABLE IF NOT EXISTS user (
                        `username` varchar(256) NOT NULL,
                        `password` varchar(256) NOT NULL,
                        `email` varchar(256),
                        PRIMARY KEY (`username`)
                    )
                    """;
            try (var preparedStatement = conn.prepareStatement(configureStatement)) {
                var rs = preparedStatement.executeQuery();
                rs.next();
                System.out.println(rs.getInt(1));
            }
        } catch (Exception e) {
            throw new DataAccessException(e.getMessage());
        }
    }
}
