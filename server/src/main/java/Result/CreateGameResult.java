package Result;

public record CreateGameResult(Integer gameID, Integer statusNumber, String message) {
    public CreateGameResult clearStatusNumber(){
        return new CreateGameResult(gameID, null, message);
    }
}
