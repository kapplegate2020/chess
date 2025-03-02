package Result;

public record CreateGameResult(Integer gameID, Integer statusNumber, String message) {
    public CreateGameResult removeStatusNumber(){
        return new CreateGameResult(gameID, null, message);
    }
}
