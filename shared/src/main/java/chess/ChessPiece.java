package chess;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Objects;

/**
 * Represents a single chess piece
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessPiece {

    private final ChessPiece.PieceType type;
    private final ChessGame.TeamColor pieceColor;

    public ChessPiece(ChessGame.TeamColor pieceColor, ChessPiece.PieceType type) {
        this.type = type;
        this.pieceColor = pieceColor;
    }

    /**
     * The various different chess piece options
     */
    public enum PieceType {
        KING,
        QUEEN,
        BISHOP,
        KNIGHT,
        ROOK,
        PAWN
    }

    @Override
    public String toString() {
        if(pieceColor == ChessGame.TeamColor.WHITE){
            if(type == PieceType.KING){
                return "K";
            }
            else if(type == PieceType.QUEEN){
                return "Q";
            }
            else if(type == PieceType.ROOK){
                return "R";
            }
            else if(type == PieceType.KNIGHT){
                return "N";
            }
            else if(type == PieceType.BISHOP){
                return "B";
            }
            else if(type == PieceType.PAWN){
                return "P";
            }
        }
        if(pieceColor == ChessGame.TeamColor.BLACK){
            if(type == PieceType.KING){
                return "k";
            }
            else if(type == PieceType.QUEEN){
                return "q";
            }
            else if(type == PieceType.ROOK){
                return "r";
            }
            else if(type == PieceType.KNIGHT){
                return "n";
            }
            else if(type == PieceType.BISHOP){
                return "b";
            }
            else if(type == PieceType.PAWN){
                return "p";
            }
        }
        return "ERROR";
    }

    /**
     * @return Which team this chess piece belongs to
     */
    public ChessGame.TeamColor getTeamColor() {
        return pieceColor;
    }

    /**
     * @return which type of chess piece this piece is
     */
    public PieceType getPieceType() {
        return type;
    }

    /**
     * Calculates all the positions a chess piece can move to
     * Does not take into account moves that are illegal due to leaving the king in
     * danger
     *
     * @return Collection of valid moves
     */
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        ArrayList<ChessMove> moves = new ArrayList<ChessMove>();
        int x = myPosition.getRow();
        int y = myPosition.getColumn();
        if(type == PieceType.BISHOP){
            for(int i=0; i<4;i++){
                int counter = 1;
                boolean tryNext = true;
                while(tryNext){
                    int deltax = (2*(i/2)-1)*counter;
                    int deltay = (2*(i%2)-1)*counter;
                    ChessPosition destination = new ChessPosition(x+deltax, y+deltay);
                    if(x+deltax>8 || x+deltax<1 || y+deltay>8 || y+deltay<1){
                        tryNext = false;
                    }
                    else if(board.getPiece(destination)!=null){
                        tryNext = false;
                        if(board.getPiece(destination).pieceColor != pieceColor){
                            moves.add(new ChessMove(myPosition, destination, null));
                        }
                    }
                    else{
                        moves.add(new ChessMove(myPosition, destination, null));
                    }
                    counter++;
                }
            }
        }
        return moves;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ChessPiece that = (ChessPiece) o;
        return type == that.type && pieceColor == that.pieceColor;
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, pieceColor);
    }
}
