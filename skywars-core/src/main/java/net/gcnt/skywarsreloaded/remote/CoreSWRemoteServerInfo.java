package net.gcnt.skywarsreloaded.remote;

public class CoreSWRemoteServerInfo implements SWRemoteServerInfo {

    private final static int TIMEOUT = 4000; // 4 seconds

    private final String id;
    private final int maxGames;
    private int activeGames;
    private int playerCount;
    private long lastPing;

    public CoreSWRemoteServerInfo(String id, int maxGames) {
        this.id = id;
        this.maxGames = maxGames;
        this.lastPing = -1;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public int getMaxGames() {
        return maxGames;
    }

    @Override
    public int getActiveGames() {
        return activeGames;
    }

    @Override
    public int getFreeGames() {
        return maxGames - activeGames;
    }

    @Override
    public int getPlayerCount() {
        return playerCount;
    }

    @Override
    public boolean isConnectionAlive() {
        return System.currentTimeMillis() - lastPing <= TIMEOUT;
    }

    @Override
    public long getLastPing() {
        return lastPing;
    }

    @Override
    public void update(int activeGames, int playerCount) {
        this.activeGames = activeGames;
        this.playerCount = playerCount;
        this.lastPing = System.currentTimeMillis();
    }
}
