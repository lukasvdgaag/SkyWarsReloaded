package net.gcnt.skywarsreloaded.game.cages;

import net.gcnt.skywarsreloaded.AbstractSkyWarsReloaded;
import net.gcnt.skywarsreloaded.game.TeamCage;
import net.gcnt.skywarsreloaded.game.TeamSpawn;

import java.util.concurrent.CompletableFuture;

public abstract class AbstractNormalCage implements TeamCage {

    protected final AbstractSkyWarsReloaded main;
    protected final TeamSpawn spawn;

    private boolean placed;

    public AbstractNormalCage(AbstractSkyWarsReloaded mainIn, TeamSpawn spawn) {
        this.main = mainIn;
        this.spawn = spawn;
        this.placed = false;
    }

    @Override
    public TeamSpawn getSpawn() {
        return this.spawn;
    }

    @Override
    public abstract CompletableFuture<Boolean> placeCage(String cageId);

    public abstract boolean placeCageNow(String cage, String material);

    @Override
    public abstract CompletableFuture<Boolean> removeCage(String cage);

    public abstract boolean removeCageNow(String cage);

    @Override
    public boolean isPlaced() {
        return placed;
    }

    @Override
    public void setPlaced(boolean placed) {
        this.placed = placed;
    }


}
