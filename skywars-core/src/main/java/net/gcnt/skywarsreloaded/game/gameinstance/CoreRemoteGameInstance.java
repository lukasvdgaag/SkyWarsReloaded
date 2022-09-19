package net.gcnt.skywarsreloaded.game.gameinstance;

import net.gcnt.skywarsreloaded.SkyWarsReloaded;
import net.gcnt.skywarsreloaded.game.GameTemplate;
import net.gcnt.skywarsreloaded.game.types.GameState;

import java.util.UUID;

public class CoreRemoteGameInstance extends AbstractGameInstance implements RemoteGameInstance {

    private final String proxyServerName;
    private int playerCount = -1;

    public CoreRemoteGameInstance(SkyWarsReloaded plugin, String proxyServerName, UUID id, GameTemplate gameTemplate, GameState state) {
        super(plugin, id, gameTemplate);
        this.proxyServerName = proxyServerName;
        this.setState(state);
    }

    @Override
    public String getServerProxyName() {
        return this.proxyServerName;
    }

    @Override
    public int getPlayerCount() {
        return playerCount;
    }

    @Override
    public void setPlayerCount(int playerCount) {
        this.playerCount = playerCount;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof GameInstance) {
            return this.getId().equals(((GameInstance) obj).getId());
        }
        return false;
    }
}
