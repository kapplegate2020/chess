package ui;

import chess.ChessGame;
import chess.ChessMove;
import chess.ChessPiece;
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
    boolean checkResign = false;

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
        String toReturn = switch (command) {
            case "redraw" -> redraw(params);
            case "leave" -> leave(params);
            case "move" -> move(params);
            case "resign" -> checkResign(params);
            case "yes" -> resign(params);
            case "legal" -> legal(params);
            default -> help();
        };
        if(!command.equals("resign")){
            checkResign = false;
        }
        return toReturn;
    }

    private String redraw(String[] params){
        if(params.length!=0){
            return "Invalid Command.";
        }
        System.out.println(game.getBoard());
        DrawGame drawGame = new DrawGame(game, viewPoint);
        drawGame.draw(null);
        return "";
    }

    private String leave(String[] params){
        if(params.length!=0){
            return "Invalid Command.";
        }
        try {
            webSocketFacade.leave(authToken, gameID);
        } catch (Exception e) {
            return e.getMessage();
        }
        repl.leaveGame(authToken);
        return "Successfully left game.";
    }

    private String move(String[] params){
        if(params.length!=2){
            return "Invalid Command.";
        }

        ChessPosition[] positions = new ChessPosition[2];
        for(int i=0; i<2;i++){
            ChessPosition position = getPosition(params[i]);
            if(position !=null){
                positions[i] = position;
            }
            else{
                return "Location must consist of 1 letter between A and H and 1 number between 1 and 8";
            }
        }

        ChessMove move = new ChessMove(positions[0], positions[1], null);
        try {
            webSocketFacade.makeMove(authToken, gameID, move);
        } catch (Exception e) {
            return e.getMessage();
        }
        return "";
    }

    private String checkResign(String[] params){
        if(params.length!=0){
            return "Invalid Command.";
        }
        checkResign = true;
        return "Are you sure you want to resign? [yes|no]";
    }

    private String resign(String[] params){
        if(params.length!=0){
            return "Invalid Command.";
        }
        if(checkResign){
            try {
                webSocketFacade.resign(authToken, gameID);
                return "";
            } catch (Exception e) {
                return e.getMessage();
            }
        }
        return help();
    }

    private String legal(String[] params){
        if(params.length!=1){
            return "Invalid Command.";
        }
        ChessPosition position = getPosition(params[0]);
        if(position == null){
            return "Location must consist of 1 letter between A and H and 1 number between 1 and 8";
        }
        DrawGame drawGame = new DrawGame(game, viewPoint);
        drawGame.draw(position);
        return "";
    }

    @Override
    public String help(){
        return """
                - redraw - redraw the chessboard
                - leave - leaves the game and returns to logged in menu
                - move <start Letter><start Number> <end Letter><end Number> - moves piece from start to end
                - resign - resigns game and leaves
                - legal <start Letter><start Number> - highlights all legal moves
                - help
                """;
    }

    public void updateChessGame(ChessGame game){
        this.game = game;
    }

    private ChessPosition getPosition(String positionStr){
        String letters = "abcdefgh";
        if(positionStr.length()!=2){
            return null;
        }
        int x = letters.indexOf(positionStr.charAt(0));
        int y = Character.getNumericValue(positionStr.charAt(1));
        if(x == -1 || y<1 || y>8){
            return null;
        }
        return new ChessPosition(y, x+1);
    }
}
