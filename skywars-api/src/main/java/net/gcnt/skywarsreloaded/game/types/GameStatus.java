package net.gcnt.skywarsreloaded.game.types;

public enum GameStatus {

    DISABLED(false),
    WAITING_LOBBY(true),
    WAITING_CAGES(true),
    COUNTDOWN(true),
    PLAYING(false),
    ENDING(false);

    private final boolean joinable;

    GameStatus(boolean canJoin) {
        this.joinable = canJoin;
    }

    public boolean isJoinable() {
        return joinable;
    }
}
