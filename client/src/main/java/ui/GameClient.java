package ui;

import chess.ChessGame;
import chess.ChessMove;
import chess.ChessPosition;
import client.NotificationHandler;
import client.ServerFacade;
import client.WebSocketFacade;

import java.util.Arrays;
import java.util.zip.CheckedInputStream;

public class GameClient implements Client{
    ChessGame game;
    int gameID;
    String authToken;
    Repl repl;
    WebSocketFacade webSocketFacade;
    NotificationHandler notificationHandler;
    ChessGame.TeamColor viewPoint;

    public GameClient(String serverURL, Repl repl, String authToken, int gameID, ChessGame.TeamColor viewPoint){
        this.repl = repl;
        this.authToken = authToken;
        this.viewPoint = viewPoint;
        notificationHandler = new NotificationHandler(this, viewPoint);
        webSocketFacade = new WebSocketFacade(serverURL, notificationHandler);
        this.gameID = gameID;
        try{
            webSocketFacade.connect(authToken, gameID);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
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
        if(params.length!=0){
            return "Invalid Command.";
        }
        DrawGame drawGame = new DrawGame(game, viewPoint);
        drawGame.draw();
        return "";
    }

    private String leave(String[] params){
        try {
            webSocketFacade.leave(authToken, gameID);
        } catch (Exception e) {
            return e.getMessage();
        }
        repl.leaveGame(authToken);
        return "Successfully left game.";
    }

    private String move(String[] params){
        String letters = "abcdefgh";
        if(params.length!=2){
            return "Invalid Command.";
        }

        ChessPosition[] positions = new ChessPosition[2];
        for(int i=0; i<2;i++){
            if(params[0].length()!=2){
                return "Location must consist of 1 letter between A and H and 1 number between 1 and 8";
            }
            int x = letters.indexOf(params[i].charAt(0));
            int y = Character.getNumericValue(params[i].charAt(1));
            if(x == -1 || y<1 || y>8){
                return "Location must consist of 1 letter between A and H and 1 number between 1 and 8";
            }
            positions[i] = new ChessPosition(y, x+1);
        }

        ChessMove move = new ChessMove(positions[0], positions[1], null);
        try {
            webSocketFacade.makeMove(authToken, gameID, move);
        } catch (Exception e) {
            return e.getMessage();
        }
        return "";
    }

    private String resign(String[] params){
        return "Not implemented yet";
    }

    private String legal(String[] params){
        return "Not implemented yet";
    }

    @Override
    public String help(){
        return """
                - redraw - redraw the chessboard
                - leave - leaves the game and returns to logged in menu
                - move <start Letter><start Number> <end Letter><end Number> - moves piece from start to end
                - resign - resigns game and leaves
                - legal - highlights all legal moves
                - help
                """;
    }

    public void updateChessGame(ChessGame game){
        this.game = game;
    }
}
