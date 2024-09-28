package com.walrusone.skywarsreloaded.listeners;

import com.google.common.collect.Lists;
import com.walrusone.skywarsreloaded.SkyWarsReloaded;
import com.walrusone.skywarsreloaded.enums.ChestPlacementType;
import com.walrusone.skywarsreloaded.enums.GameType;
import com.walrusone.skywarsreloaded.enums.MatchState;
import com.walrusone.skywarsreloaded.enums.PlayerRemoveReason;
import com.walrusone.skywarsreloaded.game.Crate;
import com.walrusone.skywarsreloaded.game.GameMap;
import com.walrusone.skywarsreloaded.game.TeamCard;
import com.walrusone.skywarsreloaded.managers.MatchManager;
import com.walrusone.skywarsreloaded.menus.gameoptions.KitSelectionMenu;
import com.walrusone.skywarsreloaded.menus.gameoptions.VotingMenu;
import com.walrusone.skywarsreloaded.menus.gameoptions.objects.CoordLoc;
import com.walrusone.skywarsreloaded.menus.playeroptions.OptionsSelectionMenu;
import com.walrusone.skywarsreloaded.utilities.Messaging;
import com.walrusone.skywarsreloaded.utilities.Party;
import com.walrusone.skywarsreloaded.utilities.SWRServer;
import com.walrusone.skywarsreloaded.utilities.Util;
import me.gaagjescraft.network.team.skywarsreloaded.extension.NoArenaAction;
import me.gaagjescraft.network.team.skywarsreloaded.extension.SWExtension;
import me.gaagjescraft.network.team.skywarsreloaded.extension.menus.SingleJoinMenu;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.block.DoubleChest;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.*;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class PlayerInteractListener implements Listener {


    Object navigationWand = "";
    Object wandItem = "";

    private static final String legacyWEItemsErrorMessage = "An error occurred while detecting player interaction! " +
            "You are using an legacy item ID in the WorldEdit configuration on a non-legacy server (1.13+)! " +
            "Multiple functions of this plugin will fail to work properly until the WorldEdit configuration is re-created or the errors are corrected.";

    public PlayerInteractListener() {

        File f = new File(SkyWarsReloaded.get().getDataFolder().getAbsolutePath().replace("Skywars", "WorldEdit"), "config.yml");
        if (f.exists()) {
            FileConfiguration fc = YamlConfiguration.loadConfiguration(f);
            navigationWand = fc.get("navigation-wand.item");
            wandItem = fc.get("wand-item");
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onInteract(PlayerInteractEvent e) {
        Player player = e.getPlayer();
        if (MatchManager.get().getPlayerMap(player) == null) {
            return;
        }
        if (e.getItem() == null || !Bukkit.getPluginManager().isPluginEnabled("WorldEdit")) {
            return;
        }

        try {
            if (navigationWand instanceof Integer) {
                if (e.getItem().getType().getId() == (int) navigationWand) e.setCancelled(true);
            } else if (e.getItem().getType().name().equalsIgnoreCase((String) navigationWand)) {
                e.setCancelled(true);
            }

            if (wandItem instanceof Integer) {
                if (e.getItem().getType().getId() == (int) wandItem) e.setCancelled(true);
            } else if (e.getItem().getType().name().equalsIgnoreCase((String) wandItem)) {
                e.setCancelled(true);
            }
        } catch (IllegalArgumentException ex) {
            SkyWarsReloaded.get().getLogger().severe(legacyWEItemsErrorMessage);
        }
    }


    @EventHandler
    public void onClick(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        InventoryView v = player.getOpenInventory();
        if (v != null && v.getTopInventory() != null && v.getTopInventory().getType() != InventoryType.CRAFTING) return;

        final GameMap gameMap = MatchManager.get().getPlayerMap(player);
        if (gameMap == null) {
            if (Util.get().isSpawnWorld(player.getWorld())) {
                if (SkyWarsReloaded.getCfg().protectLobby()) {
                    event.setCancelled(true);
                    if (player.hasPermission("sw.alterlobby")) {
                        event.setCancelled(false);
                    }
                }
                if (event.hasItem()) {
                    if (PlayerTeleportListener.isPlayerOnCooldown(player)) {
                        event.setCancelled(true);
                        return;
                    }

                    if (event.getItem().equals(SkyWarsReloaded.getIM().getItem("optionselect"))) {
                        event.setCancelled(true);
                        Util.get().playSound(player, event.getPlayer().getLocation(), SkyWarsReloaded.getCfg().getOpenOptionsMenuSound(), 0.5F, 1);
                        new OptionsSelectionMenu(player);
                    } else if (event.getItem().equals(SkyWarsReloaded.getIM().getItem("joinselect"))) {
                        event.setCancelled(true);
                        if (SkyWarsReloaded.getIC().has("joinmenu")) {
                            Util.get().playSound(player, event.getPlayer().getLocation(), SkyWarsReloaded.getCfg().getOpenJoinMenuSound(), 1, 1);
                            if (Bukkit.getPluginManager().isPluginEnabled("Skywars-Extension")) {
                                if (SWExtension.get().getConfig().getBoolean("override_item_join_actions")) {
                                    NoArenaAction action = NoArenaAction.valueOf(SWExtension.get().getConfig().getString("no_arena_specified_action"));
                                    if (action == NoArenaAction.OPEN_CUSTOM_JOIN_MENU) {
                                        new SingleJoinMenu().openMenu(player, 1);
                                    } else if (action == NoArenaAction.JOIN_RANDOM) {
                                        List<GameMap> maps = Lists.newArrayList();
                                        for (GameMap map : SkyWarsReloaded.getGameMapMgr().getMapsCopy()) {
                                            if ((map.getMatchState() == MatchState.WAITINGSTART || map.getMatchState() == MatchState.WAITINGLOBBY) && map.canAddPlayer(player)) {
                                                maps.add(map);
                                            }
                                        }

                                        if (maps.isEmpty()) {
                                            player.sendMessage(ChatColor.translateAlternateColorCodes('&', SWExtension.get().getConfig().getString("no_solo_arenas")));
                                            return;
                                        } else {
                                            player.sendMessage(ChatColor.translateAlternateColorCodes('&', SWExtension.get().getConfig().getString("solo_join")));
                                            GameMap map;
                                            Random r = new Random();
                                            map = maps.get(r.nextInt(maps.size()));

                                            boolean b = map.addPlayers(null, player);
                                            if (b) {
                                                player.sendMessage(ChatColor.translateAlternateColorCodes('&', SWExtension.get().getConfig().getString("joined_arena").replace("%name%", map.getName())));
                                            } else {
                                                player.sendMessage((new Messaging.MessageFormatter()).format("error.could-not-join2"));
                                            }
                                        }
                                    }
                                }
                            }
                            if (SkyWarsReloaded.getGameMapMgr().getPlayableArenas(GameType.TEAM).size() == 0) {
                                if (!SkyWarsReloaded.getIC().hasViewers("joinsinglemenu")) {
                                    new BukkitRunnable() {
                                        @Override
                                        public void run() {
                                            SkyWarsReloaded.getIC().getMenu("joinsinglemenu").update();
                                        }
                                    }.runTaskLater(SkyWarsReloaded.get(), 5);
                                }
                                SkyWarsReloaded.getIC().show(player, "joinsinglemenu");
                            } else if (SkyWarsReloaded.getGameMapMgr().getPlayableArenas(GameType.SINGLE).size() == 0) {
                                if (!SkyWarsReloaded.getIC().hasViewers("jointeammenu")) {
                                    new BukkitRunnable() {
                                        @Override
                                        public void run() {
                                            SkyWarsReloaded.getIC().getMenu("jointeammenu").update();
                                        }
                                    }.runTaskLater(SkyWarsReloaded.get(), 5);
                                }
                                SkyWarsReloaded.getIC().show(player, "jointeammenu");
                            } else {
                                SkyWarsReloaded.getIC().show(player, "joinmenu");
                            }
                        }
                    } else if (event.getItem().equals(SkyWarsReloaded.getIM().getItem("spectateselect"))) {
                        event.setCancelled(true);
                        Util.get().playSound(player, event.getPlayer().getLocation(), SkyWarsReloaded.getCfg().getOpenSpectateMenuSound(), 1, 1);
                        if (SkyWarsReloaded.getGameMapMgr().getPlayableArenas(GameType.TEAM).size() == 0) {
                            if (!SkyWarsReloaded.getIC().hasViewers("spectatesinglemenu")) {
                                new BukkitRunnable() {
                                    @Override
                                    public void run() {
                                        SkyWarsReloaded.getIC().getMenu("joinsinglemenu").update();
                                    }
                                }.runTaskLater(SkyWarsReloaded.get(), 5);
                            }
                            SkyWarsReloaded.getIC().show(player, "spectatesinglemenu");
                        } else if (SkyWarsReloaded.getGameMapMgr().getPlayableArenas(GameType.SINGLE).size() == 0) {
                            if (!SkyWarsReloaded.getIC().hasViewers("spectateteammenu")) {
                                new BukkitRunnable() {
                                    @Override
                                    public void run() {
                                        SkyWarsReloaded.getIC().getMenu("jointeammenu").update();
                                    }
                                }.runTaskLater(SkyWarsReloaded.get(), 5);
                            }
                            SkyWarsReloaded.getIC().show(player, "spectateteammenu");
                        } else {
                            SkyWarsReloaded.getIC().show(player, "spectatemenu");
                        }
                    }

                    return;
                }

            }

            if (event.getClickedBlock() != null && event.getClickedBlock().getType().toString().toUpperCase().contains("SIGN")) {
                Location loc = event.getClickedBlock().getLocation();
                boolean joined;
                if (!SkyWarsReloaded.getCfg().bungeeMode()) {
                    for (GameMap gMap : SkyWarsReloaded.getGameMapMgr().getMapsCopy()) {
                        if ((gMap.hasSign(loc) || (!event.getClickedBlock().getType().name().contains("WALL") && gMap.hasSign(loc.add(0, -1, 0)))) && (gMap.getMatchState().equals(MatchState.WAITINGSTART) || gMap.getMatchState().equals(MatchState.WAITINGLOBBY))) {
                            if (player.hasPermission("sw.signs") && player.isSneaking()) {
                                return;
                            }
                            Party party = Party.getParty(player);
                            if (party != null) {
                                if (party.getLeader().equals(player.getUniqueId())) {
                                    joined = gMap.addPlayers(null, party);
                                    if (!joined) {
                                        player.sendMessage(new Messaging.MessageFormatter().format("error.could-not-join2"));
                                    }
                                } else {
                                    player.sendMessage(new Messaging.MessageFormatter().format("party.onlyleader"));
                                }
                            } else {
                                joined = gMap.addPlayers(null, player);
                                if (!joined) {
                                    player.sendMessage(new Messaging.MessageFormatter().format("error.could-not-join2"));
                                }
                            }
                        }
                    }
                } else {
                    // todo add party join support
                    SWRServer server = SWRServer.getSign(loc);
                    if (server != null) {
                        if ((server.getMatchState() == MatchState.WAITINGSTART || server.getMatchState().equals(MatchState.WAITINGLOBBY)) && server.getPlayerCount() < server.getMaxPlayers()) {
                            server.setPlayerCount(server.getPlayerCount() + 1);
                            server.updateSigns();
                            SkyWarsReloaded.get().sendBungeeMsg(player, "Connect", server.getServerName());
                        }
                    }
                }
            }
        } else {
            if (gameMap.getMatchState() == MatchState.WAITINGSTART || gameMap.getMatchState().equals(MatchState.WAITINGLOBBY)) {
                if (SkyWarsReloaded.getCfg().debugEnabled())
                    SkyWarsReloaded.get().getLogger().info("PlayerInteractEvent::onClick isWaiting true");
                event.setCancelled(true);
                if (event.getItem() != null) {
                    if (SkyWarsReloaded.getCfg().debugEnabled())
                        SkyWarsReloaded.get().getLogger().info("PlayerInteractEvent::onClick item != null");
                    if (event.getItem().isSimilar(SkyWarsReloaded.getIM().getItem("kitvote"))) {
                        if (SkyWarsReloaded.getCfg().debugEnabled())
                            SkyWarsReloaded.get().getLogger().info("PlayerInteractEvent::onClick kitvote");
                        if (MatchManager.get().getPlayerMap(player).getPlayerCard(player) == null) {
                            String sound = SkyWarsReloaded.getNMS().getVersion() < 9 ? "VILLAGER_NO" : "ENTITY_VILLAGER_NO";
                            Util.get().playSound(player, player.getLocation(), sound, 1, 1);
                            SkyWarsReloaded.getNMS().sendActionBar(player, new Messaging.MessageFormatter().format("game.select-team-before-kit"));
                            return;
                        }

                        if (SkyWarsReloaded.getCfg().kitVotingEnabled()) {
                            SkyWarsReloaded.getIC().show(player, gameMap.getKitVoteOption().getKey());
                        } else {
                            new KitSelectionMenu(player);
                        }
                        Util.get().playSound(player, player.getLocation(), SkyWarsReloaded.getCfg().getOpenKitMenuSound(), 1, 1);
                        return;
                    } else if (event.getItem().isSimilar(SkyWarsReloaded.getIM().getItem("votingItem"))) {
                        if (SkyWarsReloaded.getCfg().debugEnabled())
                            SkyWarsReloaded.get().getLogger().info("PlayerInteractEvent::onClick votingItem");
                        if (player.hasPermission("sw.votemenu")) {
                            if (MatchManager.get().getPlayerMap(player).getPlayerCard(player) == null) {
                                String sound = SkyWarsReloaded.getNMS().getVersion() < 9 ? "VILLAGER_NO" : "ENTITY_VILLAGER_NO";
                                Util.get().playSound(player, player.getLocation(), sound, 1, 1);
                                SkyWarsReloaded.getNMS().sendActionBar(player, new Messaging.MessageFormatter().format("game.select-team-before-voting"));
                                return;
                            }

                            new VotingMenu(player);
                            Util.get().playSound(player, player.getLocation(), SkyWarsReloaded.getCfg().getOpenChestMenuSound(), 1, 1);
                        } else {
                            player.sendMessage(new Messaging.MessageFormatter().format("error.nopermission"));
                        }
                        return;
                    } else if (event.getItem().isSimilar(SkyWarsReloaded.getIM().getItem("teamSelectItem"))) {
                        if (SkyWarsReloaded.getCfg().debugEnabled())
                            SkyWarsReloaded.get().getLogger().info("PlayerInteractEvent::onClick teamSelectItem");
                        SkyWarsReloaded.getIC().show(player, gameMap.getName() + "teamselect");
                        if (SkyWarsReloaded.getIC().has(gameMap.getName() + "teamselect")) {
                            SkyWarsReloaded.getIC().getMenu(gameMap.getName() + "teamselect").update();
                        }
                        // TODO ADD TEAM SELECTION MENU + ADD SOUND
                        return;
                    } else if (event.getItem().isSimilar(SkyWarsReloaded.getIM().getItem("exitGameItem"))) {
                        if (SkyWarsReloaded.getCfg().debugEnabled())
                            SkyWarsReloaded.get().getLogger().info("PlayerInteractEvent::onClick exitGameItem");
                        new BukkitRunnable() {
                            @Override
                            public void run() {
                                SkyWarsReloaded.get().getPlayerManager().removePlayer(player, PlayerRemoveReason.PLAYER_QUIT_GAME, null, true);
                                // MatchManager.get().removeAlivePlayer(player, DamageCause.CUSTOM, true, true);
                            }
                        }.runTaskLater(SkyWarsReloaded.get(), 1);
                    }
                }
                return;
            }
            if (gameMap.getMatchState() == MatchState.PLAYING) {
                if (event.getAction() == Action.RIGHT_CLICK_BLOCK) {
                    Block block = event.getClickedBlock();
                    if (block.getType().equals(Material.ENDER_CHEST)) {
                        for (GameMap gMap : SkyWarsReloaded.getGameMapMgr().getPlayableArenas(GameType.ALL)) {
                            for (Crate crate : gMap.getCrates()) {
                                if (crate.getLocation().equals(block.getLocation())) {
                                    event.setCancelled(true);
                                    if (SkyWarsReloaded.getNMS().getVersion() < 9) {
                                        player.getWorld().playSound(player.getLocation(), Sound.valueOf("CHEST_OPEN"), 1, 1);
                                    } else {
                                        player.getWorld().playSound(player.getLocation(), Sound.valueOf("BLOCK_CHEST_OPEN"), 1, 1);
                                    }
                                    player.openInventory(crate.getInventory());
                                    SkyWarsReloaded.get().getServer().getScheduler().runTaskLater(SkyWarsReloaded.get(), () -> {
                                        SkyWarsReloaded.getNMS().playChestAction(block, true);
                                    }, 1);
                                    return;
                                }
                            }
                        }
                    }
                }
            }
            if (gameMap.getMatchState() == MatchState.ENDING) {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent e) {
        Inventory inv = e.getInventory();
        InventoryView inView = e.getPlayer().getOpenInventory();
        if (inView.getTitle().equals(new Messaging.MessageFormatter().format("event.crateInv"))) {
            for (GameMap gMap : SkyWarsReloaded.getGameMapMgr().getPlayableArenas(GameType.ALL)) {
                for (Crate crate : gMap.getCrates()) {
                    if (crate.getInventory().equals(inv) && inv.getViewers().size() <= 1) {
                        if (SkyWarsReloaded.getNMS().getVersion() < 9) {
                            e.getPlayer().getWorld().playSound(e.getPlayer().getLocation(), Sound.valueOf("CHEST_CLOSE"), 1, 1);
                        } else {
                            e.getPlayer().getWorld().playSound(e.getPlayer().getLocation(), Sound.BLOCK_CHEST_CLOSE, 1, 1);
                        }
                        SkyWarsReloaded.getNMS().playChestAction(crate.getLocation().getBlock(), false);
                        return;
                    }
                }
            }
        } else if (inView.getTitle().contains("chest.yml")) {
            SkyWarsReloaded.getCM().save(inView.getTitle());
        }

    }


    @EventHandler
    public void onInventoryClick(final InventoryClickEvent event) {
        if (event.getWhoClicked() instanceof Player) {
            GameMap gMap = MatchManager.get().getPlayerMap((Player) event.getWhoClicked());
            if (gMap == null) {
                ItemStack item;
                ItemStack item2;
                if (event.getClick().equals(ClickType.NUMBER_KEY)) {
                    item = event.getWhoClicked().getInventory().getItem(event.getHotbarButton());
                    item2 = event.getCurrentItem();
                } else {
                    item = event.getCurrentItem();
                    item2 = event.getCurrentItem();
                }

                if (item != null && (item.equals(SkyWarsReloaded.getIM().getItem("optionselect"))
                        || item.equals(SkyWarsReloaded.getIM().getItem("joinselect"))
                        || item.equals(SkyWarsReloaded.getIM().getItem("spectateselect")))
                        || item2 != null && (item2.equals(SkyWarsReloaded.getIM().getItem("optionselect"))
                        || item2.equals(SkyWarsReloaded.getIM().getItem("joinselect"))
                        || item2.equals(SkyWarsReloaded.getIM().getItem("spectateselect")))) {
                    event.setCancelled(true);
                }
            } else {
                if (gMap.getMatchState().equals(MatchState.WAITINGSTART) || gMap.getMatchState().equals(MatchState.ENDING) || gMap.getMatchState().equals(MatchState.WAITINGLOBBY)) {
                    event.setCancelled(true);
                }
            }
        }

    }

    @EventHandler
    public void onPlayerDropItem(final PlayerDropItemEvent event) {
        final GameMap gameMap = MatchManager.get().getPlayerMap(event.getPlayer());
        if (gameMap == null) {
            return;
        }
        if (gameMap.getMatchState() == MatchState.WAITINGSTART || gameMap.getMatchState() == MatchState.ENDING || gameMap.getMatchState().equals(MatchState.WAITINGLOBBY)) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent e) {
        Player player = e.getPlayer();
        Location blockLoc = e.getBlock().getLocation();

        GameMap playerPlayingMap = MatchManager.get().getPlayerMap(player);
        if (playerPlayingMap == null) {
            if (e.getBlock().getType().equals(Material.CHEST) || e.getBlock().getType().equals(Material.TRAPPED_CHEST) || e.getBlock().getType().equals(Material.DIAMOND_BLOCK) || e.getBlock().getType().equals(Material.EMERALD_BLOCK)) {
                GameMap map = SkyWarsReloaded.getGameMapMgr().getMap(player.getWorld().getName());
                if (map == null) {
                    return;
                }
                if (map.isEditing()) {
                    if (e.getBlock().getType().equals(Material.CHEST) || e.getBlock().getType().equals(Material.TRAPPED_CHEST)) {
                        Chest chest = (Chest) e.getBlock().getState();

                        // Debug
                        CoordLoc coordBlockLoc = new CoordLoc(blockLoc);
                        if (SkyWarsReloaded.getCfg().debugEnabled()) {
                            boolean isCenter = map.getCenterChests().stream().anyMatch(coord -> {
                                boolean found = coord.equals(coordBlockLoc);
                                return found;
                            });
                            boolean isIsland = map.getChests().stream().anyMatch(coord -> {
                                boolean found = coord.equals(coordBlockLoc);
                                return found;
                            });
                        }

                        // Remove from map
                        map.removeChest(chest);
                        InventoryHolder ih = chest.getInventory().getHolder();
                        if (ih instanceof DoubleChest) {
                            DoubleChest dc = (DoubleChest) ih;
                            Chest left = (Chest) dc.getLeftSide();
                            Chest right = (Chest) dc.getRightSide();
                            Location locLeft = left.getLocation();
                            Location locRight = right.getLocation();
                            World world = e.getBlock().getWorld();
                            new BukkitRunnable() {
                                @Override
                                public void run() {
                                    world.getBlockAt(locLeft).setType(Material.AIR);
                                    world.getBlockAt(locRight).setType(Material.AIR);
                                }
                            }.runTaskLater(SkyWarsReloaded.get(), 2L);
                        }
                        player.sendMessage(new Messaging.MessageFormatter().setVariable("mapname", map.getDisplayName()).format("maps.removeChest"));
                    } else if (e.getBlock().getType().equals(Material.DIAMOND_BLOCK)) {
                        // Remove all spawns matching location and collect which ones were removed
                        Map<TeamCard, List<Integer>> result = map.removeSpawnsAtLocation(blockLoc);

                        // Send a message to the player for every spawn removed - this could be one or multiple
                        for (Map.Entry<TeamCard, List<Integer>> removedTeamLocs : result.entrySet()) {
                            int teamCardPos = map.getTeamCardPosition(removedTeamLocs.getKey());
                            if (teamCardPos == -1) {
                                teamCardPos = map.getTeamCards().size();
                            }
                            for (Integer spawnIndex : removedTeamLocs.getValue()) {
                                player.sendMessage(new Messaging.MessageFormatter()
                                        // Convert spawn index to human number
                                        .setVariable("num", "" + (spawnIndex + 1))
                                        // Convert team index to human number
                                        .setVariable("team", "" + (teamCardPos + 1))
                                        .setVariable("mapname", map.getDisplayName())
                                        .format("maps.spawnRemoved"));
                            }
                        }
                        /*CoordLoc loc = new CoordLoc(e.getBlock().getX(), e.getBlock().getY(), e.getBlock().getZ());
                        if (map.getTeamSize() == 1) {
                            List<CoordLoc> locs = map.spawnLocations.getOrDefault(0, Lists.newArrayList());
                            for (int i = 0; i < locs.size(); i++) {
                                CoordLoc l = locs.get(i);
                                if (l.getX() == loc.getX() && l.getY() == loc.getY() && l.getZ() == loc.getZ()) {
                                    locs.remove(i);
                                    map.spawnLocations.put(0, locs);
                                    e.getPlayer().sendMessage(new Messaging.MessageFormatter()
                                            // 1 because human readable
                                            .setVariable("num", "" + 1)
                                            .setVariable("team", "" + i)
                                            .setVariable("mapname", map.getDisplayName())
                                            .format("maps.spawnRemoved"));
                                    break;
                                }
                            }
                        }*/

                    } else if (e.getBlock().getType().equals(Material.EMERALD_BLOCK)) {
                        boolean result = map.removeDeathMatchSpawn(blockLoc);
                        if (result) {
                            player.sendMessage(new Messaging.MessageFormatter().setVariable("num", "" + (map.getDeathMatchSpawns().size() + 1)).setVariable("mapname", map.getDisplayName()).format("maps.deathSpawnRemoved"));
                        }
                    }
                }
            }
            return;
        }
        if (playerPlayingMap.getMatchState().equals(MatchState.WAITINGSTART) || playerPlayingMap.getMatchState().equals(MatchState.WAITINGLOBBY)) {
            e.setCancelled(true);
            new BukkitRunnable() {
                @Override
                public void run() {
                    CoordLoc spawn = playerPlayingMap.getPlayerCard(player).getSpawn();
                    player.teleport(new Location(playerPlayingMap.getCurrentWorld(), spawn.getX() + 0.5, spawn.getY() + 1, spawn.getZ() + 0.5));
                }
            }.runTaskLater(SkyWarsReloaded.get(), 2);
        }
        if (playerPlayingMap.getMatchState().equals(MatchState.PLAYING)) {
            Block block = e.getBlock();
            if (block.getType().equals(Material.ENDER_CHEST)) {
                for (Crate crate : playerPlayingMap.getCrates()) {
                    if (crate.getLocation().equals(block.getLocation())) {
                        e.setCancelled(true);
                        return;
                    }
                }
            }
        }
    }

    @EventHandler
    public void onBlockPlaced(BlockPlaceEvent e) {
        GameMap gMap = MatchManager.get().getPlayerMap(e.getPlayer());
        if (gMap == null) {
            if (e.getBlockPlaced().getState() instanceof Chest) {
                GameMap map = SkyWarsReloaded.getGameMapMgr().getMap(e.getPlayer().getWorld().getName());
                if (map == null) {
                    return;
                }
                if (map.isEditing()) {
                    Location loc = e.getBlock().getLocation();
                    Player player = e.getPlayer();
                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            map.addChest((Chest) loc.getBlock().getState(), map.getChestPlacementType());
                            if (map.getChestPlacementType() == ChestPlacementType.NORMAL) {
                                player.sendMessage(new Messaging.MessageFormatter().setVariable("mapname", map.getDisplayName()).format("maps.addChest"));
                            } else if (map.getChestPlacementType() == ChestPlacementType.CENTER) {
                                player.sendMessage(new Messaging.MessageFormatter().setVariable("mapname", map.getDisplayName()).format("maps.addCenterChest"));
                            }
                        }
                    }.runTaskLater(SkyWarsReloaded.get(), 2L);
                }
            }
        }
    }

    @EventHandler
    public void onPlayerWalk(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        for (GameMap gMap : SkyWarsReloaded.getGameMapMgr().getPlayableArenas(GameType.ALL)) {
            if (gMap.getDeathMatchWaiters().contains(player.getUniqueId().toString())) {
                if (event.getFrom().getBlockX() != event.getTo().getBlockX() || event.getFrom().getBlockZ() != event.getTo().getBlockZ()) {
                    event.setCancelled(true);
                }
            }
        }
    }

    // Util

    private void replaceAllEmptyArmorByItem(PlayerInventory playerInv, ItemStack[] armor, ItemStack replacement) {
        for (int armorSlot = 0; armorSlot < armor.length; armorSlot++) {
            ItemStack armorItem = armor[armorSlot];
            if (armorItem == null || armorItem.getType().equals(Material.AIR)) armor[armorSlot] = replacement;
        }
        playerInv.setArmorContents(armor);
    }

}