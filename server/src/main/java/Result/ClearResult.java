package Result;

public record ClearResult(Integer statusNumber, String message) {
    public ClearResult removeStatusNumber(){
        return new ClearResult(null, message);
    }
}
