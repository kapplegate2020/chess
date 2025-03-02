package Result;

public record JoinGameResult(Integer statusNumber, String message) {
    public JoinGameResult clearStatusNumber(){
        return new JoinGameResult(null, message);
    }
}
