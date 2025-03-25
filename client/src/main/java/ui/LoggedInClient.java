package ui;

import chess.ChessGame;
import client.ResponseException;
import client.ServerFacade;
import model.GameData;
import request.CreateGameRequest;
import request.JoinGameRequest;
import request.ListGamesRequest;
import request.LogoutRequest;
import result.CreateGameResult;
import result.ListGamesResult;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;

public class LoggedInClient implements Client{
    ServerFacade serverFacade;
    Repl repl;
    String authToken;
    ArrayList<Integer> gameIds = null;

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
            case "join" -> joinGame(params);
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
        return "Successfully logged out.";
    }

    private String createGame(String[] params) throws  ResponseException{
        if(params.length != 1){
            return "Invalid Command.";
        }
        CreateGameRequest createGameRequest= new CreateGameRequest(authToken, params[0]);
        serverFacade.createGame(createGameRequest);
        return "Successfully created game "+params[0];
    }

    private String listGames(String[] params) throws ResponseException{
        if(params.length != 0){
            return "Invalid Command.";
        }
        ListGamesRequest listGamesRequest = new ListGamesRequest(authToken);
        ListGamesResult listGamesResult = serverFacade.listGames(listGamesRequest);
        gameIds = new ArrayList<>();
        Integer counter = 0;
        StringBuilder gameStr = new StringBuilder();
        for(GameData game : listGamesResult.games()){
            gameIds.add(game.gameID());
            String white = "None";
            String black = "None";
            if(game.whiteUsername() != null){
                white = game.whiteUsername();
            }
            if(game.blackUsername() != null){
                black = game.blackUsername();
            }
            gameStr.append(counter);
            gameStr.append(".\tGame Name: ");
            gameStr.append(game.gameName());
            gameStr.append("\tWhite: ");
            gameStr.append(white);
            gameStr.append("\tBlack: ");
            gameStr.append(black);
            gameStr.append("\n");
            counter++;
        }
        return gameStr.toString();
    }

    private String joinGame(String[] params) throws ResponseException{
        if(params.length!=2){
            return "Invalid Command.";
        }

        int id = checkGameId(params[0]);

        ChessGame.TeamColor team = null;
        if(params[1].equals("white")){
            team = ChessGame.TeamColor.WHITE;
        }
        else if(params[1].equals("black")){
            team = ChessGame.TeamColor.BLACK;
        }
        else{
            return "Please choose either black or white.";
        }

        JoinGameRequest joinGameRequest = new JoinGameRequest(authToken, gameIds.get(id), team);
        serverFacade.joinGame(joinGameRequest);
        repl.joinGame(authToken, new ChessGame(), team);
        return "Successfully joined game "+id;
    }


    private String observe(String[] params) throws ResponseException{
        if(params.length!=1){
            return "Invalid Command.";
        }

        int id = checkGameId(params[0]);

        repl.joinGame(authToken, new ChessGame(), ChessGame.TeamColor.WHITE);
        return "This feature is not fully implemented yet.";
    }

    private Integer checkGameId(String idStr) throws ResponseException{
        int id;
        try{
            id = Integer.parseInt(idStr);
        } catch (NumberFormatException e) {
            throw new ResponseException("Not an integer");
        }

        if(gameIds == null){
            throw new ResponseException("Please list the games before trying to join.");
        }

        if(gameIds.isEmpty()){
            throw new ResponseException("There are no games currently. You can create a game or list the games again.");
        }

        if(id<0 || id>= gameIds.size()){
            throw new ResponseException("Game Id does not exist.");
        }
        return id;
    }


}
