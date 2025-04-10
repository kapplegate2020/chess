package ui;

import chess.ChessGame;
import client.ServerFacade;
import client.WebSocketFacade;

import java.util.Arrays;

public class GameClient implements Client{
    ChessGame game;
    int gameID;
    String authToken;
    Repl repl;
    WebSocketFacade webSocketFacade;

    public GameClient(String serverURL, Repl repl, String authToken, int gameID, ChessGame.TeamColor viewPoint){
        this.repl = repl;
        this.authToken = authToken;
        webSocketFacade = new WebSocketFacade(serverURL);
        this.gameID = gameID;
        try{
            webSocketFacade.connect(authToken, gameID);
        } catch (Exception e) {

        }
        DrawGame draw = new DrawGame(game, viewPoint);
        draw.draw();
    }

    @Override
    public String eval(String input){
        String[] tokens = input.toLowerCase().split(" ");
        if(tokens.length==0){
            return help();
        }
        String command = tokens[0];
        String[] params = Arrays.copyOfRange(tokens, 1, tokens.length);
        return switch (command) {
            case "redraw" -> redraw(params);
            case "leave" -> leave(params);
            case "move" -> move(params);
            case "resign" -> resign(params);
            case "legal" -> legal(params);
            default -> help();
        };
    }

    private String redraw(String[] params){
        try {
            //webSocketFacade.send("Testing, 123");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return "Not implemented yet";
    }

    private String leave(String[] params){
        return "Not implemented yet";
    }

    private String move(String[] params){
        return "Not implemented yet";
    }

    private String resign(String[] params){
        return "Not implemented yet";
    }

    private String legal(String[] params){
        return "Not implemented yet";
    }

    @Override
    public String help(){
        return "help";
    }
}
