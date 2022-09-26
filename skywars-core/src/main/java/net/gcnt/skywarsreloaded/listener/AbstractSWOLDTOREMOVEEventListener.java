package net.gcnt.skywarsreloaded.listener;

import net.gcnt.skywarsreloaded.SkyWarsReloaded;
import net.gcnt.skywarsreloaded.enums.DeathReason;
import net.gcnt.skywarsreloaded.event.SWMessageReceivedEvent;
import net.gcnt.skywarsreloaded.event.SWMessageSentEvent;
import net.gcnt.skywarsreloaded.game.GamePlayer;
import net.gcnt.skywarsreloaded.game.GameTeam;
import net.gcnt.skywarsreloaded.game.GameTemplate;
import net.gcnt.skywarsreloaded.game.gameinstance.GameInstance;
import net.gcnt.skywarsreloaded.game.gameinstance.LocalGameInstance;
import net.gcnt.skywarsreloaded.game.types.ChestType;
import net.gcnt.skywarsreloaded.game.types.GameState;
import net.gcnt.skywarsreloaded.unlockable.killmessages.KillMessageGroup;
import net.gcnt.skywarsreloaded.utils.results.SpawnRemoveResult;
import net.gcnt.skywarsreloaded.wrapper.entity.SWEntity;
import net.gcnt.skywarsreloaded.wrapper.entity.SWOwnedEntity;
import net.gcnt.skywarsreloaded.wrapper.entity.SWPlayer;
import net.gcnt.skywarsreloaded.wrapper.event.*;
import net.gcnt.skywarsreloaded.wrapper.world.SWWorld;

public class AbstractSWOLDTOREMOVEEventListener {

    public final SkyWarsReloaded plugin;

