package net.gcnt.skywarsreloaded.game;

import net.gcnt.skywarsreloaded.wrapper.SWPlayer;

public interface GamePlayer {

    SWPlayer getSWPlayer();

    boolean isAlive();

    void setAlive(boolean alive);

    GameWorld getGame();

    GameTeam getTeam();

}
