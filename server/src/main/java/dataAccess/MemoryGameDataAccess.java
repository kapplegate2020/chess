package dataAccess;

import model.GameData;

import java.util.ArrayList;
import java.util.HashMap;

public class MemoryGameDataAccess implements GameDataAccess{
    HashMap<Integer, GameData> games = new HashMap<Integer, GameData>();

    @Override
    public void createGame(GameData gameData) throws DataAccessException {
        try{
            if(games.containsKey(gameData.gameID())){
                throw new DataAccessException("GameID is already taken.");
            }
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
        try{
            if(games.containsKey(gameID)){
                return games.get(gameID);
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
            games.clear();
        }
        catch(Exception e){
            throw new DataAccessException(e.getMessage());
        }
    }
}
