package model;

import chess.ChessGame;

public record GameData(int gameID, String whiteUsername, String blackUsername, String gameName, ChessGame game) {
    public GameData addBlackUsername(String blackUsername){
        return new GameData(gameID, whiteUsername, blackUsername, gameName, game);
    }
    public GameData addWhiteUsername(String whiteUsername){
        return new GameData(gameID, whiteUsername, blackUsername, gameName, game);
    }
    public GameData updateGame(ChessGame game){
        return new GameData(gameID, whiteUsername, blackUsername, gameName, game);
    }
}
