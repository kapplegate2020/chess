package Request;

import Result.JoinGameResult;
import chess.ChessGame;

public record JoinGameRequest(String authToken, Integer gameID, ChessGame.TeamColor playerColor) {
    public JoinGameRequest addAuthToken(String authToken){
        return new JoinGameRequest(authToken, gameID, playerColor);
    }
}
