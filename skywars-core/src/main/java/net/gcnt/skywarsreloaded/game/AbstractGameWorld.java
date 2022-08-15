package net.gcnt.skywarsreloaded.game;

import net.gcnt.skywarsreloaded.SkyWarsReloaded;
import net.gcnt.skywarsreloaded.game.chest.SWChestTier;
import net.gcnt.skywarsreloaded.game.chest.filler.SWChestFillerManager;
import net.gcnt.skywarsreloaded.game.state.EndingStateHandler;
import net.gcnt.skywarsreloaded.game.state.PlayingStateHandler;
import net.gcnt.skywarsreloaded.game.types.ChestType;
import net.gcnt.skywarsreloaded.game.types.GameState;
import net.gcnt.skywarsreloaded.game.types.TeamColor;
import net.gcnt.skywarsreloaded.party.SWParty;
import net.gcnt.skywarsreloaded.utils.CoreSWCCompletableFuture;
import net.gcnt.skywarsreloaded.utils.Message;
import net.gcnt.skywarsreloaded.utils.SWCompletableFuture;
import net.gcnt.skywarsreloaded.utils.SWCoord;
import net.gcnt.skywarsreloaded.utils.properties.ConfigProperties;
import net.gcnt.skywarsreloaded.utils.properties.MessageProperties;
import net.gcnt.skywarsreloaded.wrapper.entity.SWPlayer;

