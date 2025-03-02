package Result;

import model.GameData;

import java.util.ArrayList;

public record ListGamesResult(ArrayList<GameData> games, Integer statusNumber, String message) {
    public ListGamesResult removeStatusNumber(){
        return new ListGamesResult(games, null, message);
    }
}
