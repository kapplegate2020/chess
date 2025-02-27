package dataAccess;

import model.GameData;

import java.util.ArrayList;

public interface GameDataAccess {
    GameData createGame(GameData gameData) throws DataAccessException;
    ArrayList<GameData> listGames() throws DataAccessException;
    GameData updateGame(GameData gameData) throws DataAccessException;
    GameData getGame(String gameID) throws DataAccessException;
    void clear() throws DataAccessException;
}