import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public abstract class AbstractGameWorld implements GameWorld {

    // Static properties
    protected final SkyWarsReloaded plugin;
    protected final String id;
    protected final GameTemplate gameTemplate;
    protected final String worldName;
    // Player data
    protected final List<GamePlayer> players;
    protected final List<GameTeam> teams;
    protected final HashMap<UUID, SWChestTier> votedChestTiers;
    protected final HashMap<UUID, ChestType> selectedEditingChestType;
    protected GameTeam winningTeam;
    protected GameScheduler scheduler;
    // Map data
    protected ChestType chestType;
    // States
    protected boolean editing;
    protected GameState state;
    protected int timer;

    public AbstractGameWorld(SkyWarsReloaded plugin, String id, GameTemplate gameTemplate) {
        this.plugin = plugin;
        this.id = id;
        this.gameTemplate = gameTemplate;
        this.teams = new ArrayList<>();
        this.players = new ArrayList<>();
        this.votedChestTiers = new HashMap<>();
        this.selectedEditingChestType = new HashMap<>();
        this.state = GameState.DISABLED;
        this.timer = 0;
        this.worldName = "swr-" + id;
        this.winningTeam = null;
        this.chestType = ChestType.ISLAND;
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
    public GameTeam getTeam(GamePlayer player) {
        for (GameTeam gt : teams) {
            if (gt.isMember(player.getSWPlayer())) return gt;
        }
        return null;
    }

    @Override
    public GamePlayer getPlayer(SWPlayer player) {
        for (GamePlayer gp : players) {
            if (gp.getSWPlayer().equals(player)) return gp;
        }
        return null;
    }

    @Override
    public String getWorldName() {
        return worldName;
    }

    public ChestType getGameDifficulty() {
        return this.chestType;
    }

    public void setGameDifficulty(ChestType chestTypeIn) {
        this.chestType = chestTypeIn;
    }

    @Override
    public void startScheduler() {
        if (this.scheduler != null) this.scheduler.end();
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
    public Map<UUID, SWChestTier> getVotedChestTiers() {
        return this.votedChestTiers;
    }

    private int getObjectCount(Collection<?> collection, Object object) {
        int count = 0;
        for (Object o : collection) {
            if ((o == null && object == null) || (o != null && o.equals(object))) count++;
        }
        return count;
    }

    @Override
    public SWChestTier getChestTier() {
        final Collection<SWChestTier> values = this.votedChestTiers.values();
        return values.stream()
                .max((o1, o2) -> getObjectCount(values, o2) - getObjectCount(values, o1))
                .orElse(plugin.getChestManager().createChestTier("default"));
    }

    @Override
    public void setChestTypeSelected(UUID player, SWChestTier type) {
        this.votedChestTiers.put(player, type);
    }

    @Override
    public boolean canJoinGame() {
        return this.state.isJoinable() && isSpawnAvailable();
    }

    @Override
    public boolean isSpawnAvailable() {
        // checking if there is an empty spawn somewhere.
        return this.players.size() < getTemplate().getTeamSize() * getTeams().size();
    }

    @Override
    public GamePlayer preparePlayerJoin(UUID uuid, boolean ignoreAvailableSpawns) {
        if (!canJoinGame())
            throw new IllegalStateException("Game is not joinable, the main skywars plugins or extensions " + "should always check if the instance is joinable before calling this method. (user id: " + uuid + " | game id: " + this.id + ")");

        if (!this.isSpawnAvailable() && !ignoreAvailableSpawns) throw new IllegalStateException("No spawns are available");

        SWPlayer swp = plugin.getPlayerManager().getPlayerByUUID(uuid);
        if (swp == null) swp = plugin.getPlayerManager().getPlayerByUUID(uuid);
        if (swp == null) return null;

        return new CoreGamePlayer(swp, this);
    }

    @Override
    public boolean addPlayers(GamePlayer... players) {
        // Select the default chest type to be placed if the player is joining a map that is being edited.
        ChestType defaultChestType = getDefaultChestType();

        boolean successState = true;
        int index = 0;
        for (GamePlayer gamePlayer : players) {
            // todo handle reservations - check if specific player can join if reserved
            if (!canJoinGame()) {
                successState = false;
                continue;
            }

            // Apply the default chest type to the gamePlayer
            final SWPlayer swPlayer = gamePlayer.getSWPlayer();
            if (this.editing) {
                this.selectedEditingChestType.put(swPlayer.getUuid(), defaultChestType);

                // Add the gamePlayer to the game
                this.players.add(gamePlayer);
                swPlayer.setGameWorld(this);

                continue;
            }

            // Get list of available games to join ordered from lowest - highest players.
            // Re-calculate this every time to make sure the emptiest teams are picked most.
            List<GameTeam> gameTeams = getTeamsOrderedByPlayerCountIncreasing();

            if (gameTeams.size() == 0) {
                successState = false;
                continue;
            }

            // Get team best suited for the player
            GameTeam gameTeam = findPreferredTeam(swPlayer, gameTeams);

            if (gameTeam == null) {
                successState = false;
                continue;
            }

            // Add the gamePlayer to the game
            this.players.add(gamePlayer);
            gameTeam.addPlayer(gamePlayer);
            TeamSpawn spawn = gameTeam.getSpawn(gamePlayer);
            if (spawn == null) spawn = gameTeam.assignSpawn(gamePlayer);
            swPlayer.setGameWorld(this);

            // Start placing the cage
            SWCompletableFuture<Boolean> cagePlaceFuture = spawn.updateCage();

            // Teleport player to next available spawn
            swPlayer.freeze();
            TeamSpawn finalSpawn = spawn;
            cagePlaceFuture.thenRunSync(() -> teleportPlayerToLobbyOrTeamSpawn(swPlayer, finalSpawn).thenRunSync(() -> {
                preparePlayer(swPlayer);
                swPlayer.unfreeze();
            }));
//
//            teleportPlayerToLobbyOrTeamSpawn(swPlayer, spawn)
//                    .thenRun(() -> cagePlaceFuture.thenRunSync(swPlayer::unfreeze));

            announce(plugin.getMessages().getMessage(MessageProperties.GAMES_PLAYER_JOINED.toString()).replace("%player%", gamePlayer.getSWPlayer().getName()).replace("%players%", getWaitingPlayers().size() + "").replace("%maxplayers%", this.gameTemplate.getMaxPlayers() + ""));

            // todo give vote and other game items here.
            // todo teleport gamePlayer to team spawn / waiting spawn here.
        }
        return successState;
    }

    @Override
    public void preparePlayer(SWPlayer player) {
        player.clearInventory();
        player.setHealth(20);
        player.setFoodLevel(20);
        player.clearBodyArrows();
        player.setFireTicks(0);

        GamePlayer gp = getPlayer(player);
        if (gp != null && gp.isSpectating()) {
            player.setAllowFlight(true);
            player.setFlying(true);
            player.setGameMode(3);
        } else {
            player.setFlying(false);
            player.setAllowFlight(false);

            if (state == GameState.PLAYING) {
                player.setGameMode(0);
            } else {
                // adventure mode when waiting/ending
                player.setGameMode(2);
            }
        }
    }

    public GameTeam findPreferredTeam(SWPlayer swPlayer, List<GameTeam> gameTeams) {
        SWParty party = swPlayer.getParty();

        // Try to find corresponding team with players in the same party
        if (party != null) {
            boolean teamExistsForParty = false;
            for (GameTeam gameTeam : gameTeams) {
                for (GamePlayer gamePlayer : gameTeam.getPlayers()) {
                    if (gamePlayer.getSWPlayer().getParty() == party) {
                        teamExistsForParty = true;
                        if (gameTeam.canJoin(swPlayer.getUuid())) {
                            return gameTeam;
                        }
                    }
                }
            }
            if (!this.gameTemplate.isAllowedDispersedParties() && teamExistsForParty) return null;
        }

        // Try to find the team with the least players that also has slots that are not reserved
        for (GameTeam gameTeam : gameTeams) {
            if (gameTeam.canJoin(swPlayer.getUuid())) {
                return gameTeam;
            }
        }

        return null;
    }

    /**
     * Teleport the player to the game. This will choose whether the player should be placed
     * in the waiting lobby or directly in the cage automatically.
     *
     * @param swPlayer The player to teleport.
     * @param spawn    The spawn to teleport the player to if the player shouldn't be sent to the map's lobby
     */
    public SWCompletableFuture<Boolean> teleportPlayerToLobbyOrTeamSpawn(SWPlayer swPlayer, TeamSpawn spawn) {
        if (shouldSendPlayerToCages()) {
            SWCoord coord = spawn.getLocation().clone().setWorld(this.getWorld());
            final SWCoord add = coord.add(0.5, 0, 0.5);
            swPlayer.teleport(add);
            return CoreSWCCompletableFuture.completedFuture(this.plugin, true);
        } else {
            SWCoord coord = this.gameTemplate.getWaitingLobbySpawn().clone().setWorld(this.getWorld());
            return swPlayer.teleportAsync(coord.add(0, 0.5, 0));
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
    public List<GamePlayer> getWaitingPlayers() {
        return players.stream().filter(Predicate.not(GamePlayer::isSpectating)).collect(Collectors.toList());
    }

    @Override
    public List<GamePlayer> getSpectators() {
        return players.stream().filter(GamePlayer::isSpectating).collect(Collectors.toList());
    }

    @Override
    public List<GameTeam> getAliveTeams() {
        return teams.stream().filter(Predicate.not(GameTeam::isEliminated)).collect(Collectors.toList());
    }

    @Override
    public GameState getState() {
        return state;
    }

    @Override
    public void setState(GameState state) {
        this.state = state;
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
    public void removeCages() {
        teams.forEach(gameTeam -> gameTeam.getSpawns().forEach(TeamSpawn::removeCage));
    }

    @Override
    public void fillChests() {
        final SWChestFillerManager chestFillerManager = plugin.getChestFillerManager();
        final SWChestTier chestTier = getChestTier();

        for (Map.Entry<SWCoord, ChestType> chest : this.gameTemplate.getChests().entrySet()) {
            chestFillerManager.getFillerByName(this.getChestTier().getMode()).fillChest(chestTier, this, chest.getKey(), chest.getValue());
        }
    }

    @Override
    public void loadTeams() {
        int index = 0;
        final TeamColor[] colors = TeamColor.values();

        for (List<SWCoord> locations : this.gameTemplate.getTeamSpawnpoints()) {
            if (index >= colors.length) index = 0;

            TeamColor color = colors[index];
            GameTeam team = new CoreGameTeam(this.plugin, this, (index + 1) + "", color, locations);
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

    // Private utils

    protected ChestType getDefaultChestType() {
        if (this.editing) {
            Collection<ChestType> chests = this.gameTemplate.getChests().values();
            if (!chests.isEmpty()) {
                for (ChestType chest : chests) {
                    return chest;
                }
            }
        }
        return ChestType.ISLAND;
    }

    private List<GameTeam> getTeamsOrderedByPlayerCountIncreasing() {
        return teams.stream().filter(GameTeam::canJoin).sorted((team1, team2) -> team2.getPlayerCount() - team1.getPlayerCount()) // reverse sort
                .collect(Collectors.toList());
    }

    private boolean shouldSendPlayerToCages() {
        return this.gameTemplate.getTeamSize() == 1 || this.state == GameState.WAITING_CAGES || this.state == GameState.COUNTDOWN;
    }

    @Override
    public GameTeam getWinningTeam() {
        return winningTeam;
    }

    @Override
    public void determineWinner() {
        final List<GameTeam> aliveTeams = getAliveTeams();
        if (aliveTeams.size() == 1) {
            winningTeam = aliveTeams.get(0);
        } else if (!aliveTeams.isEmpty()) {
            // pick random team from the alive teams
            winningTeam = aliveTeams.get(new Random().nextInt(aliveTeams.size()));
        }
    }

    @Override
    public void endGame() {
        setState(GameState.ENDING);
        determineWinner();

        Message message;

        if (getTemplate().getTeamSize() == 1) {
            message = plugin.getMessages().getMessage(MessageProperties.GAMES_SUMMARY.toString());

            String winner = "N/A";
            if (winningTeam != null && winningTeam.getPlayers().size() > 0) {
                winner = winningTeam.getPlayers().get(0).getSWPlayer().getName();
            }

            message.replace("%winner%", winner);
        } else {
            message = plugin.getMessages().getMessage(MessageProperties.GAMES_TEAM_SUMMARY.toString());

            StringBuilder sb = new StringBuilder();
            if (winningTeam != null) {
                for (int i = 0; i < winningTeam.getPlayers().size(); i++) {
                    sb.append(winningTeam.getPlayers().get(i).getSWPlayer().getName());
                    if (i < winningTeam.getPlayers().size() - 1) {
                        sb.append("&7, ");
                    }
                }
            } else {
                sb.append("N/A");
            }

            message.replace("%winning_team%", winningTeam == null ? "N/A" : winningTeam.getName());
            message.replace("%winners%", sb.toString());
        }

        // replacing the top 3 killers placeholders.
        final List<GamePlayer> topKillers = getTopKillers();
        for (int position = 0; position < 3; position++) {
            final int displayPos = position + 1;
            if (topKillers.size() > position) {
                final GamePlayer gamePlayer = topKillers.get(position);
                message.replace("%killer_" + displayPos + "%", gamePlayer.getSWPlayer().getName());
                message.replace("%killer_" + displayPos + "_kills%", String.valueOf(gamePlayer.getKills()));
            } else {
                message.replace("%killer_" + displayPos + "%", "N/A");
                message.replace("%killer_" + displayPos + "_kills%", "0");
            }
        }

        message.sendCentered(players.stream().map(GamePlayer::getSWPlayer).toArray(SWPlayer[]::new));

        Message winTitle = plugin.getMessages().getMessage(MessageProperties.TITLES_GAMES_WON.toString());
        Message lostTitle = plugin.getMessages().getMessage(MessageProperties.TITLES_GAMES_LOST.toString());
        for (GamePlayer gp : players) {
            if (winningTeam != null && winningTeam.getPlayers().contains(gp)) {
                winTitle.sendTitle(gp.getSWPlayer());
            } else {
                lostTitle.sendTitle(gp.getSWPlayer());
            }
        }

        setTimer(plugin.getConfig().getInt(ConfigProperties.GAME_TIMER_ENDING.toString()));
        getScheduler().setGameStateHandler(new EndingStateHandler(plugin, this));
    }

    @Override
    public List<GamePlayer> getTopKillers() {
        return new ArrayList<>(players).stream().sorted((player1, player2) -> player2.getKills() - player1.getKills()).collect(Collectors.toList());
    }

    @Override
    public void startGame() {
        setState(GameState.PLAYING);
        fillChests();
        removeCages();
        for (GamePlayer gp : getWaitingPlayers()) {
            gp.setAlive(true);
            preparePlayer(gp.getSWPlayer());
            gp.getSWPlayer().playSound(gp.getSWPlayer().getLocation(), "BLOCK_NOTE_BLOCK_PLING", 1, 2);
            gp.getSWPlayer().sendTitle("§a§lGOOD LUCK", "§eThe game has started!", 20, 50, 20);
            gp.getSWPlayer().sendMessage("§aThe game has started! §eGood luck!");
        }
        getScheduler().setGameStateHandler(new PlayingStateHandler(plugin, this));
    }
}
