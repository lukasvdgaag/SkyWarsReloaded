package net.gcnt.skywarsreloaded.utils.results;

public enum SpawnAddResult {

    INDEX_TOO_LOW(false),
    INDEX_TOO_HIGH(false),
    NEW_TEAM_ADDED(true),
    TEAM_UPDATED(true),
    SPAWN_ALREADY_EXISTS(false),
    MAX_TEAM_SPAWNS_REACHED(false);

    private final boolean success;

    SpawnAddResult(boolean success) {
        this.success = success;
    }

    public boolean isSuccess() {
        return success;
    }
}
