package com.walrusone.skywarsreloaded.api.impl;

import com.walrusone.skywarsreloaded.api.SWRGameAPI;
import com.walrusone.skywarsreloaded.api.SkywarsReloadedAPI;
import com.walrusone.skywarsreloaded.enums.GameType;
import com.walrusone.skywarsreloaded.game.GameMap;
import com.walrusone.skywarsreloaded.managers.MatchManager;
import org.bukkit.entity.Player;

import java.util.List;

public class SWRGameImpl implements SWRGameAPI {

    private final SkywarsReloadedAPI swrAPI;
    private final MatchManager matchManager;

    public SWRGameImpl(SkywarsReloadedAPI swrAPIIn) {
        this.swrAPI = swrAPIIn;
        this.matchManager = MatchManager.get();
    }

    @Override
    public List<GameMap> getPlayableGames() {
        return GameMap.getPlayableArenas(GameType.ALL);
    }

    @Override
    public List<GameMap> getPlayableGames(GameType type) {
        return GameMap.getPlayableArenas(type);
    }

    @Override
    public List<GameMap> getGames() {
        return GameMap.getMaps();
    }

    @Override
    public List<GameMap> getGames(GameType type) {
        List<GameMap> maps = GameMap.getMaps();
        if (type == GameType.ALL) return maps;
        maps.removeIf(gameMap -> type==GameType.SINGLE ? gameMap.getTeamSize()>1 : gameMap.getTeamSize()==1);
        return maps;
    }

    @Override
    public GameMap getGame(String name) {
        return GameMap.getMap(name);
    }

    @Override
    public List<GameMap> getSortedGames() {
        return GameMap.getSortedArenas();
    }

    @Override
    public GameMap getGame(Player player) {
        return this.matchManager.getPlayerMap(player);
    }

    @Override
    public MatchManager getMatchManager() {
        return matchManager;
    }
}
