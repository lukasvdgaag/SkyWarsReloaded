package net.gcnt.skywarsreloaded.data.games;

import net.gcnt.skywarsreloaded.game.GameInstance;
import net.gcnt.skywarsreloaded.game.GameTemplate;

import java.util.List;

public interface GameInstanceStorage {

    List<GameInstance> getGameInstances();

    GameInstance getGameInstanceById(String uuid);

    List<GameInstance> getGameInstancesByTemplate(GameTemplate template);

    void addGameInstance(GameInstance gameInstance);

    void removeGameInstance(GameInstance gameInstance);

    void removeGameInstance(String uuid);

}
