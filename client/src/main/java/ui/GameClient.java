package ui;

import chess.ChessGame;
import client.ServerFacade;

import java.util.Arrays;

public class GameClient implements Client{
    ChessGame game;
    String authToken;
    ServerFacade serverFacade;
    Repl repl;

    public GameClient(String serverURL, Repl repl, String authToken, ChessGame game, ChessGame.TeamColor viewPoint){
        serverFacade = new ServerFacade(serverURL);
        this.repl = repl;
        this.authToken = authToken;
        this.game = game;
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

    @Override
    public String help(){
        return "help";
    }
}
