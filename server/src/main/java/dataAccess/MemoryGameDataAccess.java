package dataAccess;

import model.GameData;

import java.util.ArrayList;
import java.util.HashMap;

public class MemoryGameDataAccess implements GameDataAccess{
    HashMap<Integer, GameData> games = new HashMap<Integer, GameData>();

    @Override
    public void createGame(GameData gameData) throws DataAccessException {
        if(games.containsKey(gameData.gameID())){
            throw new DataAccessException("GameID is already taken.");
        }
    }

    @Override
    public ArrayList<GameData> listGames() throws DataAccessException {
        return new ArrayList<GameData>(games.values());
    }

    @Override
    public void updateGame(GameData gameData) throws DataAccessException {
        if(!games.containsKey(gameData.gameID())){
            throw new DataAccessException("GameID does not exist.");
        }
        games.put(gameData.gameID(), gameData);
    }

    @Override
    public GameData getGame(int gameID) throws DataAccessException {
        if(games.containsKey(gameID)){
            return games.get(gameID);
        }
        return null;
    }

    @Override
    public void clear() {
        games.clear();
    }
}
