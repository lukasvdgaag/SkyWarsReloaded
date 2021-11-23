package net.gcnt.skywarsreloaded.utils.results;

public class SpawnRemoveResult {

    private final boolean success;
    private final int team;
    private final int index;

    public SpawnRemoveResult(boolean success, int team, int index) {
        this.success = success;
        this.team = team;
        this.index = index;
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
}
