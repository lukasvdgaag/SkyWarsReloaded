package net.gcnt.skywarsreloaded.game.cages;

import net.gcnt.skywarsreloaded.AbstractSkyWarsReloaded;
import net.gcnt.skywarsreloaded.game.TeamCage;
import net.gcnt.skywarsreloaded.game.TeamSpawn;
import net.gcnt.skywarsreloaded.utils.SWCompletableFuture;

public abstract class AbstractNormalTeamCage implements TeamCage {

    protected final AbstractSkyWarsReloaded main;
    protected final TeamSpawn spawn;

    private boolean placed;

    public AbstractNormalTeamCage(AbstractSkyWarsReloaded mainIn, TeamSpawn spawn) {
        this.main = mainIn;
        this.spawn = spawn;
        this.placed = false;
    }

    @Override
    public TeamSpawn getSpawn() {
        return this.spawn;
    }

    @Override
    public abstract SWCompletableFuture<Boolean> placeCage(String cageId);

    public abstract boolean placeCageNow(String cage, String material);

    @Override
    public abstract SWCompletableFuture<Boolean> removeCage(String cage);

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
