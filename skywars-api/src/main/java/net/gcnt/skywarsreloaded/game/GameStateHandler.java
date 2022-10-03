package net.gcnt.skywarsreloaded.game;

import net.gcnt.skywarsreloaded.SkyWarsReloaded;
import net.gcnt.skywarsreloaded.game.gameinstance.LocalGameInstance;

public interface GameStateHandler {

    void tick();

    void tickSecond();

    LocalGameInstance getGameInstance();

    SkyWarsReloaded getPlugin();

}
