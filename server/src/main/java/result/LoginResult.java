package result;

public record LoginResult(String username, String authToken, Integer statusNumber, String message) {
    public LoginResult removeStatusNumber(){
        return new LoginResult(username, authToken,null, message);
    }
}
