package Result;

public record LogoutResult(Integer statusNumber, String message) {
    public LogoutResult removeStatusNumber(){
        return new LogoutResult(null, message);
    }
}
