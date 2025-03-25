package ui;

import chess.ChessGame;
import client.ServerFacade;

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
        if(input.equals("quit")){
            repl.quit();
            return "";
        }
        else if(input.equals("leave")){
            repl.leaveGame(authToken);
            return "";
        }
        return "Not yet implemented.";
    }

    @Override
    public String help(){
        return "help";
    }
}
