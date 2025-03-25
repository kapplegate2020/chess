package ui;

import client.ResponseException;
import client.ServerFacade;
import request.CreateGameRequest;
import request.LogoutRequest;
import result.CreateGameResult;

import java.util.Arrays;
import java.util.Map;

public class LoggedInClient implements Client{
    ServerFacade serverFacade;
    Repl repl;
    String authToken;
    Map<Integer, Integer> gameIDs = null;

    public LoggedInClient(String serverURL, Repl repl, String authToken){
        serverFacade = new ServerFacade(serverURL);
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
            case "create" -> createGame(params);
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

    private String logout(String[] params) throws ResponseException{
        if(params.length!=0){
            return "Invalid Command.";
        }
        LogoutRequest logoutRequest = new LogoutRequest(authToken);
        serverFacade.logout(logoutRequest);
        repl.logout();
        return "";
    }

    private String createGame(String[] params) throws  ResponseException{
        if(params.length != 1){
            return "Invalid Command.";
        }
        CreateGameRequest createGameRequest= new CreateGameRequest(authToken, params[0]);
        serverFacade.createGame(createGameRequest);
        return "Successfully created game "+params[0];
    }


}
