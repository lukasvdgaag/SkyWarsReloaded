package net.gcnt.skywarsreloaded.data.games;

import net.gcnt.skywarsreloaded.game.GameTemplate;
import net.gcnt.skywarsreloaded.game.gameinstance.GameInstance;
import net.gcnt.skywarsreloaded.game.gameinstance.RemoteGameInstance;

import java.util.List;

public interface GameInstanceStorage {

    List<RemoteGameInstance> getGameInstances();

    GameInstance getGameInstanceById(String uuid);

    List<GameInstance> getGameInstancesByTemplate(GameTemplate template);

    void addGameInstance(GameInstance gameInstance);

    void removeOldInstances();

    void removeGameInstance(GameInstance gameInstance);

    void removeGameInstance(String uuid);

}
