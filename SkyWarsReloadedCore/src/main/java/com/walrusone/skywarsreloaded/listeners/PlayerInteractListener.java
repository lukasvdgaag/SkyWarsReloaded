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
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.File;
import java.util.List;
import java.util.Random;

public class PlayerInteractListener implements Listener {

    Object navigationWand = "";
    Object wandItem = "";

    public PlayerInteractListener() {
        File f = new File(SkyWarsReloaded.get().getDataFolder().getAbsolutePath().replace("Skywars", "WorldEdit"), "config.yml");
        if (f.exists()) {
            FileConfiguration fc = YamlConfiguration.loadConfiguration(f);
            navigationWand = fc.get("navigation-wand.item");
            wandItem = fc.get("wand-item");
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onInteract(PlayerInteractEvent e) {
        Player player = e.getPlayer();
        if (MatchManager.get().getPlayerMap(player) != null) {

            if (Bukkit.getPluginManager().isPluginEnabled("WorldEdit")) {

                if (e.getItem() == null) {
                    return;
                }

                if (navigationWand instanceof Integer) {
                    if (e.getItem().getType().getId() == (int) navigationWand) {
                        e.setCancelled(true);
                    }
                } else {
                    if (e.getItem().getType().name().equalsIgnoreCase((String) navigationWand)) {
                        e.setCancelled(true);
                    }
                }

                if (wandItem instanceof Integer) {
                    if (e.getItem().getType().getId() == (int) wandItem) {
                        e.setCancelled(true);
                    }
                } else {
                    if (e.getItem().getType().name().equalsIgnoreCase((String) wandItem)) {
                        e.setCancelled(true);
                    }
                }
            }
        }
    }


    @EventHandler
    public void onClick(PlayerInteractEvent a1) {
        InventoryView v = a1.getPlayer().getOpenInventory();
        if (v != null && v.getTopInventory() != null && v.getTopInventory().getType() != InventoryType.CRAFTING) return;

        final GameMap gameMap = MatchManager.get().getPlayerMap(a1.getPlayer());
        if (gameMap == null) {
            if (Util.get().isSpawnWorld(a1.getPlayer().getWorld())) {
                if (SkyWarsReloaded.getCfg().protectLobby()) {
                    a1.setCancelled(true);
                    if (a1.getPlayer().hasPermission("sw.alterlobby")) {
                        a1.setCancelled(false);
                    }
                }
                if (a1.hasItem()) {
                    if (PlayerTeleportListener.cooldowns.contains(a1.getPlayer())) {
                        a1.setCancelled(true);
                        return;
                    }

                    if (a1.getItem().equals(SkyWarsReloaded.getIM().getItem("optionselect"))) {
                        a1.setCancelled(true);
                        Util.get().playSound(a1.getPlayer(), a1.getPlayer().getLocation(), SkyWarsReloaded.getCfg().getOpenOptionsMenuSound(), 0.5F, 1);
                        new OptionsSelectionMenu(a1.getPlayer());
                    } else if (a1.getItem().equals(SkyWarsReloaded.getIM().getItem("joinselect"))) {
                        a1.setCancelled(true);
                        if (SkyWarsReloaded.getIC().has("joinmenu")) {
                            Util.get().playSound(a1.getPlayer(), a1.getPlayer().getLocation(), SkyWarsReloaded.getCfg().getOpenJoinMenuSound(), 1, 1);
                            if (Bukkit.getPluginManager().isPluginEnabled("Skywars-Extension")) {
                                if (SWExtension.get().getConfig().getBoolean("override_item_join_actions")) {
                                    NoArenaAction action = NoArenaAction.valueOf(SWExtension.get().getConfig().getString("no_arena_specified_action"));
                                    if (action == NoArenaAction.OPEN_CUSTOM_JOIN_MENU) {
                                        new SingleJoinMenu().openMenu(a1.getPlayer(), 1);
                                        return;
                                    } else if (action == NoArenaAction.JOIN_RANDOM) {
                                        List<GameMap> maps = Lists.newArrayList();
                                        for (GameMap map : GameMap.getMaps()) {
                                            if ((map.getMatchState() == MatchState.WAITINGSTART || map.getMatchState() == MatchState.WAITINGLOBBY) && map.canAddPlayer()) {
                                                maps.add(map);
                                            }
                                        }

                                        if (maps.isEmpty()) {
                                            a1.getPlayer().sendMessage(ChatColor.translateAlternateColorCodes('&', SWExtension.get().getConfig().getString("no_solo_arenas")));
                                            return;
                                        }
                                        a1.getPlayer().sendMessage(ChatColor.translateAlternateColorCodes('&', SWExtension.get().getConfig().getString("solo_join")));
                                        GameMap map;
                                        Random r = new Random();
                                        map = maps.get(r.nextInt(maps.size()));

                                        boolean b = map.addPlayers((TeamCard) null, a1.getPlayer());
                                        if (b) {
                                            a1.getPlayer().sendMessage(ChatColor.translateAlternateColorCodes('&', SWExtension.get().getConfig().getString("joined_arena").replace("%name%", map.getName())));
                                        } else {
                                            a1.getPlayer().sendMessage((new Messaging.MessageFormatter()).format("error.could-not-join2"));
                                        }
                                        return;
                                    }
                                }
                            }
                            if (GameMap.getPlayableArenas(GameType.TEAM).size() == 0) {
                                if (!SkyWarsReloaded.getIC().hasViewers("joinsinglemenu")) {
                                    new BukkitRunnable() {
                                        @Override
                                        public void run() {
                                            SkyWarsReloaded.getIC().getMenu("joinsinglemenu").update();
                                        }
                                    }.runTaskLater(SkyWarsReloaded.get(), 5);
                                }
                                SkyWarsReloaded.getIC().show(a1.getPlayer(), "joinsinglemenu");
                                return;
                            } else if (GameMap.getPlayableArenas(GameType.SINGLE).size() == 0) {
                                if (!SkyWarsReloaded.getIC().hasViewers("jointeammenu")) {
                                    new BukkitRunnable() {
                                        @Override
                                        public void run() {
                                            SkyWarsReloaded.getIC().getMenu("jointeammenu").update();
                                        }
                                    }.runTaskLater(SkyWarsReloaded.get(), 5);
                                }
                                SkyWarsReloaded.getIC().show(a1.getPlayer(), "jointeammenu");
                                return;
                            } else {
                                SkyWarsReloaded.getIC().show(a1.getPlayer(), "joinmenu");
                                return;
                            }
                        }
                    } else if (a1.getItem().equals(SkyWarsReloaded.getIM().getItem("spectateselect"))) {
                        a1.setCancelled(true);
                        Util.get().playSound(a1.getPlayer(), a1.getPlayer().getLocation(), SkyWarsReloaded.getCfg().getOpenSpectateMenuSound(), 1, 1);
                        if (GameMap.getPlayableArenas(GameType.TEAM).size() == 0) {
                            if (!SkyWarsReloaded.getIC().hasViewers("spectatesinglemenu")) {
                                new BukkitRunnable() {
                                    @Override
                                    public void run() {
                                        SkyWarsReloaded.getIC().getMenu("joinsinglemenu").update();
                                    }
                                }.runTaskLater(SkyWarsReloaded.get(), 5);
                            }
                            SkyWarsReloaded.getIC().show(a1.getPlayer(), "spectatesinglemenu");
                            return;
                        } else if (GameMap.getPlayableArenas(GameType.SINGLE).size() == 0) {
                            if (!SkyWarsReloaded.getIC().hasViewers("spectateteammenu")) {
                                new BukkitRunnable() {
                                    @Override
                                    public void run() {
                                        SkyWarsReloaded.getIC().getMenu("jointeammenu").update();
                                    }
                                }.runTaskLater(SkyWarsReloaded.get(), 5);
                            }
                            SkyWarsReloaded.getIC().show(a1.getPlayer(), "spectateteammenu");
                            return;
                        } else {
                            SkyWarsReloaded.getIC().show(a1.getPlayer(), "spectatemenu");
                            return;
                        }
                    }
                }

            }

            Player player = a1.getPlayer();
            if (a1.getClickedBlock() != null && a1.getClickedBlock().getType().toString().toUpperCase().contains("SIGN")) {
                Location loc = a1.getClickedBlock().getLocation();
                boolean joined;
                if (!SkyWarsReloaded.getCfg().bungeeMode()) {
                    for (GameMap gMap : GameMap.getMaps()) {
                        if ((gMap.hasSign(loc) || (!a1.getClickedBlock().getType().name().contains("WALL") && gMap.hasSign(loc.add(0, -1, 0)))) && (gMap.getMatchState().equals(MatchState.WAITINGSTART) || gMap.getMatchState().equals(MatchState.WAITINGLOBBY))) {
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
                a1.setCancelled(true);
                if (a1.getItem() != null) {
                    Player player = a1.getPlayer();
                    if (a1.getItem().isSimilar(SkyWarsReloaded.getIM().getItem("kitvote"))) {
                        if (MatchManager.get().getPlayerMap(a1.getPlayer()).getPlayerCard(a1.getPlayer()) == null) {
                            String sound = SkyWarsReloaded.getNMS().getVersion() < 9 ? "VILLAGER_NO" : "ENTITY_VILLAGER_NO";
                            Util.get().playSound(player, player.getLocation(), sound, 1, 1);
                            SkyWarsReloaded.getNMS().sendActionBar(player, new Messaging.MessageFormatter().format("game.select-team-before-kit"));
                            return;
                        }

                        if (SkyWarsReloaded.getCfg().kitVotingEnabled()) {
                            SkyWarsReloaded.getIC().show(player, gameMap.getKitVoteOption().getKey());
                        } else {
                            new KitSelectionMenu(a1.getPlayer());
                        }
                        Util.get().playSound(player, player.getLocation(), SkyWarsReloaded.getCfg().getOpenKitMenuSound(), 1, 1);
                        return;
                    } else if (a1.getItem().isSimilar(SkyWarsReloaded.getIM().getItem("votingItem"))) {
                        if (a1.getPlayer().hasPermission("sw.votemenu")) {
                            if (MatchManager.get().getPlayerMap(a1.getPlayer()).getPlayerCard(a1.getPlayer()) == null) {
                                String sound = SkyWarsReloaded.getNMS().getVersion() < 9 ? "VILLAGER_NO" : "ENTITY_VILLAGER_NO";
                                Util.get().playSound(player, player.getLocation(), sound, 1, 1);
                                SkyWarsReloaded.getNMS().sendActionBar(player, new Messaging.MessageFormatter().format("game.select-team-before-voting"));
                                return;
                            }

                            new VotingMenu(a1.getPlayer());
                            Util.get().playSound(player, player.getLocation(), SkyWarsReloaded.getCfg().getOpenChestMenuSound(), 1, 1);
                        } else {
                            player.sendMessage(new Messaging.MessageFormatter().format("error.nopermission"));
                        }
                        return;
                    } else if (a1.getItem().isSimilar(SkyWarsReloaded.getIM().getItem("teamSelectItem"))) {
                        SkyWarsReloaded.getIC().show(player, gameMap.getName() + "teamselect");
                        if (SkyWarsReloaded.getIC().has(gameMap.getName() + "teamselect")) {
                            SkyWarsReloaded.getIC().getMenu(gameMap.getName() + "teamselect").update();
                        }
                        // TODO ADD TEAM SELECTION MENU + ADD SOUND
                        return;
                    } else if (a1.getItem().isSimilar(SkyWarsReloaded.getIM().getItem("exitGameItem"))) {
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
                if (a1.getAction() == Action.RIGHT_CLICK_BLOCK) {
                    Block block = a1.getClickedBlock();
                    if (block.getType().equals(Material.ENDER_CHEST)) {
                        for (GameMap gMap : GameMap.getPlayableArenas(GameType.ALL)) {
                            for (Crate crate : gMap.getCrates()) {
                                if (crate.getLocation().equals(block.getLocation())) {
                                    a1.setCancelled(true);
                                    if (SkyWarsReloaded.getNMS().getVersion() < 9) {
                                        a1.getPlayer().getWorld().playSound(a1.getPlayer().getLocation(), Sound.valueOf("CHEST_OPEN"), 1, 1);
                                    } else {
                                        a1.getPlayer().getWorld().playSound(a1.getPlayer().getLocation(), Sound.valueOf("BLOCK_CHEST_OPEN"), 1, 1);
                                    }
                                    a1.getPlayer().openInventory(crate.getInventory());
                                    SkyWarsReloaded.getNMS().playEnderChestAction(block, true);
                                    return;
                                }
                            }
                        }
                    }
                }
            }
            if (gameMap.getMatchState() == MatchState.ENDING) {
                a1.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent e) {
        Inventory inv = e.getInventory();
        InventoryView inView = e.getPlayer().getOpenInventory();
        if (inView.getTitle().equals(new Messaging.MessageFormatter().format("event.crateInv"))) {
            for (GameMap gMap : GameMap.getPlayableArenas(GameType.ALL)) {
                for (Crate crate : gMap.getCrates()) {
                    if (crate.getInventory().equals(inv) && inv.getViewers().size() <= 1) {
                        if (SkyWarsReloaded.getNMS().getVersion() < 9) {
                            e.getPlayer().getWorld().playSound(e.getPlayer().getLocation(), Sound.valueOf("CHEST_CLOSE"), 1, 1);
                        } else {
                            e.getPlayer().getWorld().playSound(e.getPlayer().getLocation(), Sound.BLOCK_CHEST_CLOSE, 1, 1);
                        }
                        SkyWarsReloaded.getNMS().playEnderChestAction(e.getPlayer().getWorld().getBlockAt(crate.getLocation()), false);
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
        GameMap playerPlayingMap = MatchManager.get().getPlayerMap(e.getPlayer());
        if (playerPlayingMap == null) {
            if (e.getBlock().getType().equals(Material.CHEST) || e.getBlock().getType().equals(Material.TRAPPED_CHEST) || e.getBlock().getType().equals(Material.DIAMOND_BLOCK) || e.getBlock().getType().equals(Material.EMERALD_BLOCK)) {
                GameMap map = GameMap.getMap(e.getPlayer().getWorld().getName());
                if (map == null) {
                    return;
                }
                if (map.isEditing()) {
                    if (e.getBlock().getType().equals(Material.CHEST) || e.getBlock().getType().equals(Material.TRAPPED_CHEST)) {
                        Chest chest = (Chest) e.getBlock().getState();
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
                        e.getPlayer().sendMessage(new Messaging.MessageFormatter().setVariable("mapname", map.getDisplayName()).format("maps.removeChest"));
                    } else if (e.getBlock().getType().equals(Material.DIAMOND_BLOCK)) {
                        int[] result = map.removeTeamCard(e.getBlock().getLocation());
                        if (result != null) {
                            e.getPlayer().sendMessage(new Messaging.MessageFormatter()
                                    // Convert spawn index to human number
                                    .setVariable("num", "" + result[0] + 1)
                                    // Convert team index to human number
                                    .setVariable("team", "" + result[1] + 1)
                                    .setVariable("mapname", map.getDisplayName())
                                    .format("maps.spawnRemoved"));
                            return;
                        }

                        CoordLoc loc = new CoordLoc(e.getBlock().getX(), e.getBlock().getY(), e.getBlock().getZ());
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
                        }

                    } else if (e.getBlock().getType().equals(Material.EMERALD_BLOCK)) {
                        boolean result = map.removeDeathMatchSpawn(e.getBlock().getLocation());
                        if (result) {
                            e.getPlayer().sendMessage(new Messaging.MessageFormatter().setVariable("num", "" + (map.getDeathMatchSpawns().size() + 1)).setVariable("mapname", map.getDisplayName()).format("maps.deathSpawnRemoved"));
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
                    CoordLoc spawn = playerPlayingMap.getPlayerCard(e.getPlayer()).getSpawn();
                    e.getPlayer().teleport(new Location(playerPlayingMap.getCurrentWorld(), spawn.getX() + 0.5, spawn.getY() + 1, spawn.getZ() + 0.5));
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
                GameMap map = GameMap.getMap(e.getPlayer().getWorld().getName());
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
        for (GameMap gMap : GameMap.getPlayableArenas(GameType.ALL)) {
            if (gMap.getDeathMatchWaiters().contains(player.getUniqueId().toString())) {
                if (event.getFrom().getBlockX() != event.getTo().getBlockX() || event.getFrom().getBlockZ() != event.getTo().getBlockZ()) {
                    event.setCancelled(true);
                }
            }
        }
    }


}