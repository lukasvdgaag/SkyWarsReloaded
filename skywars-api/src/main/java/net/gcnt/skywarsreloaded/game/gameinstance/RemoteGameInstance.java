package net.gcnt.skywarsreloaded.game.gameinstance;

public interface RemoteGameInstance extends GameInstance {

    /**
     * Gets the server name on which the instance is running. This name should be the one set in the proxy
     * configuration file.
     *
     * @return The server name
     */
    String getServerProxyName();

    /**
     * Sets the cached player count for the remote instance.
     *
     * @param playerCount The new player count
     */
    void setPlayerCount(int playerCount);

}
