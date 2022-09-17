package net.gcnt.skywarsreloaded.game;

import net.gcnt.skywarsreloaded.SkyWarsReloaded;

public interface GameStateHandler {

    void tick();

    void tickSecond();

    GameInstance getGameInstance();

    SkyWarsReloaded getPlugin();

}
