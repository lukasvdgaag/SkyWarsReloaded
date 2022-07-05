package net.gcnt.skywarsreloaded.listener;

import net.gcnt.skywarsreloaded.SkyWarsReloaded;
import net.gcnt.skywarsreloaded.enums.DeathReason;
import net.gcnt.skywarsreloaded.game.GamePlayer;
import net.gcnt.skywarsreloaded.game.GameTeam;
import net.gcnt.skywarsreloaded.game.GameTemplate;
import net.gcnt.skywarsreloaded.game.GameWorld;
import net.gcnt.skywarsreloaded.game.types.GameState;
import net.gcnt.skywarsreloaded.unlockable.killmessages.KillMessageGroup;
import net.gcnt.skywarsreloaded.utils.results.SpawnRemoveResult;
import net.gcnt.skywarsreloaded.wrapper.entity.SWEntity;
import net.gcnt.skywarsreloaded.wrapper.entity.SWOwnedEntity;
import net.gcnt.skywarsreloaded.wrapper.event.*;
import net.gcnt.skywarsreloaded.wrapper.entity.SWPlayer;
import net.gcnt.skywarsreloaded.wrapper.world.SWWorld;

public class AbstractSWEventListener implements SWEventListener {

    public final SkyWarsReloaded plugin;

    public AbstractSWEventListener(SkyWarsReloaded pluginIn) {
        this.plugin = pluginIn;
    }

    @Override
    public void onAsyncPlayerPreLogin(SWAsyncPlayerPreLoginEvent event) {
        SWPlayer player = this.plugin.getPlayerManager().initPlayer(event.getUUID());
        this.plugin.getStorage().loadData(player);
    }

    @Override
    public void onPlayerJoin(SWPlayerJoinEvent event) {
        event.getPlayer().fetchParentPlayer();
    }

    @Override
    public void onAsyncPlayerChat(SWAsyncPlayerChatEvent event) {

    }

    @Override
    public void onPlayerQuit(SWPlayerQuitEvent event) {
        this.plugin.getPlayerManager().removePlayer(event.getPlayer());
    }

    @Override
    public void onPlayerInteract(SWPlayerInteractEvent event) {
        if (event.getClickedBlockType().toLowerCase().contains("chest")) {
            if (event.getAction() == SWPlayerInteractEvent.Action.LEFT_CLICK_BLOCK) {
                event.getPlayer().sendMessage("opening chest.");
                plugin.getNMSManager().getNMS().setChestOpen(event.getClickedBlockLocation(), true);
            }
        }
    }

    private boolean cancelWhenWaitingInGame(SWPlayer player, SWCancellable cancellable) {
        if (player == null) return false;
        GameWorld gameWorld = player.getGameWorld();
        if (gameWorld == null) return false;

        if (gameWorld.getState().isWaiting() || gameWorld.getState() == GameState.ENDING) {
            cancellable.setCancelled(true);
            return true;
        }
        return false;
    }

    private boolean cancelWhenWaitingInGame(SWPlayerEvent event) {
        if (!(event instanceof SWCancellable)) return false;

        return cancelWhenWaitingInGame(event.getPlayer(), (SWCancellable) event);
    }

    @Override
    public void onPlayerBlockBreak(SWBlockBreakEvent event) {
        if (cancelWhenWaitingInGame(event)) return;

        GameWorld gameWorld = plugin.getGameManager().getGameWorldByName(event.getCoord().getWorld().getName());
        if (gameWorld == null || !gameWorld.isEditing()) return;
        final GameTemplate template = gameWorld.getTemplate();

        if (event.getBlockTypeName().equalsIgnoreCase("CHEST") || event.getBlockTypeName().equalsIgnoreCase("TRAPPED_CHEST")) {
            // player is removing a chest.
            boolean res = template.removeChest(event.getCoord());
            if (res) {
                event.getPlayer().sendTitle(plugin.getUtils().colorize("&c&lCHEST REMOVED"), plugin.getUtils().colorize("&7Removed a chest from the template!"), 5, 30, 5);
                event.getPlayer().sendMessage(plugin.getUtils().colorize(String.format("&cRemoved a chest from game template &7%s&c. There's &7%d &cleft.", template.getDisplayName(), template.getChests().size())));
                template.checkToDoList(event.getPlayer());
            }
        } else if (event.getBlockTypeName().equalsIgnoreCase("BEACON")) {
            // player is removing a player spawnpoint.
            SpawnRemoveResult res = template.removeSpawn(event.getCoord());

            if (res.isSuccess()) {
                int currentSpawns = res.getRemainingSpawns();

                event.getPlayer().sendTitle(plugin.getUtils().colorize("&c&lSPAWN REMOVED"), plugin.getUtils().colorize("&7Removed a player spawn from the template!"), 5, 30, 5);
                String message;
                if (template.getTeamSize() == 1) {
                    message = String.format("&cRemoved player spawnpoint &7#%d &cfrom game template &b%s&a.", res.getTeam() + 1, template.getDisplayName());
                } else {
                    message = String.format("&cRemoved player spawnpoint &7#%d &cfrom team &7%d &cfor game template &7%s&c. &e%d &cspawns left to set for this team.", res.getIndex() + 1, res.getTeam() + 1, template.getDisplayName(), template.getTeamSize() - currentSpawns);
                }
                event.getPlayer().sendMessage(plugin.getUtils().colorize(message));
                template.checkToDoList(event.getPlayer());
            }
        }
    }

