package net.gcnt.skywarsreloaded.bukkit.game;

import net.gcnt.skywarsreloaded.bukkit.BukkitSkyWarsReloaded;
import net.gcnt.skywarsreloaded.game.*;
import net.gcnt.skywarsreloaded.utils.Coord;
import net.gcnt.skywarsreloaded.wrapper.SWPlayer;
import org.bukkit.Bukkit;
import org.bukkit.World;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class BukkitGameWorld implements GameWorld {

    private final BukkitSkyWarsReloaded plugin;
    private final String gameId;
    private final GameTemplate gameData;
    private final List<GamePlayer> spectators;
    private final List<GameTeam> teams;
    private GameStatus status;
    private int timer;
    private String worldName;

    public BukkitGameWorld(BukkitSkyWarsReloaded plugin, GameTemplate gameData) {
        this.plugin = plugin;
        this.gameData = gameData;
        this.gameId = UUID.randomUUID().toString();

        this.spectators = new ArrayList<>();
        this.teams = new ArrayList<>();
        this.status = GameStatus.DISABLED;
        this.timer = 0;
        // todo create a world and edit this.worldName.
    }

    public World getBukkitWorld() {
        return Bukkit.getWorld(this.worldName);
    }

    @Override
    public String getId() {
        return gameId;
    }

    @Override
    public GameTemplate getGame() {
        return gameData;
    }

    @Override
    public List<GameTeam> getTeams() {
        return teams;
    }

    @Override
    public String getWorldName() {
        return this.worldName;
    }

    @Override
    public void addPlayers(SWPlayer... players) {

    }

    @Override
    public void removePlayer(SWPlayer player) {

    }

    @Override
    public List<GamePlayer> getPlayers() {
        List<GamePlayer> players = new ArrayList<>();
        for (GameTeam t : teams) {
            players.addAll(t.getPlayers());
        }
        return players;
    }

    @Override
    public List<GamePlayer> getAlivePlayers() {
        List<GamePlayer> players = new ArrayList<>();
        for (GameTeam t : teams) {
            players.addAll(t.getAlivePlayers());
        }
        return players;
    }

    public List<GamePlayer> getAllPlayers() {
        ArrayList<GamePlayer> prs = new ArrayList<>();
        prs.addAll(getPlayers());
        prs.addAll(getSpectators());
        return prs;
    }

    @Override
    public List<GamePlayer> getSpectators() {
        return this.spectators;
    }

    @Override
    public GameStatus getStatus() {
        return this.status;
    }

    @Override
    public void setStatus(GameStatus status) {
        this.status = status;
    }

    @Override
    public int getTimer() {
        return 0;
    }

    @Override
    public void setTimer(int timer) {
        this.timer = timer;
    }

    @Override
    public String[] generateChestLoot() {
        return new String[0];
    }

    @Override
    public void fillChest(Coord coord) {

    }

    @Override
    public void removeCages() {
        teams.forEach(gameTeam -> gameTeam.getSpawns().forEach(
                TeamSpawn::removeCage
        ));
    }
}
