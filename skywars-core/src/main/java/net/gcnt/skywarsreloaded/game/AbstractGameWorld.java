package net.gcnt.skywarsreloaded.game;

import net.gcnt.skywarsreloaded.SkyWarsReloaded;
import net.gcnt.skywarsreloaded.game.chest.SWChestType;
import net.gcnt.skywarsreloaded.game.types.GameDifficulty;
import net.gcnt.skywarsreloaded.game.types.GameStatus;
import net.gcnt.skywarsreloaded.utils.Item;
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

        for (GamePlayer player : players) {
            // Apply the default chest type to the player
            if (this.editing) {
                this.selectedChestTypes.put(player.getSWPlayer().getUuid(), defaultChestType);
            }
            // Add the player to the game
            this.players.add(player);
            player.getSWPlayer().setGameWorld(this);
        }
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


}
