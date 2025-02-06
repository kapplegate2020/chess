package chess;

import java.util.ArrayList;
import java.util.Collection;

/**
 * For a class that can manage a chess game, making moves on a board
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessGame {
    TeamColor currentTurn = TeamColor.WHITE;
    ChessBoard board = new ChessBoard();

    public ChessGame() {
        board.resetBoard();
    }

    /**
     * @return Which team's turn it is
     */
    public TeamColor getTeamTurn() {
        return currentTurn;
    }

    /**
     * Set's which teams turn it is
     *
     * @param team the team whose turn it is
     */
    public void setTeamTurn(TeamColor team) {
        currentTurn = team;
    }

    /**
     * Enum identifying the 2 possible teams in a chess game
     */
    public enum TeamColor {
        WHITE,
        BLACK
    }

    /**
     * Gets a valid moves for a piece at the given location
     *
     * @param startPosition the piece to get valid moves for
     * @return Set of valid moves for requested piece, or null if no piece at
     * startPosition
     */
    public Collection<ChessMove> validMoves(ChessPosition startPosition) {
        ChessPiece piece = board.getPiece(startPosition);
        if(board == null){
            return null;
        }

        ArrayList<ChessMove> moves = new ArrayList<>();
        for(ChessMove move : piece.pieceMoves(board, startPosition)){
            ChessBoard potentialBoard = board.copy();
            potentialBoard.addPiece(startPosition, null);
            ChessPosition endPosition = move.getEndPosition();
            potentialBoard.addPiece(endPosition, piece);
            if(!boardIsInCheck(potentialBoard, piece.getTeamColor())){
                System.out.println(potentialBoard);
                moves.add(move);
            }

        }
        return moves;
    }

    /**
     * Makes a move in a chess game
     *
     * @param move chess move to preform
     * @throws InvalidMoveException if move is invalid
     */
    public void makeMove(ChessMove move) throws InvalidMoveException {
        //get vars and check the turn
        ChessPosition startPosition = move.getStartPosition();
        ChessPosition endPosition = move.getEndPosition();
        ChessPiece piece = board.getPiece(startPosition);
        if(piece == null){
            throw new InvalidMoveException("There is no piece at the starting position");
        }
        TeamColor team = piece.getTeamColor();
        ChessPiece.PieceType promotionPiece = move.getPromotionPiece();
        if(team != currentTurn){
            throw new InvalidMoveException("Incorrect team");
        }

        //make sure the move is valid
        boolean isValid = false;
        for(ChessMove validMove : validMoves(startPosition)){
            if(validMove.equals(move)){
                isValid = true;
                break;
            }
        }
        if(!isValid){
            throw new InvalidMoveException("Not a valid move");
        }

        //do the move
        board.addPiece(startPosition, null);
        if(promotionPiece == null){
            board.addPiece(endPosition, piece);
        }
        else{
            board.addPiece(endPosition, new ChessPiece(team, promotionPiece));
        }
        if(currentTurn == TeamColor.BLACK){
            currentTurn = TeamColor.WHITE;
        }else{
            currentTurn = TeamColor.BLACK;
        }


    }


    /**
     * Makes a move in a chess game
     *
     * @param teamColor which team to check
     * @return arrayList of all valid moves for a team
     *
     */
    private Collection<ChessMove> getAllMoves(ChessBoard board, TeamColor teamColor){
        ArrayList<ChessMove> moves = new ArrayList<>();
        for(int i=1; i<=8; i++){
            for(int j=1; j<=8; j++){
                ChessPosition position = new ChessPosition(i, j);
                ChessPiece piece = board.getPiece(position);
                if(piece != null){
                    if(piece.getTeamColor()!= teamColor){
                        ArrayList<ChessMove> pieceMoves = (ArrayList<ChessMove>) piece.pieceMoves(board, position);
                        moves.addAll(pieceMoves);
                    }
                }
            }
        }
        return moves;
    }


    /**
     * Makes a move in a chess game
     *
     * @param board chess board to check
     * @param teamColor which team to check
     * @return True if the specified team is in check
     *
     */
    private boolean boardIsInCheck(ChessBoard board, TeamColor teamColor){
        //first, find the king
        ChessPosition kingLocation = null;
        for(int i=1; i<=8; i++){
            for(int j=1; j<=8; j++){
                ChessPosition position = new ChessPosition(i, j);
                ChessPiece piece = board.getPiece(position);
                if(piece != null){
                    if(piece.getTeamColor()== teamColor && piece.getPieceType()== ChessPiece.PieceType.KING){
                        kingLocation = position;
                    }
                }
            }
        }

        //then, check if anyone is challenging the king
        for(ChessMove move : getAllMoves(board, teamColor)){
            ChessPosition destination = move.getEndPosition();
            if(destination.equals(kingLocation)){
                return true;
            }
        }

        return false;
    }

    /**
     * Determines if the given team is in check
     *
     * @param teamColor which team to check for check
     * @return True if the specified team is in check
     */
    public boolean isInCheck(TeamColor teamColor) {
        return boardIsInCheck(board, teamColor);
    }

    /**
     * Determines if the given team is in checkmate
     *
     * @param teamColor which team to check for checkmate
     * @return True if the specified team is in checkmate
     */
    public boolean isInCheckmate(TeamColor teamColor) {
        throw new RuntimeException("Not implemented");
    }

    /**
     * Determines if the given team is in stalemate, which here is defined as having
     * no valid moves
     *
     * @param teamColor which team to check for stalemate
     * @return True if the specified team is in stalemate, otherwise false
     */
    public boolean isInStalemate(TeamColor teamColor) {
        throw new RuntimeException("Not implemented");
    }

    /**
     * Sets this game's chessboard with a given board
     *
     * @param board the new board to use
     */
    public void setBoard(ChessBoard board) {
        this.board = board;
    }

    /**
     * Gets the current chessboard
     *
     * @return the chessboard
     */
    public ChessBoard getBoard() {
        return board;
    }
}
