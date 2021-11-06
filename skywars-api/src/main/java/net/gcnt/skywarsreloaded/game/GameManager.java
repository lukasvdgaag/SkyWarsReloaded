package net.gcnt.skywarsreloaded.game;

import java.util.List;

public interface GameManager {

    void loadAllGameTemplates();

    GameTemplate getGameTemplateByName(String gameId);

    void deleteGameTemplate(String gameId);

    List<GameTemplate> getGameTemplates();

    GameTemplate createGameTemplate(String gameId);

    GameWorld createGameWorld(GameTemplate data);

    List<GameWorld> getGameWorlds(GameTemplate data);

    List<GameWorld> getGameWorlds();

}
