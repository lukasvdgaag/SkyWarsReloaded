package net.gcnt.skywarsreloaded.game;

import java.util.List;

public interface GameInstance {

    String getId();

    GameTemplate getTemplate();

    List<GameTeam> getTeams();

}
