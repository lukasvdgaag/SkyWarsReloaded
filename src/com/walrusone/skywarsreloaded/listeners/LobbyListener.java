package com.walrusone.skywarsreloaded.listeners;

import com.walrusone.skywarsreloaded.SkyWarsReloaded;
import com.walrusone.skywarsreloaded.enums.GameType;
import com.walrusone.skywarsreloaded.enums.LeaderType;
import com.walrusone.skywarsreloaded.game.GameMap;
import com.walrusone.skywarsreloaded.game.SWRSign;
import com.walrusone.skywarsreloaded.managers.MatchManager;
import com.walrusone.skywarsreloaded.utilities.Messaging;
import com.walrusone.skywarsreloaded.utilities.Party;
import com.walrusone.skywarsreloaded.utilities.Util;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;

import java.util.UUID;

public class LobbyListener implements org.bukkit.event.Listener {
    public LobbyListener() {
    }

    @EventHandler(priority = org.bukkit.event.EventPriority.HIGH)
    public void onEntityDamage(EntityDamageByEntityEvent e) {
        if ((SkyWarsReloaded.getCfg().protectLobby()) && (Util.get().isSpawnWorld(e.getEntity().getWorld()))) {
            e.setCancelled(true);
            if ((((e.getEntity() instanceof Player)) || ((e.getDamager() instanceof Player))) &&
                    (e.getDamager().hasPermission("sw.alterlobby"))) {
                e.setCancelled(false);
            }
        }
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent e) {
        Player player = (Player) e.getWhoClicked();
        if ((SkyWarsReloaded.getCfg().protectLobby()) && (Util.get().isSpawnWorld(player.getWorld())) &&
                (!player.hasPermission("sw.alterlobby")) && (!SkyWarsReloaded.getIC().has(player))) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onPlayerDropItem(PlayerDropItemEvent e) {
        if ((SkyWarsReloaded.getCfg().protectLobby()) && (Util.get().isSpawnWorld(e.getPlayer().getWorld())) &&
                (!e.getPlayer().hasPermission("sw.alterlobby"))) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void signPlaced(SignChangeEvent event) {
        if (Util.get().isSpawnWorld(event.getBlock().getWorld())) {
            String[] lines = event.getLines();
            if ((lines[0].equalsIgnoreCase("[sw]")) && (lines.length >= 2)) {
                if (event.getPlayer().hasPermission("sw.signs")) {
                    Location signLocation = event.getBlock().getLocation();
                    World w = signLocation.getWorld();
                    Block b = w.getBlockAt(signLocation);
                    if (b.getState() instanceof Sign) {
                        event.setCancelled(true);
                        String arenaName = lines[1];
                        GameMap gMap = GameMap.getMap(arenaName);
                        if (gMap != null) {
                            gMap.addSign(signLocation);
                            event.getPlayer().sendMessage(new Messaging.MessageFormatter().format("signs.added"));
                        } else {
                            event.getPlayer().sendMessage(new Messaging.MessageFormatter().format("signs.no-map"));
                        }
                    }
                } else {
                    event.getPlayer().sendMessage(new Messaging.MessageFormatter().format("error.signs-no-perm"));
                    event.setCancelled(true);
                }
            } else if ((lines[0].equalsIgnoreCase("[swl]")) && (lines.length >= 3)) {
                if (event.getPlayer().hasPermission("sw.signs")) {
                    Location signLocation = event.getBlock().getLocation();
                    World w = signLocation.getWorld();
                    Block b = w.getBlockAt(signLocation);
                    if (b.getState() instanceof Sign) {
                        event.setCancelled(true);
                        if (SkyWarsReloaded.get().getUseable().contains(lines[1].toUpperCase())) {
                            LeaderType type = LeaderType.valueOf(lines[1].toUpperCase());
                            if (Util.get().isInteger(lines[2])) {
                                if (Integer.valueOf(lines[2]) <= SkyWarsReloaded.getCfg().getLeaderSize()) {
                                    SkyWarsReloaded.getLB().addLeaderSign(Integer.valueOf(lines[2]), type, signLocation);
                                    event.getPlayer().sendMessage(new Messaging.MessageFormatter().format("signs.addedleader"));
                                } else {
                                    event.getPlayer().sendMessage(new Messaging.MessageFormatter().format("signs.invalid-range"));
                                }
                            } else {
                                event.getPlayer().sendMessage(new Messaging.MessageFormatter().format("error.position"));
                            }
                        } else {
                            event.getPlayer().sendMessage(new Messaging.MessageFormatter().format("signs.invalid-type"));
                        }
                    }
                } else {
                    event.getPlayer().sendMessage(new Messaging.MessageFormatter().format("error.signs-no-perm"));
                    event.setCancelled(true);
                }
            }
        }
    }

    @EventHandler
    public void signRemoved(BlockBreakEvent event) {
        if (Util.get().isSpawnWorld(event.getBlock().getWorld())) {
            Location blockLocation = event.getBlock().getLocation();
            World w = blockLocation.getWorld();
            Block b = w.getBlockAt(blockLocation);
            if ((b.getState() instanceof Sign)) {
                Sign sign = (Sign) b.getState();
                Location loc = sign.getLocation();
                boolean removed = false;

                for (GameMap map : GameMap.getMaps()) {
                    if (map.hasSign(loc)) {
                        if (event.getPlayer().isSneaking()) {
                            if (!removed) {
                                if (event.getPlayer().hasPermission("sw.signs")) {
                                    removed = map.removeSign(loc);
                                }
                                else {
                                    event.setCancelled(true);
                                }
                            }
                        }
                        else {
                            event.setCancelled(true);
                        }
                    }
                }

                if (!removed) {
                    if (event.getPlayer().hasPermission("sw.signs")) {
                        removed = SkyWarsReloaded.getLB().removeLeaderSign(loc);
                    }
                }
                if (removed) {
                    event.getPlayer().sendMessage(new Messaging.MessageFormatter().format("signs.remove"));
                }
            }
        }
    }

    @EventHandler
    public void onPressurePlate(PlayerInteractEvent e) {
        if (Util.get().isSpawnWorld(e.getPlayer().getWorld())) {
            Player player = e.getPlayer();
            GameMap gMap = MatchManager.get().getPlayerMap(player);
            if ((gMap == null) &&
                    (e.getAction() == org.bukkit.event.block.Action.PHYSICAL) && ((e.getClickedBlock().getType() == SkyWarsReloaded.getNMS().getMaterial("STONE_PLATE").getType()) ||
                    (

                            e.getClickedBlock().getType() == SkyWarsReloaded.getNMS().getMaterial("IRON_PLATE").getType()) || (e.getClickedBlock().getType() == SkyWarsReloaded.getNMS().getMaterial("GOLD_PLATE").getType())) &&
                    (SkyWarsReloaded.getCfg().pressurePlateJoin())) {
                Location spawn = SkyWarsReloaded.getCfg().getSpawn();
                if (spawn != null) {
                    boolean joined = false;
                    int count = 0;
                    Party party = Party.getParty(player);
                    while ((count < 4) && (!joined)) {
                        if (party != null) {
                            if (party.getLeader().equals(player.getUniqueId())) {
                                boolean tryJoin = true;
                                for (UUID uuid : party.getMembers()) {
                                    if (Util.get().isBusy(uuid)) {
                                        tryJoin = false;
                                        party.sendPartyMessage(new Messaging.MessageFormatter().setVariable("player", org.bukkit.Bukkit.getPlayer(uuid).getName()).format("party.memberbusy"));
                                    }
                                }
                                if (!tryJoin) break;
                                if (e.getClickedBlock().getType() == SkyWarsReloaded.getNMS().getMaterial("STONE_PLATE").getType()) {
                                    joined = MatchManager.get().joinGame(party, GameType.ALL);
                                } else if (e.getClickedBlock().getType() == SkyWarsReloaded.getNMS().getMaterial("IRON_PLATE").getType()) {
                                    joined = MatchManager.get().joinGame(party, GameType.SINGLE);
                                } else {
                                    joined = MatchManager.get().joinGame(party, GameType.TEAM);
                                }

                            } else {
                                player.sendMessage(new Messaging.MessageFormatter().format("party.onlyleader"));
                                joined = true;
                                break;
                            }
                        } else if (e.getClickedBlock().getType() == SkyWarsReloaded.getNMS().getMaterial("STONE_PLATE").getType()) {
                            joined = MatchManager.get().joinGame(player, GameType.ALL);
                        } else if (e.getClickedBlock().getType() == SkyWarsReloaded.getNMS().getMaterial("IRON_PLATE").getType()) {
                            joined = MatchManager.get().joinGame(player, GameType.SINGLE);
                        } else {
                            joined = MatchManager.get().joinGame(player, GameType.TEAM);
                        }

                        count++;
                    }
                    if (!joined) {
                        player.sendMessage(new Messaging.MessageFormatter().format("error.could-not-join"));
                    }
                } else {
                    e.getPlayer().sendMessage(org.bukkit.ChatColor.RED + "YOU MUST SET SPAWN IN THE LOBBY WORLD WITH /SWR SETSPAWN BEFORE STARTING A GAME");
                    SkyWarsReloaded.get().getLogger().info("YOU MUST SET SPAWN IN THE LOBBY WORLD WITH /SWR SETSPAWN BEFORE STARTING A GAME");
                }
            }
        }
    }
}
