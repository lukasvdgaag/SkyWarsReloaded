package com.walrusone.skywarsreloaded.enums;

public enum ScoreVar {
    MAPNAME,
    TIME,
    PLAYERS,
    MAXPLAYERS,
    WINNER,
    WINNER1,
    WINNER2,
    WINNER3,
    WINNER4,
    WINNER5,
    WINNER6,
    WINNER7,
    WINNER8,
    RESTARTTIME,
    CHESTVOTE,
    TIMEVOTE,
    HEALTHVOTE,
    WEATHERVOTE,
    MODIFIERVOTE;

    public static ScoreVar matchType(String string) {
        for (ScoreVar var : ScoreVar.values()) {
            if (var.toString().equalsIgnoreCase(string)) {
                return var;
            }
        }
        return null;
    }
}