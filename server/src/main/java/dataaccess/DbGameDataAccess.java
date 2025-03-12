package dataaccess;

import chess.ChessGame;
import com.google.gson.Gson;
import model.GameData;

import java.util.ArrayList;
import java.util.HashMap;

public class DbGameDataAccess implements GameDataAccess{
    HashMap<Integer, GameData> games = new HashMap<Integer, GameData>();

    public DbGameDataAccess(){
        try {
            configureDatabase();
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void createGame(GameData gameData) throws DataAccessException {
        if(getGame(gameData.gameID()) != null){
            throw new DataAccessException("GameID is already taken.");
        }
        try (var conn = DatabaseManager.getConnection()) {
            String statement = "INSERT INTO game (gameID, whiteUsername, blackUsername, gameName, game) VALUES (?, ?, ?, ?, ?)";
            try (var insertPreparedStatement = conn.prepareStatement(statement)) {
                insertPreparedStatement.setInt(1, gameData.gameID());
                insertPreparedStatement.setString(2, gameData.whiteUsername());
                insertPreparedStatement.setString(3, gameData.blackUsername());
                insertPreparedStatement.setString(4, gameData.gameName());
                String chessGame = new Gson().toJson(gameData.game());
                insertPreparedStatement.setString(5, chessGame);
                insertPreparedStatement.executeUpdate();
            }
        } catch (Exception e) {
            throw new DataAccessException(e.getMessage());
        }
    }

    @Override
    public ArrayList<GameData> listGames() throws DataAccessException {
        try (var conn = DatabaseManager.getConnection()) {
            String statement = "SELECT gameID, whiteUsername, blackUsername, gameName, game FROM game";
            try (var preparedStatement = conn.prepareStatement(statement)){
                try (var rs = preparedStatement.executeQuery()){
                    ArrayList<GameData> games = new ArrayList<GameData>();
                    while(rs.next()){
                        int returnedGameID = rs.getInt("gameID");
                        String whiteUsername = rs.getString("whiteUsername");
                        String blackUsername = rs.getString("blackUsername");
                        String gameName = rs.getString("gameName");
                        ChessGame chessGame = new Gson().fromJson(rs.getString("game"), ChessGame.class);
                        games.add(new GameData(returnedGameID, whiteUsername, blackUsername, gameName, chessGame));
                    }
                    return games;
                }
            }
        } catch (Exception e) {
            throw new DataAccessException(e.getMessage());
        }
    }

    @Override
    public void updateGame(GameData gameData) throws DataAccessException {
        if(getGame(gameData.gameID()) == null){
            throw new DataAccessException("GameID does not exist.");
        }
        try (var conn = DatabaseManager.getConnection()) {
            String statement = "UPDATE game SET whiteUsername = ?, blackUsername = ?, gameName = ?, game = ? WHERE gameID = ?";
            try (var insertPreparedStatement = conn.prepareStatement(statement)) {
                insertPreparedStatement.setInt(5, gameData.gameID());
                insertPreparedStatement.setString(1, gameData.whiteUsername());
                insertPreparedStatement.setString(2, gameData.blackUsername());
                insertPreparedStatement.setString(3, gameData.gameName());
                String chessGame = new Gson().toJson(gameData.game());
                insertPreparedStatement.setString(4, chessGame);
                insertPreparedStatement.executeUpdate();
            }
        } catch (Exception e) {
            throw new DataAccessException(e.getMessage());
        }
    }

    @Override
    public GameData getGame(int gameID) throws DataAccessException {
        try (var conn = DatabaseManager.getConnection()) {
            String statement = "SELECT gameID, whiteUsername, blackUsername, gameName, game FROM game WHERE gameID=?";
            try (var preparedStatement = conn.prepareStatement(statement)){
                preparedStatement.setInt(1, gameID);
                try (var rs = preparedStatement.executeQuery()){
                    if(rs.next()){
                        int returnedGameID = rs.getInt("gameID");
                        String whiteUsername = rs.getString("whiteUsername");
                        String blackUsername = rs.getString("blackUsername");
                        String gameName = rs.getString("gameName");
                        ChessGame chessGame = new Gson().fromJson(rs.getString("game"), ChessGame.class);
                        return new GameData(returnedGameID, whiteUsername, blackUsername, gameName, chessGame);
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
        try (var conn = DatabaseManager.getConnection()) {
            String statement = "TRUNCATE game";
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
                    CREATE TABLE IF NOT EXISTS game (
                        `gameID` int NOT NULL,
                        `whiteUsername` varchar(256),
                        `blackUsername` varchar(256),
                        `gameName` varchar(256) NOT NULL,
                        `game` text,
                        PRIMARY KEY (`gameID`)
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
