package result;

public record RegisterResult(String username, String authToken, Integer statusNumber, String message) {
    public RegisterResult removeStatusNumber(){
        return new RegisterResult(username, authToken, null, message);
    }
}
