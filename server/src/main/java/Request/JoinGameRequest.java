package Request;

import chess.ChessGame;

public record JoinGameRequest(String authToken, Integer gameID, ChessGame.TeamColor playerColor) {
}
