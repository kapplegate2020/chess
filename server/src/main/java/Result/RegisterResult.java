package Result;

public class RegisterResult{
    private final String username;
    private final String authToken;
    private final transient Integer statusNumber;
    private final String message;

    public RegisterResult(String username, String authToken, Integer statusNumber, String message){
        this.username = username;
        this.authToken = authToken;
        this.statusNumber = statusNumber;
        this.message = message;
    }

    public String username(){
        return username;
    }

    public String authToken(){
        return authToken;
    }

    public Integer statusNumber(){
        return statusNumber;
    }

    public String message(){
        return message;
    }
}
