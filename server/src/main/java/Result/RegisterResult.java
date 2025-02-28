package Result;

public record RegisterResult(String username, String authToken, Integer errorNumber, String error) {
}
