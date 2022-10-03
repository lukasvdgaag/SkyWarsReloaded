package net.gcnt.skywarsreloaded.game;

import net.gcnt.skywarsreloaded.SkyWarsReloaded;
import net.gcnt.skywarsreloaded.game.gameinstance.LocalGameInstance;
import net.gcnt.skywarsreloaded.game.types.TeamColor;
import net.gcnt.skywarsreloaded.utils.SWCoord;
import net.gcnt.skywarsreloaded.wrapper.entity.SWPlayer;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class CoreGameTeam implements GameTeam {

    private final SkyWarsReloaded plugin;
    private final LocalGameInstance game;
    private final String name;
    private final TeamColor color;
    private final List<CoreTeamSpawn> spawns;
    private List<GamePlayer> players;
    private final Map<UUID, Long> reservations;

    public CoreGameTeam(SkyWarsReloaded pluginIn, LocalGameInstance game, String name, TeamColor color, List<SWCoord> spawns) {
        this.plugin = pluginIn;
        this.game = game;
        this.name = name;
        this.color = color;
        this.spawns = new ArrayList<>();
        this.players = new ArrayList<>();
        this.reservations = new ConcurrentHashMap<>();

        for (SWCoord coord : spawns) {
            this.spawns.add(new CoreTeamSpawn(this.plugin, this, coord));
        }
    }

    @Override
    public TeamColor getColor() {
        return this.color;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public boolean isMember(SWPlayer player) {
        for (GamePlayer gp : players) {
            if (gp.getSWPlayer().equals(player)) return true;
        }
        return false;
    }

    @Override
    public void addPlayer(GamePlayer player) {
        if (players == null || !canJoin()) return;

        this.players.add(player);
        player.setTeam(this);
    }

    @Override
    public void removePlayer(SWPlayer player) {
        // todo remove the player.
    }

    @Override
    public void rejoin(SWPlayer player) {
        // todo re-add the player.
    }

    @Override
    public List<GamePlayer> getPlayers() {
        return this.players;
    }

    @Override
    public int getPlayerCount() {
        return this.getPlayers().size();
    }

    @Override
    public LocalGameInstance getGameWorld() {
        return this.game;
    }

    @Override
    public int getSize() {
        return this.players.size();
    }

    @Override
    public int getAliveSize() {
        return this.getAlivePlayers().size();
    }

    public List<GamePlayer> getAlivePlayers() {
        return players.stream().filter(GamePlayer::isAlive).collect(Collectors.toList());
    }

    @Override
    public void eliminatePlayer(GamePlayer player) {
        this.getAlivePlayers().remove(player);
        player.setAlive(false);
        player.setSpectating(true);
        player.getSWPlayer().teleport(game.getTemplate().getSpectateSpawn());

        if (game.getAliveTeams().size() <= 1) {
            game.endGame();
        }
    }

    @Override
    public boolean isEliminated() {
        return this.getAlivePlayers().isEmpty();
    }

    @Override
    public boolean canJoin() {
        return this.players.size() + this.getValidReservationCount() < game.getTemplate().getTeamSize();
    }

    @Override
    public boolean canJoin(UUID uuid) {
        // Get values
        int playerCount = this.players.size();
        int reservationCount = this.getValidReservationCount();
        int teamSize = game.getTemplate().getTeamSize();

        // Check if team is already full
        if (playerCount >= teamSize) return false;

        // Check with reservations
        if (playerCount + reservationCount >= teamSize) {
            Long reservationExpireTime = this.reservations.get(uuid);

            // Check if player has a reservation
            if (reservationExpireTime == null) return false;

            // If player's reservation is expired, return false and remove it
            if (reservationExpireTime > System.currentTimeMillis()) {
                this.reservations.remove(uuid);
                return false;
            }

            // Else, fall through to true
        }
        return true;
    }

    @Override
    public TeamSpawn getSpawn(GamePlayer player) {
        for (TeamSpawn spawn : spawns) {
            if (spawn.getPlayers().contains(player)) return spawn;
        }
        return null;
    }

    @Override
    public TeamSpawn assignSpawn(GamePlayer player) {
        for (TeamSpawn spawn : spawns) {
            if (spawn.isOccupied()) continue;

            spawn.addPlayer(player);
            return spawn;
        }
        return null;
    }

    @Override
    public List<? extends TeamSpawn> getSpawns() {
        return this.spawns;
    }

    @Override
    public void resetData() {
        players = new ArrayList<>();
    }

    @Override
    public int getValidReservationCount() {
        long now = System.currentTimeMillis();
        return (int) this.reservations.values().stream().filter(timestamp -> timestamp < now).count();
    }

    @Override
    public void addReservation(UUID uuid, long expireTime) {
        this.reservations.put(uuid, expireTime);
    }

    @Override
    public Long getReservation(UUID uuid) {
        return this.reservations.get(uuid);
    }
}
