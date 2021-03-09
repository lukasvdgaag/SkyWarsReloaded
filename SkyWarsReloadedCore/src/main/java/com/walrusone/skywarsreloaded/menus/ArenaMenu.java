package com.walrusone.skywarsreloaded.menus;

import com.walrusone.skywarsreloaded.SkyWarsReloaded;
import com.walrusone.skywarsreloaded.enums.MatchState;
import com.walrusone.skywarsreloaded.game.GameMap;
import com.walrusone.skywarsreloaded.game.cages.CageType;
import com.walrusone.skywarsreloaded.game.signs.SWRSign;
import com.walrusone.skywarsreloaded.listeners.ChatListener;
import com.walrusone.skywarsreloaded.utilities.Messaging;
import me.rayzr522.jsonmessage.JSONMessage;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.BlockState;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;

public class ArenaMenu {

    private static final String menuName = ChatColor.DARK_PURPLE + "Arena Manager: ";

    public ArenaMenu(String key, GameMap gMap) {
        int menuSize = 27;
        Inventory menu = Bukkit.createInventory(null, menuSize + 9, menuName + gMap.getName());
        ArrayList<Inventory> invs = new ArrayList<>();
        invs.add(menu);

        Runnable update = () -> {
            if (SkyWarsReloaded.getIC().hasViewers(key)) {
                List<String> lores = new ArrayList<>();
                lores.add(ChatColor.GOLD + "" + gMap.getMinTeams());
                lores.add(ChatColor.AQUA + "Left Click to Increase.");
                lores.add(ChatColor.AQUA + "Right Click to Decrease.");
                ItemStack min = SkyWarsReloaded.getNMS().getItemStack(new ItemStack(Material.DIAMOND_HELMET, 1), lores, "Minimun Players");


                lores.clear();
                if (gMap.isRegistered()) {
                    lores.add(ChatColor.GREEN + "REGISTERED");
                    lores.add(ChatColor.GOLD + gMap.getMatchState().toString().toUpperCase());
                    lores.add(ChatColor.GREEN + "" + gMap.getAlivePlayers().size() + " of " + gMap.getMaxPlayers() + " Players");
                    lores.add(" ");
                    lores.add(ChatColor.RED + "Shift Left Click to Unregister.");
                    lores.add(ChatColor.RED + "Unregistering will end the match!");
                } else {
                    lores.add(ChatColor.RED + "UNREGISTERED");
                    lores.add(" ");
                    lores.add(ChatColor.AQUA + "Shift Left Click to");
                    lores.add(ChatColor.AQUA + "Attempt Registration!");
                }
                ItemStack status = SkyWarsReloaded.getNMS().getItemStack(new ItemStack(Material.MAP, 1), lores, "Arena Status");

                lores.clear();
                lores.add(ChatColor.translateAlternateColorCodes('&', gMap.getDisplayName()));
                lores.add(" ");
                lores.add(ChatColor.AQUA + "Left Click to Change");
                lores.add(ChatColor.AQUA + "the Display Name.");
                ItemStack display = SkyWarsReloaded.getNMS().getItemStack(new ItemStack(Material.NAME_TAG, 1), lores, "Display Name");

                lores.clear();
                lores.add(ChatColor.translateAlternateColorCodes('&', gMap.getDesigner()));
                lores.add(" ");
                lores.add(ChatColor.AQUA + "Left Click to Change");
                lores.add(ChatColor.AQUA + "the Creator.");
                ItemStack creator = SkyWarsReloaded.getNMS().getItemStack(SkyWarsReloaded.getNMS().getMaterial("SKULL_ITEM"), lores, "Map Creator");

                lores.clear();
                lores.add(ChatColor.GOLD + "" + gMap.getSigns().size() + " Sign Available!");
                lores.add(" ");
                lores.add(ChatColor.AQUA + "Left Click for a list");
                lores.add(ChatColor.AQUA + "of Sign Locations.");


                String signItem = "SIGN";
                if (SkyWarsReloaded.getNMS().getVersion() >= 13) {
                    signItem = "BIRCH_SIGN";
                }

                ItemStack signs = SkyWarsReloaded.getNMS().getItemStack(new ItemStack(Material.valueOf(signItem), 1), lores, "Join Signs");

                lores.clear();
                if (gMap.isRegistered()) {
                    lores.add(ChatColor.AQUA + "Left Click to spectate!");
                } else {
                    lores.add(ChatColor.RED + "Spectate Currently Unavailable!");
                }
                ItemStack spectate = SkyWarsReloaded.getNMS().getItemStack(new ItemStack(Material.COMPASS, 1), lores, "Spectate");

                lores.clear();
                lores.add(ChatColor.AQUA + "Shift Left Click to");
                lores.add(ChatColor.AQUA + "End Current Match!");
                ItemStack end = SkyWarsReloaded.getNMS().getItemStack(new ItemStack(SkyWarsReloaded.getNMS().getMaterial("ENDER_PORTAL_FRAME")), lores, "End Match");

                lores.clear();
                lores.add(ChatColor.AQUA + "Shift Left Click to Edit Map!");
                if (gMap.isRegistered()) {
                    lores.add(ChatColor.RED + "Editing will End Match");
                    lores.add(ChatColor.RED + "and unregister the map!");
                }
                ItemStack edit = SkyWarsReloaded.getNMS().getItemStack(new ItemStack(SkyWarsReloaded.getNMS().getMaterial("WORKBENCH")), lores, "Edit Map");

                lores.clear();

                if (gMap.isEditing()) {
                    lores.add(ChatColor.AQUA + "Shift Left Click to Save Map!");
                    lores.add(ChatColor.RED + "Saving will close map");
                    lores.add(ChatColor.RED + "from editing!");
                } else {
                    lores.add(ChatColor.RED + "Map is not being edited!");
                }
                ItemStack save = SkyWarsReloaded.getNMS().getItemStack(new ItemStack(Material.BOOK, 1), lores, "Save Map");

                lores.clear();
                lores.add(ChatColor.GOLD + gMap.getCage().getType().toString());
                if (gMap.isRegistered()) {
                    lores.add(ChatColor.RED + "Cage Type cannot be");
                    lores.add(ChatColor.RED + "Changed while registered!");
                } else {
                    lores.add(ChatColor.AQUA + "Left Click to Change Cage Type!");
                }
                ItemStack cage = SkyWarsReloaded.getNMS().getItemStack(new ItemStack(SkyWarsReloaded.getNMS().getMaterial("IRON_FENCE")), lores, "Cage Type");

                lores.clear();
                lores.add(ChatColor.AQUA + "Left Click to view events!");
                ItemStack events = SkyWarsReloaded.getNMS().getItemStack(new ItemStack(Material.JUKEBOX, 1), lores, "Events");

                lores.clear();
                lores.add(ChatColor.GREEN + "Team Size: " + ChatColor.GOLD + gMap.getTeamSize());
                if (gMap.allowFriendlyFire()) {
                    lores.add(ChatColor.GREEN + "Freindly Fire Enabled: " + ChatColor.GREEN + "TRUE");
                } else {
                    lores.add(ChatColor.GREEN + "Freindly Fire Enabled: " + ChatColor.RED + "FALSE");
                }
                if (gMap.isRegistered()) {
                    lores.add(" ");
                    lores.add(ChatColor.RED + "Values cannot be changed");
                    lores.add(ChatColor.RED + "while map is registered!");
                } else {
                    lores.add(" ");
                    lores.add(ChatColor.AQUA + "Left click to increase team size!");
                    lores.add(ChatColor.AQUA + "Right click to decrease team size!");
                    lores.add(ChatColor.AQUA + "Shift Left Click to toggle");
                    lores.add(ChatColor.AQUA + "Friendly Fire!");
                }
                ItemStack teams = SkyWarsReloaded.getNMS().getItemStack(new ItemStack(SkyWarsReloaded.getNMS().getMaterial("REDSTONE_COMPARATOR")), lores, "Teams");

                menu.setItem(0, status);
                menu.setItem(2, display);
                menu.setItem(4, creator);
                menu.setItem(6, min);
                menu.setItem(8, signs);
                menu.setItem(10, spectate);
                menu.setItem(12, end);
                menu.setItem(14, edit);
                menu.setItem(16, save);
                menu.setItem(20, cage);
                menu.setItem(22, events);
                menu.setItem(24, teams);
            }
        };

        SkyWarsReloaded.getIC().create(key, invs, event -> {
            Player player = event.getPlayer();
            String name = event.getName();
            if (name.equalsIgnoreCase(SkyWarsReloaded.getNMS().getItemName(SkyWarsReloaded.getIM().getItem("exitMenuItem")))) {
                SkyWarsReloaded.getIC().show(player, "arenasmenu");
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        GameMap.updateArenasManager();
                    }
                }.runTaskLater(SkyWarsReloaded.get(), 2);
                return;
            }
            if (player.hasPermission("sw.map.arenas")) {
                if (event.getClick().equals(ClickType.SHIFT_LEFT) && event.getSlot() == 0) {
                    if (gMap.isRegistered()) {
                        gMap.setRegistered(false);
                        gMap.unregister(true);
                    } else {
                        gMap.setRegistered(true);
                        gMap.registerMap();
                    }
                    gMap.update();
                } else if (event.getClick().equals(ClickType.LEFT) && event.getSlot() == 2) {
                    player.closeInventory();
                    ChatListener.setTime(player.getUniqueId(), System.currentTimeMillis());
                    ChatListener.setSetting(player.getUniqueId(), gMap.getName() + ":display");
                    player.sendMessage(new Messaging.MessageFormatter().format("maps.changename"));
                } else if (event.getClick().equals(ClickType.LEFT) && event.getSlot() == 4) {
                    player.closeInventory();
                    ChatListener.setTime(player.getUniqueId(), System.currentTimeMillis());
                    ChatListener.setSetting(player.getUniqueId(), gMap.getName() + ":creator");
                    player.sendMessage(new Messaging.MessageFormatter().format("maps.changecreator"));
                } else if (event.getClick().equals(ClickType.LEFT) && event.getSlot() == 6) {
                    if (gMap.getMinTeams() < gMap.getMaxPlayers()) {
                        gMap.setMinTeams(gMap.getMinTeams() + 1);
                        gMap.update();
                    }
                } else if (event.getClick().equals(ClickType.RIGHT) && event.getSlot() == 6) {
                    if (gMap.getMinTeams() > 2) {
                        gMap.setMinTeams(gMap.getMinTeams() - 1);
                        gMap.update();
                    }
                } else if (event.getClick().equals(ClickType.LEFT) && event.getSlot() == 8) {
                    player.closeInventory();
                    for (int i = 1; i <= gMap.getSigns().size(); i++) {
                        SWRSign swSign = gMap.getSigns().get(i - 1);
                        BlockState bs = swSign.getLocation().getBlock().getState();
                        Sign sign;
                        if (bs instanceof Sign) {
                            sign = (Sign) bs;
                            Block block = sign.getBlock();
                            org.bukkit.material.Sign meteSign = (org.bukkit.material.Sign) block.getState().getData();
                            BlockFace facing = meteSign.getFacing();
                            Location loc = block.getLocation();

                            switch (facing) {
                                case NORTH:
                                    loc.setX(loc.getX() - 1);
                                    break;
                                case SOUTH:
                                    loc.setX(loc.getX() + 1);
                                    break;
                                case EAST:
                                    loc.setZ(loc.getZ() + 1);
                                    break;
                                case WEST:
                                    loc.setZ(loc.getZ() + 1);
                                    break;
                                default:
                                    break;
                            }

                            World world = swSign.getLocation().getWorld();
                            int x = loc.getBlockX();
                            int y = loc.getBlockY();
                            int z = loc.getBlockZ();

                            JSONMessage.create("Sign " + i + ": " + world.getName() + " - " + block.getLocation().getBlockX() + ", " + block.getLocation().getBlockY() + ", " + block.getLocation().getBlockZ())
                                    .color(ChatColor.GOLD)
                                    .tooltip("Click to teleport")
                                    .runCommand("/teleport " + x + " " + y + " " + z)
                                    .color(ChatColor.GOLD).send(player);
                        }

                    }
                } else if (event.getClick().equals(ClickType.LEFT) && event.getSlot() == 10) {
                    if (gMap.isRegistered()) {
                        player.closeInventory();
                        if (gMap.getMatchState() != MatchState.OFFLINE && gMap.getMatchState() != MatchState.ENDING) {
                            new BukkitRunnable() {
                                @Override
                                public void run() {
                                    SkyWarsReloaded.get().getPlayerManager().addSpectator(gMap, player);
                                }
                            }.runTaskLater(SkyWarsReloaded.get(), 5);
                        }
                    }
                } else if (event.getClick().equals(ClickType.LEFT) && event.getSlot() == 12) {
                    if (gMap.isRegistered() && !gMap.getMatchState().equals(MatchState.OFFLINE) && !gMap.getMatchState().equals(MatchState.ENDING)) {
                        gMap.stopGameInProgress();
                        gMap.refreshMap();
                    }
                } else if (event.getClick().equals(ClickType.SHIFT_LEFT) && event.getSlot() == 14) {
                    player.closeInventory();
                    GameMap.editMap(gMap, player);
                } else if (event.getClick().equals(ClickType.SHIFT_LEFT) && event.getSlot() == 16) {
                    player.closeInventory();
                    gMap.saveMap(player);
                } else if (event.getClick().equals(ClickType.LEFT) && event.getSlot() == 20) {
                    if (!gMap.isRegistered()) {
                        gMap.setCage(CageType.getNext(gMap.getCage().getType()));
                        gMap.update();
                    }
                } else if (event.getClick().equals(ClickType.LEFT) && event.getSlot() == 22) {
                    new EventsMenu(player, gMap);
                } else if (event.getClick().equals(ClickType.SHIFT_LEFT) && event.getSlot() == 24) {
                    if (!gMap.isRegistered()) {
                        gMap.setFriendlyFire(!gMap.allowFriendlyFire());
                        gMap.update();
                    }
                } else if (event.getClick().equals(ClickType.LEFT) && event.getSlot() == 24) {
                    if (gMap.getTeamSize() < 8 && !gMap.isRegistered()) {
                        gMap.setTeamSize(gMap.getTeamSize() + 1);
                        gMap.update();
                    }
                } else if (event.getClick().equals(ClickType.RIGHT) && event.getSlot() == 24) {
                    if (gMap.getTeamSize() > 1 && !gMap.isRegistered()) {
                        gMap.setTeamSize(gMap.getTeamSize() - 1);
                        gMap.update();
                    }
                }
            }
        });

        SkyWarsReloaded.getIC().getMenu(key).setUpdate(update);
    }

}