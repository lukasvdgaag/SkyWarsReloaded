package net.gcnt.skywarsreloaded.utils.results;

public class SpawnRemoveResult {

    private final boolean success;
    private final int team;
    private final int index;
    private final int remainingSpawns;

    public SpawnRemoveResult(boolean success, int team, int index, int remainingSpawns) {
        this.success = success;
        this.team = team;
        this.index = index;
        this.remainingSpawns = remainingSpawns;
    }

    public int getTeam() {
        return team;
    }

    public int getIndex() {
        return index;
    }

    public boolean isSuccess() {
        return success;
    }

    public int getRemainingSpawns() {
        return remainingSpawns;
    }
}
