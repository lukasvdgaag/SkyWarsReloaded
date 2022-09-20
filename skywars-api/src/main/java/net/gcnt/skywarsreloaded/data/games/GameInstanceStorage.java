package net.gcnt.skywarsreloaded.data.games;

import net.gcnt.skywarsreloaded.data.Storage;
import net.gcnt.skywarsreloaded.game.GameTemplate;
import net.gcnt.skywarsreloaded.game.gameinstance.GameInstance;
import net.gcnt.skywarsreloaded.game.gameinstance.LocalGameInstance;
import net.gcnt.skywarsreloaded.game.gameinstance.RemoteGameInstance;

import java.util.List;
import java.util.UUID;

public interface GameInstanceStorage extends Storage {

    List<RemoteGameInstance> fetchGameInstances();

    RemoteGameInstance getGameInstanceById(String uuid);

    List<RemoteGameInstance> getGameInstancesByTemplate(GameTemplate template);

    void updateGameInstance(LocalGameInstance gameInstance);

    void removeOldInstances();

    void removeGameInstance(GameInstance gameInstance);

    void removeGameInstance(UUID uuid);

    void startAutoUpdating();

    void stopAutoUpdating();

}
