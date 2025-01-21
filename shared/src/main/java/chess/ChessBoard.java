package chess;

import java.util.Arrays;

/**
 * A chessboard that can hold and rearrange chess pieces.
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessBoard {
    private ChessPiece[][] squares = new ChessPiece[8][8];

    @Override
    public String toString() {
        StringBuilder output = new StringBuilder();
        for(int i=8; i>=1; i--){
            output.append("|");
            for(int j=1;j<=8;j++){
                ChessPiece current = getPiece(new ChessPosition(i,j));
                if(current == null){
                    output.append(" ");
                }
                else {
                    output.append(current);
                }
                output.append("|");
            }
            output.append("\n");
        }
        return output.toString();
    }

    public ChessBoard() {
        
    }

    /**
     * Adds a chess piece to the chessboard
     *
     * @param position where to add the piece to
     * @param piece    the piece to add
     */
    public void addPiece(ChessPosition position, ChessPiece piece) {
        squares[position.getRow()-1][position.getColumn()-1] = piece;
    }

    /**
     * Gets a chess piece on the chessboard
     *
     * @param position The position to get the piece from
     * @return Either the piece at the position, or null if no piece is at that
     * position
     */
    public ChessPiece getPiece(ChessPosition position) {
        return squares[position.getRow()-1][position.getColumn()-1];
    }

    /**
     * Sets the board to the default starting board
     * (How the game of chess normally starts)
     */
    public void resetBoard() {
        ChessGame.TeamColor w = ChessGame.TeamColor.WHITE;
        ChessGame.TeamColor b = ChessGame.TeamColor.BLACK;

        //first row
        addPiece(new ChessPosition(1,1), new ChessPiece(w, ChessPiece.PieceType.ROOK));
        addPiece(new ChessPosition(1,2), new ChessPiece(w, ChessPiece.PieceType.KNIGHT));
        addPiece(new ChessPosition(1,3), new ChessPiece(w, ChessPiece.PieceType.BISHOP));
        addPiece(new ChessPosition(1,4), new ChessPiece(w, ChessPiece.PieceType.QUEEN));
        addPiece(new ChessPosition(1,5), new ChessPiece(w, ChessPiece.PieceType.KING));
        addPiece(new ChessPosition(1,6), new ChessPiece(w, ChessPiece.PieceType.BISHOP));
        addPiece(new ChessPosition(1,7), new ChessPiece(w, ChessPiece.PieceType.KNIGHT));
        addPiece(new ChessPosition(1,8), new ChessPiece(w, ChessPiece.PieceType.ROOK));

        //white pawns in second row
        for(int i=1;i<=8;i++){
            addPiece(new ChessPosition(2,i), new ChessPiece(w, ChessPiece.PieceType.PAWN));
        }

        //middle all blank
        for(int i=3; i<=8; i++){
            for(int j=1; j<=8; j++){
                addPiece(new ChessPosition(i,j), null);
            }
        }

        //black pawns in second to last row
        for(int i=1;i<=8;i++){
            addPiece(new ChessPosition(7,i), new ChessPiece(b, ChessPiece.PieceType.PAWN));
        }

        //last row
        addPiece(new ChessPosition(8,1), new ChessPiece(b, ChessPiece.PieceType.ROOK));
        addPiece(new ChessPosition(8,2), new ChessPiece(b, ChessPiece.PieceType.KNIGHT));
        addPiece(new ChessPosition(8,3), new ChessPiece(b, ChessPiece.PieceType.BISHOP));
        addPiece(new ChessPosition(8,4), new ChessPiece(b, ChessPiece.PieceType.QUEEN));
        addPiece(new ChessPosition(8,5), new ChessPiece(b, ChessPiece.PieceType.KING));
        addPiece(new ChessPosition(8,6), new ChessPiece(b, ChessPiece.PieceType.BISHOP));
        addPiece(new ChessPosition(8,7), new ChessPiece(b, ChessPiece.PieceType.KNIGHT));
        addPiece(new ChessPosition(8,8), new ChessPiece(b, ChessPiece.PieceType.ROOK));



    }
}
