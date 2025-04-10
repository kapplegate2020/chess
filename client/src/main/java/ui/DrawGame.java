package ui;

import chess.ChessGame;
import chess.ChessMove;
import chess.ChessPiece;
import chess.ChessPosition;

import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import static ui.EscapeSequences.*;

public class DrawGame {
    ChessGame game;
    String letters = "ABCDEFGH";
    Integer[] numbers = {1, 2, 3, 4, 5, 6, 7, 8};
    Map<ChessPiece.PieceType, Character> pieces = new HashMap<>();
    String[] whiteBlackAlt= {SET_BG_COLOR_WHITE, SET_BG_COLOR_BLACK};
    String[] greenAlt= {SET_BG_COLOR_GREEN, SET_BG_COLOR_DARK_GREEN};
    Map<ChessGame.TeamColor, String> textColors = new HashMap<>();
    ChessGame.TeamColor viewPoint;


    public DrawGame(ChessGame game, ChessGame.TeamColor viewPoint){
        this.game = game;
        pieces.put(ChessPiece.PieceType.PAWN, 'P');
        pieces.put(ChessPiece.PieceType.KING, 'K');
        pieces.put(ChessPiece.PieceType.QUEEN, 'Q');
        pieces.put(ChessPiece.PieceType.ROOK, 'R');
        pieces.put(ChessPiece.PieceType.BISHOP, 'B');
        pieces.put(ChessPiece.PieceType.KNIGHT, 'N');
        textColors.put(ChessGame.TeamColor.BLACK, SET_TEXT_COLOR_BLUE);
        textColors.put(ChessGame.TeamColor.WHITE, SET_TEXT_COLOR_RED);
        if(viewPoint.equals(ChessGame.TeamColor.BLACK)){
            numbers = new Integer[]{8, 7, 6, 5, 4, 3, 2, 1};
        }
    }

    public void draw(ChessPosition position){
        ArrayList<ChessPosition> movePositions = new ArrayList<>();
        if(position != null) {
            Collection<ChessMove> moves = game.validMoves(position);
            if(moves!= null) {
                for (ChessMove move : moves) {
                    movePositions.add(move.getEndPosition());
                }
            }
        }
        var out = new PrintStream(System.out, true, StandardCharsets.UTF_8);
        out.print(ERASE_SCREEN);
        drawHeader(out);
        for(int i : numbers){
            drawRow(out, i, movePositions, position);
        }
        drawHeader(out);
    }

    public void drawHeader(PrintStream out){
        out.print(SET_BG_COLOR_LIGHT_GREY);
        out.print("   ");
        for(int j : numbers){
            out.print(" ");
            out.print(letters.charAt(j-1));
            out.print(" ");
        }
        out.print("   ");
        out.print(RESET_BG_COLOR);
        out.println();
    }

    public void drawRow(PrintStream out, int i, Collection<ChessPosition> movePositions, ChessPosition start){
        out.print(SET_BG_COLOR_LIGHT_GREY);
        out.print(" ");
        out.print(8-i+1);
        out.print(" ");
        for(int j : numbers){
            ChessPosition position = new ChessPosition(8-i+1, j);
            if(position.equals(start)){
                out.print(SET_BG_COLOR_YELLOW);
            }
            else if(movePositions.contains(position)){
                out.print(greenAlt[(i+j)%2]);
            }
            else {
                out.print(whiteBlackAlt[(i + j) % 2]);
            }

            ChessPiece piece = game.getBoard().getPiece(position);
            char pieceChar= ' ';
            if(piece!=null){
                pieceChar = pieces.get(piece.getPieceType());
                out.print(textColors.get(piece.getTeamColor()));
            }
            out.print(" ");
            out.print(pieceChar);
            out.print(" ");
            out.print(RESET_TEXT_COLOR);
        }
        out.print(SET_BG_COLOR_LIGHT_GREY);
        out.print(" ");
        out.print(8-i+1);
        out.print(" ");
        out.print(RESET_BG_COLOR);
        out.println();
    }
}
