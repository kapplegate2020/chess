package Result;

import model.GameData;

import java.util.ArrayList;

public record ListGamesResult(ArrayList<GameData> gameList, Integer statusNumber, String message) {
    public ListGamesResult removeStatusNumber(){
        return new ListGamesResult(gameList, null, message);
    }
}
