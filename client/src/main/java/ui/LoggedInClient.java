package ui;

public class LoggedInClient implements Client{
    String serverURL;
    Repl repl;

    public LoggedInClient(String serverURL, Repl repl){
        this.serverURL = serverURL;
        this.repl = repl;
    }

    @Override
    public String eval(String input){

    }
}
