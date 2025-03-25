package result;

public record RegisterResult(String username, String authToken, Integer statusNumber, String message) implements Result{
    public RegisterResult removeStatusNumber(){
        return new RegisterResult(username, authToken, null, message);
    }

    @Override
    public RegisterResult addStatusNumber(int statusNumber){
        return new RegisterResult(username, authToken, statusNumber, message);
    }
}
