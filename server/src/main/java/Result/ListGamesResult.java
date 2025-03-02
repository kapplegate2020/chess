package Result;

import model.GameData;

import java.util.ArrayList;

public record ListGamesResult(ArrayList<GameData> gameList, Integer statusNumber, String message) {
    public ListGamesResult clearStatusNumber(){
        return new ListGamesResult(gameList, null, message);
    }
}
