package ui;

public class LoggedInClient implements Client{
    String serverURL;
    Repl repl;
    String authToken;

    public LoggedInClient(String serverURL, Repl repl, String authToken){
        this.serverURL = serverURL;
        this.repl = repl;
        this.authToken = authToken;
    }

    @Override
    public String eval(String input){
        return input;
    }

    @Override
    public String help(){
        return authToken;
    }
}
