package net.gcnt.skywarsreloaded.game.types;

public enum GameState {

    DISABLED(false, false),
    WAITING_LOBBY(true, true),
    WAITING_CAGES(true, true),
    COUNTDOWN(true, true),
    PLAYING(false, false),
    ENDING(false, false),
    EDIT_MODE(false, false);

    private final boolean joinable;
    private final boolean waiting;

    GameState(boolean canJoin, boolean waiting) {
        this.joinable = canJoin;
        this.waiting = waiting;
    }

    public boolean isJoinable() {
        return joinable;
    }

    public boolean isWaiting() {
        return waiting;
    }
}
