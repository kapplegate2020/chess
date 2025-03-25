package ui;

import client.ResponseException;

import java.util.Arrays;

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
    public String eval(String input) throws ResponseException {
        String[] tokens = input.toLowerCase().split(" ");
        if(tokens.length==0){
            return help();
        }
        String command = tokens[0];
        String[] params = Arrays.copyOfRange(tokens, 1, tokens.length);
        return switch (command) {
            case "logout" -> logout(params);
            case "create" -> listGames(params);
            case "list" -> listGames(params);
            case "join" -> join(params);
            case "observe" -> observe(params);
            default -> help();
        };
    }

    @Override
    public String help(){
        return """
                - logout - to log out
                - create <NAME> - to create a game
                - list - to list all the games
                - join <ID> [WHITE|BLACK] - to join a game
                - observe <ID> - to watch a game
                """;
    }

    private logout(String[] params){
        if(params.length!=0){
            return "Invalid Command.";
        }
        repl.logout();
        return "";
    }
}
