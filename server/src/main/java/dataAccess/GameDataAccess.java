package dataAccess;

import model.GameData;

import java.util.ArrayList;

public interface GameDataAccess {
    void createGame(GameData gameData) throws DataAccessException;
    ArrayList<GameData> listGames() throws DataAccessException;
    void updateGame(GameData gameData) throws DataAccessException;
    GameData getGame(int gameID) throws DataAccessException;
    void clear() throws DataAccessException;
}