    @Override
    public void onPlayerBlockPlace(SWBlockPlaceEvent event) {
        if (cancelWhenWaitingInGame(event)) return;

        // player is placing a chest
        if (event.getBlockTypeName().equalsIgnoreCase("CHEST") ||
                event.getBlockTypeName().equalsIgnoreCase("TRAPPED_CHEST")) {
            GameWorld gameWorld = plugin.getGameManager().getGameWorldByName(event.getCoord().getWorld().getName());
            if (gameWorld == null || !gameWorld.isEditing()) return;

            final GameTemplate template = gameWorld.getTemplate();
            boolean res = template.addChest(event.getCoord().asBlock(), gameWorld.getSelectedChestTypes().getOrDefault(event.getPlayer().getUuid(), plugin.getChestManager().getChestTypeByName("normal")));
            if (res) {
                event.getPlayer().sendTitle(plugin.getUtils().colorize("&a&lCHEST ADDED"),
                        plugin.getUtils().colorize("&7Added a new chest to the template!"), 5, 30, 5);
                event.getPlayer().sendMessage(plugin.getUtils().colorize(
                        String.format("&aAdded a new chest (&b#%d&a) to game template &b%s&a.",
                                template.getChests().size(), template.getDisplayName())));
                template.checkToDoList(event.getPlayer());
            }
        }

    }

    @Override
    public void onPlayerDeath(SWPlayerDeathEvent event) {
        SWPlayer player = event.getPlayer();
        final GameWorld gameWorld = player.getGameWorld();

        if (gameWorld == null) return;

        player.setHealth(20);
        event.setDeathMessage(null);
        event.setKeepInventory(false);
    }

    @Override
    public void onEntityDamage(SWEntityDamageEvent event) {
        if (!(event.getEntity() instanceof SWPlayer)) return;

        SWPlayer player = (SWPlayer) event.getEntity();
        if (cancelWhenWaitingInGame(player, event)) return;
        GameWorld gameWorld = player.getGameWorld();
        if (gameWorld == null) return;

        GamePlayer gamePlayer = gameWorld.getPlayer(player);
        if (gamePlayer.isSpectating()) {
            event.setCancelled(true);
            return;
        }
        // todo fixed damage getting cancelled regardless.

        if (gameWorld.getState() != GameState.PLAYING) return;

        if (player.getHealth() - event.getFinalDamage() > 0) return;

        final GameTeam team = gamePlayer.getTeam();
        if (team == null) return;

        // preparing the player for spectate.
        event.setDamage(0);
        team.eliminatePlayer(gamePlayer);
        gameWorld.preparePlayer(player);

        DeathReason reason = event.getCause();
        if (reason == null) reason = DeathReason.DEFAULT;

        SWPlayer tagged = gamePlayer.getLastTaggedBy();

        // sending the death message.
        String message;
        if (tagged != null) {
            if (!reason.isKill()) {
                reason = DeathReason.fromString(reason.name() + "_KILL");
                if (reason == null) reason = DeathReason.DEFAULT_KILL;
            }

            // selecting the kill messages of the killer.
            final KillMessageGroup killMessageGroup = plugin.getUnlockablesManager().getKillMessageGroup(tagged.getPlayerData().getKillMessagesTheme());
            message = killMessageGroup.getRandomMessage(reason, tagged);
            message = message.replace("%killer%", tagged.getName());
        } else {
            final KillMessageGroup killMessageGroup = plugin.getUnlockablesManager().getKillMessageGroup(player.getPlayerData().getKillMessagesTheme());
            message = killMessageGroup.getRandomMessage(reason, null);
        }
        message = message.replace("%player%", player.getName());

        gameWorld.announce(plugin.getUtils().colorize(message));

        player.sendTitle("§c§lYOU DIED", "§7You are now a spectator", 5, 30, 5);
        // todo customize this message.
    }

    @Override
    public void onEntityDamageByEntity(SWEntityDamageByEntityEvent event) {
        if (!(event.getEntity() instanceof SWPlayer)) return;

        SWPlayer player = (SWPlayer) event.getEntity();
        if (cancelWhenWaitingInGame(player, event)) return;

        SWEntity damager = event.getDamager();

        SWPlayer tagger = null;
        if (damager instanceof SWPlayer) {
            tagger = (SWPlayer) damager;
        } else if (damager instanceof SWOwnedEntity) {
            tagger = ((SWOwnedEntity) damager).getOwner();
        }

        if (tagger != null && !tagger.equals(player)) {
            final GamePlayer gamePlayer = player.getGameWorld().getPlayer(player);
            gamePlayer.setLastTaggedBy(tagger);
        }
    }

    @Override
    public void onPlayerFoodLevelChange(SWPlayerFoodLevelChangeEvent event) {
        cancelWhenWaitingInGame(event);
        event.setFoodLevel(20);
    }

    @Override
    public void onChunkLoad(SWChunkLoadEvent swEvent) {

    }

    @Override
    public void onWorldInit(SWWorldInitEvent swEvent) {
        SWWorld world = swEvent.getWorld();
        if (this.plugin.getGameManager().getGameTemplateByName(world.getName()) != null) {
            world.setKeepSpawnLoaded(false);
        }
    }

    @Override
    public void onPlayerMove(SWPlayerMoveEvent swEvent) {
        if (swEvent.getPlayer().isFrozen()) {
            swEvent.setCancelled(true);
            return;
        }
    }
}
