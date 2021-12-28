package net.gcnt.skywarsreloaded.game;

import net.gcnt.skywarsreloaded.SkyWarsReloaded;
import net.gcnt.skywarsreloaded.game.types.GameStatus;
import net.gcnt.skywarsreloaded.utils.Item;
import net.gcnt.skywarsreloaded.utils.SWCoord;
import net.gcnt.skywarsreloaded.wrapper.player.SWPlayer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public abstract class AbstractGameWorld implements GameWorld {

    public final SkyWarsReloaded plugin;
    public final String id;
    public final GameTemplate gameTemplate;
    public List<GameTeam> teams;

    public String worldName;
    public List<GamePlayer> players;
    public GameStatus status;
    public int timer;
    public boolean editing;

    public AbstractGameWorld(SkyWarsReloaded plugin, String id, GameTemplate gameTemplate) {
        this.plugin = plugin;
        this.id = id;
        this.gameTemplate = gameTemplate;
        this.teams = new ArrayList<>();
        this.players = new ArrayList<>();
        this.timer = 0;
        this.status = GameStatus.DISABLED;
        this.worldName = "swr-" + id;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public GameTemplate getTemplate() {
        return this.gameTemplate;
    }

    @Override
    public List<GameTeam> getTeams() {
        return teams;
    }

    @Override
    public String getWorldName() {
        return worldName;
    }

    @Override
    public boolean isEditing() {
        return editing;
    }

    @Override
    public void setEditing(boolean editing) {
        this.editing = editing;
    }

    @Override
    public boolean canJoin() {
        if (!this.status.isJoinable()) return false;
        return true;
    }

    @Override
    public boolean isSpawnAvailable() {
        // checking if there is an empty spawn somewhere.
        return this.players.size() < getTemplate().getTeamSize() * getTeams().size();
    }

    @Override
    public GamePlayer preparePlayerJoin(UUID uuid, boolean ignoreAvailableSpawns) {
        if (!canJoin()) throw new IllegalStateException("Game is not joinable, the main skywars plugins or extensions " +
                "should always check if the instance is joinable before calling this method. (user id: " + uuid +
                " | game id: " + this.id + ")");
        if (!this.isSpawnAvailable() && !ignoreAvailableSpawns)
            throw new IllegalStateException("No spawns are available");
        SWPlayer swp = plugin.getPlayerManager().getPlayerByUUID(uuid);
        if (swp == null) swp = plugin.getPlayerManager().initPlayer(uuid);
        if (swp == null) return null;
        return new CoreGamePlayer(swp, this);
    }

    @Override
    public void addPlayers(GamePlayer... players) {
        this.players.addAll(Arrays.asList(players));
    }

    @Override
    public void removePlayer(GamePlayer player) {
        this.players.remove(player);
    }

    @Override
    public List<GamePlayer> getPlayersCopy() {
        return new ArrayList<>(players);
    }

    @Override
    public List<GamePlayer> getAlivePlayers() {
        return players.stream().filter(GamePlayer::isAlive).collect(Collectors.toList());
    }

    @Override
    public List<GamePlayer> getSpectators() {
        return players.stream().filter(GamePlayer::isSpectating).collect(Collectors.toList());
    }

    @Override
    public GameStatus getStatus() {
        return status;
    }

    @Override
    public void setStatus(GameStatus status) {
        this.status = status;
    }

    @Override
    public int getTimer() {
        return timer;
    }

    @Override
    public void setTimer(int timer) {
        this.timer = timer;
    }

    @Override
    public Item[] generateChestLoot() {
        return new Item[0];
    }

    @Override
    public abstract void fillChest(SWCoord coord);

    @Override
    public void removeCages() {
        teams.forEach(gameTeam -> gameTeam.getSpawns().forEach(
                TeamSpawn::removeCage
        ));
    }
}