    public AbstractSWOLDTOREMOVEEventListener(SkyWarsReloaded pluginIn) {
        this.plugin = pluginIn;
    }

    
    public void onAsyncPlayerPreLogin(SWAsyncPlayerPreLoginEvent event) {
        SWPlayer player = this.plugin.getPlayerManager().initPlayer(event.getUUID());
        this.plugin.getPlayerStorage().loadData(player);
    }

    
    public void onPlayerJoin(SWPlayerJoinEvent event) {
        event.getPlayer().fetchParentPlayer();
    }

    
    public void onAsyncPlayerChat(SWAsyncPlayerChatEvent event) {

    }

    
    public void onPlayerQuit(SWPlayerQuitEvent event) {
        this.plugin.getPlayerManager().removePlayer(event.getPlayer());
    }

    
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
        GameInstance gameWorld = player.getGameWorld();
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

    
    public void onPlayerBlockBreak(SWBlockBreakEvent event) {
        if (cancelWhenWaitingInGame(event)) return;

        GameInstance gameWorld = plugin.getGameInstanceManager().getGameInstanceByName(event.getCoord().getWorld().getName());
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

    
    public void onPlayerBlockPlace(SWBlockPlaceEvent event) {
        if (cancelWhenWaitingInGame(event) || plugin.getGameInstanceManager().isManagerRemote()) return;

        // player is placing a chest
        if (event.getBlockTypeName().equalsIgnoreCase("CHEST") ||
                event.getBlockTypeName().equalsIgnoreCase("TRAPPED_CHEST")) {
            LocalGameInstance gameWorld = (LocalGameInstance) plugin.getGameInstanceManager().getGameInstanceByName(event.getCoord().getWorld().getName());
            if (gameWorld == null || !gameWorld.isEditing()) return;

            final GameTemplate template = gameWorld.getTemplate();
            final ChestType chestType = gameWorld.getSelectedEditingChestTypes().getOrDefault(event.getPlayer().getUuid(), ChestType.ISLAND);
            boolean res = template.addChest(event.getCoord().asBlock(), chestType);
            if (res) {
                event.getPlayer().sendTitle(plugin.getUtils().colorize("&a&lCHEST ADDED"),
                        plugin.getUtils().colorize("&7Added a new chest to the template!"), 5, 30, 5);
                event.getPlayer().sendMessage(plugin.getUtils().colorize(
                        String.format("&aAdded a new &e%s &achest (&b#%d&a) to game template &b%s&a.",
                                chestType.getId().toLowerCase(),
                                template.getChests().size(), template.getDisplayName())));
                template.checkToDoList(event.getPlayer());
            }
        }

    }

    
    public void onPlayerDeath(SWPlayerDeathEvent event) {
        SWPlayer player = event.getPlayer();
        final GameInstance gameWorld = player.getGameWorld();

        if (gameWorld == null) return;

        player.setHealth(20);
        event.setDeathMessage(null);
        event.setKeepInventory(false);
    }

    
    public void onEntityDamage(SWEntityDamageEvent event) {
        if (!(event.getEntity() instanceof SWPlayer) || plugin.getGameInstanceManager().isManagerRemote()) return;

        SWPlayer player = (SWPlayer) event.getEntity();
        if (cancelWhenWaitingInGame(player, event)) return;
        LocalGameInstance gameWorld = (LocalGameInstance) player.getGameWorld();
        if (gameWorld == null) return;

        GamePlayer gamePlayer = gameWorld.getPlayer(player);
        if (gamePlayer.isSpectating() || !gamePlayer.isAlive()) {
            event.setCancelled(true);
            System.out.println(gamePlayer.getSWPlayer().getName() + " is spectating");
            return;
        }

        if (gameWorld.getState() != GameState.PLAYING) return;

        if (player.getHealth() - event.getFinalDamage() > 0) return;

        final GameTeam team = gamePlayer.getTeam();
        if (team == null) return;

        event.setDamage(0);

        DeathReason reason = event.getCause();
        if (reason == null) reason = DeathReason.DEFAULT;

        SWPlayer tagged = gamePlayer.getLastTaggedBy();

        // sending the death message.
        // todo check for assists.
        String message;
        if (tagged != null) {
            if (!reason.isKill()) {
                reason = DeathReason.fromString(reason.name() + "_KILL");
                if (reason == null) reason = DeathReason.DEFAULT_KILL;
            }

            final GamePlayer taggedGamePlayer = gameWorld.getPlayer(tagged);
            if (taggedGamePlayer != null) taggedGamePlayer.addKill();

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
        player.sendTitle("§c§lYOU DIED!", "§7You are now a spectator!", 5, 30, 5);
        // todo customize this message.

        team.eliminatePlayer(gamePlayer);
        gameWorld.preparePlayer(player);
    }

    
    public void onEntityDamageByEntity(SWEntityDamageByEntityEvent event) {
        if (!(event.getEntity() instanceof SWPlayer) || plugin.getGameInstanceManager().isManagerRemote()) return;

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
            final GamePlayer gamePlayer = ((LocalGameInstance) player.getGameWorld()).getPlayer(player);
            gamePlayer.setLastTaggedBy(tagger);
        }
    }

    
    public void onPlayerFoodLevelChange(SWPlayerFoodLevelChangeEvent event) {
        cancelWhenWaitingInGame(event);
        event.setFoodLevel(20);
    }

    
    public void onChunkLoad(SWChunkLoadEvent swEvent) {

    }

    
    public void onWorldInit(SWWorldInitEvent swEvent) {
        SWWorld world = swEvent.getWorld();
        if (this.plugin.getGameInstanceManager().getGameTemplateByName(world.getName()) != null) {
            world.setKeepSpawnLoaded(false);
        }
    }

    
    public void onPlayerMove(SWPlayerMoveEvent swEvent) {
        if (swEvent.getPlayer().isFrozen()) {
            swEvent.setCancelled(true);
            return;
        }
    }

    
    public void onSWMessageReceived(SWMessageReceivedEvent event) {

    }

    
    public void onSWMessageSent(SWMessageSentEvent event) {

    }
}
