package request;

public record CreateGameRequest(String authToken, String gameName) {
    public CreateGameRequest addAuthToken(String authToken){
        return new CreateGameRequest(authToken, gameName);
    }
}
