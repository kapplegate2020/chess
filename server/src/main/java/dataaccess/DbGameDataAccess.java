package dataaccess;

import chess.ChessGame;
import com.google.gson.Gson;
import model.GameData;
import model.UserData;

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
        try{
            if(games.containsKey(gameData.gameID())){
                throw new DataAccessException("GameID is already taken.");
            }
            games.put(gameData.gameID(), gameData);
        }
        catch (Exception e){
            throw new DataAccessException(e.getMessage());
        }
    }

    @Override
    public ArrayList<GameData> listGames() throws DataAccessException {
        try{
            return new ArrayList<GameData>(games.values());
        }
        catch (Exception e){
            throw new DataAccessException(e.getMessage());
        }
    }

    @Override
    public void updateGame(GameData gameData) throws DataAccessException {
        try{
            if(!games.containsKey(gameData.gameID())){
                throw new DataAccessException("GameID does not exist.");
            }
            games.put(gameData.gameID(), gameData);
        }
        catch (Exception e){
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
        try {
            games.clear();
        }
        catch(Exception e){
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
