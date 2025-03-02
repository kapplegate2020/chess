package Result;

public record JoinGameResult(Integer statusNumber, String message) {
    public JoinGameResult removeStatusNumber(){
        return new JoinGameResult(null, message);
    }
}
