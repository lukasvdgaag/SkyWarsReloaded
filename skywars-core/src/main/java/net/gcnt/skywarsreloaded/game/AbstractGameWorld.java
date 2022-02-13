package net.gcnt.skywarsreloaded.game;

import net.gcnt.skywarsreloaded.SkyWarsReloaded;
import net.gcnt.skywarsreloaded.game.chest.SWChestType;
import net.gcnt.skywarsreloaded.game.types.GameDifficulty;
import net.gcnt.skywarsreloaded.game.types.GameStatus;
import net.gcnt.skywarsreloaded.game.types.TeamColor;
import net.gcnt.skywarsreloaded.utils.Item;
import net.gcnt.skywarsreloaded.utils.Message;
import net.gcnt.skywarsreloaded.utils.SWCoord;
import net.gcnt.skywarsreloaded.wrapper.player.SWPlayer;

import java.util.*;
import java.util.stream.Collectors;

public abstract class AbstractGameWorld implements GameWorld {

    // Static properties
    protected final SkyWarsReloaded plugin;
    private final String id;
    private final GameTemplate gameTemplate;
    private final String worldName;
    // Player data
    private final List<GamePlayer> players;
    private final List<GameTeam> teams;
    private final HashMap<UUID, SWChestType> selectedChestTypes;
    private GameScheduler scheduler;
    // Map data
    private GameDifficulty gameDifficulty;
    // States
    private boolean editing;
    private GameStatus status;
    private int timer;

    public AbstractGameWorld(SkyWarsReloaded plugin, String id, GameTemplate gameTemplate) {
        this.plugin = plugin;
        this.id = id;
        this.gameTemplate = gameTemplate;
        this.teams = new ArrayList<>();
        this.players = new ArrayList<>();
        this.selectedChestTypes = new HashMap<>();
        this.status = GameStatus.DISABLED;
        this.timer = 0;
        this.worldName = "swr-" + id;
        loadTeams();
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

    public GameDifficulty getGameDifficulty() {
        return this.gameDifficulty;
    }

    public void setGameDifficulty(GameDifficulty gameDifficultyIn) {
        this.gameDifficulty = gameDifficultyIn;
    }

    @Override
    public void startScheduler() {
        this.scheduler = new CoreGameScheduler(plugin, this);
        this.scheduler.start();
    }

    @Override
    public GameScheduler getScheduler() {
        return scheduler;
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
    public Map<UUID, SWChestType> getSelectedChestTypes() {
        return this.selectedChestTypes;
    }

    @Override
    public void setChestTypeSelected(UUID player, SWChestType type) {
        this.selectedChestTypes.put(player, type);
    }

    @Override
    public boolean canJoin() {
        return this.status.isJoinable() && isSpawnAvailable();
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
    public boolean addPlayers(GamePlayer... players) {
        // Select the default chest type to be placed if the player is joining a map
        // that is being edited.
        SWChestType defaultChestType = null;
        if (this.editing) {
            Collection<SWChestType> chests = this.gameTemplate.getChests().values();
            if (!chests.isEmpty()) {
                for (SWChestType chest : chests) {
                    defaultChestType = chest;
                    break;
                }
            }
        }

        // Get list of available games to join ordered from lowest - highest players.
        List<GameTeam> gameTeams = teams.stream()
                .filter(GameTeam::canJoin)
                .sorted((team1, team2) -> team2.getPlayerCount() - team1.getPlayerCount()) // reverse sort
                .collect(Collectors.toList());

        if (gameTeams.size() == 0) return false;

        int index = 0;
        for (GamePlayer gamePlayer : players) {
            if (!canJoin()) return false;

            // Apply the default chest type to the gamePlayer
            final SWPlayer player = gamePlayer.getSWPlayer();
            if (this.editing) {
                this.selectedChestTypes.put(player.getUuid(), defaultChestType);
            }
            // Add the gamePlayer to the game
            this.players.add(gamePlayer);
            player.setGameWorld(this);

            if (shouldSendPlayerToCages()) {
                GameTeam team = gameTeams.get(index);
                TeamSpawn spawn = team.addPlayer(gamePlayer);

                player.teleport(spawn.getLocation());

                if (!team.canJoin()) index++;
            } else {
                player.teleport(this.gameTemplate.getWaitingLobbySpawn());
            }

            // todo send join message here.

            // todo give vote and other game items here.
            // todo teleport gamePlayer to team spawn / waiting spawn here.
        }
        return true;
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
    public Item[] generateChestLoot(SWChestType chestType) {
        HashMap<GameDifficulty, HashMap<Integer, Item>> contents = chestType.getAllContents();
        HashMap<Integer, Item> items = contents.get(this.gameDifficulty);
        return new Item[0]; // todo
    }

    @Override
    public void removeCages() {
        teams.forEach(gameTeam -> gameTeam.getSpawns().forEach(TeamSpawn::removeCage));
    }

    @Override
    public void fillChests() {
        for (Map.Entry<SWCoord, SWChestType> chest : this.gameTemplate.getChests().entrySet()) {
            this.fillChest(chest.getKey(), chest.getValue());
        }
    }

    @Override
    public void loadTeams() {
        int index = 0;
        final TeamColor[] colors = TeamColor.values();

        for (List<SWCoord> locations : this.gameTemplate.getTeamSpawnpoints()) {
            if (index >= colors.length) index = 0;

            TeamColor color = colors[index];
            GameTeam team = new CoreGameTeam(this, (index + 1) + "", color, locations);
            this.teams.add(team);

            index++;
        }
    }

    @Override
    public void announce(String message) {
        for (GamePlayer player : players) {
            if (!player.getSWPlayer().isOnline()) continue;
            player.getSWPlayer().sendMessage(message);
        }
    }

    @Override
    public void announce(Message message) {
        for (GamePlayer player : players) {
            if (!player.getSWPlayer().isOnline()) continue;
            message.send(player.getSWPlayer());
        }
    }

    @Override
    public void announceTitle(Message message) {
        announceTitle(message, 20, 50, 20);
    }

    @Override
    public void announceTitle(Message message, int fadeIn, int stay, int fadeOut) {
        for (GamePlayer player : players) {
            if (!player.getSWPlayer().isOnline()) continue;
            message.sendTitle(fadeIn, stay, fadeOut, player.getSWPlayer());
        }
    }

    private boolean shouldSendPlayerToCages() {
        return this.gameTemplate.getTeamSize() == 1 || this.status == GameStatus.WAITING_CAGES || this.status == GameStatus.COUNTDOWN;
    }
}
