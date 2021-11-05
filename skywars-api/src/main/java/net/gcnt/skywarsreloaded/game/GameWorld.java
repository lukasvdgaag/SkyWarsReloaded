package net.gcnt.skywarsreloaded.game;

import net.gcnt.skywarsreloaded.wrapper.SWPlayer;

import java.util.List;

public interface GameWorld {

    String getId();

    GameData getGame();

    List<Team> getTeams();

    String getWorldName();

    void addPlayers(SWPlayer... players);

    void removePlayer(SWPlayer player);

    List<GamePlayer> getPlayers();

    List<GamePlayer> getAlivePlayers();

    List<GamePlayer> getSpectators();

    GameStatus getStatus();

    void setStatus(GameStatus status);

    int getTimer();

    void setTimer(int timer);

}
