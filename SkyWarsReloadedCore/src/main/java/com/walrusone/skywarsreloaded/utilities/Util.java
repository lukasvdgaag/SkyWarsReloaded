package com.walrusone.skywarsreloaded.utilities;

import com.walrusone.skywarsreloaded.SkyWarsReloaded;
import com.walrusone.skywarsreloaded.game.GameMap;
import com.walrusone.skywarsreloaded.managers.MatchManager;
import com.walrusone.skywarsreloaded.managers.PlayerStat;
import com.walrusone.skywarsreloaded.menus.gameoptions.objects.CoordLoc;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;

public class Util {

    private static Util instance;
    private Random rand;

    public Util() {
        rand = new Random();
    }

    public static Util get() {
        if (Util.instance == null) {
            Util.instance = new Util();
        }
        return Util.instance;
    }

    private static double quadraticEquationRoot(double a, double b, double c) {
        double root1, root2;
        root1 = (-b + Math.sqrt(Math.pow(b, 2) - 4 * a * c)) / (2 * a);
        root2 = (-b - Math.sqrt(Math.pow(b, 2) - 4 * a * c)) / (2 * a);
        return Math.max(root1, root2);
    }

    public int getRandomNum(int min, int max) {
        return rand.nextInt((max - min) + 1) + min;
    }

    public boolean hasPerm(String t, CommandSender sender, String s) {
        if (t.equalsIgnoreCase("sw")) {
            return sender.hasPermission("sw." + s);
        } else if (t.equalsIgnoreCase("kit")) {
            return sender.hasPermission("sw.kit." + s);
        } else if (t.equalsIgnoreCase("map")) {
            return sender.hasPermission("sw.map." + s);
        } else if (t.equalsIgnoreCase("party")) {
            return sender.hasPermission("sw.party." + s);
        }
        return false;
    }

    public String getMessageKey(String type) {
        if (type.equalsIgnoreCase("sw")) {
            return "sw";
        } else if (type.equalsIgnoreCase("kit")) {
            return "swkit";
        } else if (type.equalsIgnoreCase("map")) {
            return "swmap";
        } else if (type.equalsIgnoreCase("party")) {
            return "swparty";
        }
        return "";
    }

    public void playSound(Player player, Location location, String sound, float volume, float pitch) {
        if (SkyWarsReloaded.getCfg().soundsEnabled()) {
            try {
                if (player != null && !sound.equalsIgnoreCase("none")) {
                    player.playSound(location, Sound.valueOf(sound), volume, pitch);
                }
            } catch (IllegalArgumentException e) {
                SkyWarsReloaded.get().getLogger().info("ERROR: " + sound + " is not a valid bukkit sound. Please check your configs");
            }
        }
    }

    public int getMultiplier(Player player) {
        if (player.hasPermission("sw.vip5")) {
            return SkyWarsReloaded.getCfg().getVip5();
        } else if (player.hasPermission("sw.vip4")) {
            return SkyWarsReloaded.getCfg().getVip4();
        } else if (player.hasPermission("sw.vip3")) {
            return SkyWarsReloaded.getCfg().getVip3();
        } else if (player.hasPermission("sw.vip2")) {
            return SkyWarsReloaded.getCfg().getVip2();
        } else if (player.hasPermission("sw.vip1")) {
            return SkyWarsReloaded.getCfg().getVip1();
        } else {
            return 1;
        }
    }

    public void doCommands(List<String> commandList, Player player) {
        for (String com : commandList) {
            String command = com;
            if (player != null) {
                command = com.replace("<player>", player.getName());
            }
            SkyWarsReloaded.get().getServer().dispatchCommand(SkyWarsReloaded.get().getServer().getConsoleSender(), command);
        }
    }

    public boolean isInteger(String s) {
        try {
            Integer.parseInt(s);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public boolean isFloat(String s) {
        try {
            Float.parseFloat(s);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public Location stringToLocation(final String location) {
        if (location != null) {
            final String[] locationParts = location.split(":");
            if (locationParts.length == 4) {
                return new Location(Bukkit.getWorld(locationParts[0]), Double.parseDouble(locationParts[1]), Double.parseDouble(locationParts[2]), Double.parseDouble(locationParts[3]));
            } else if (locationParts.length == 6) {
                return new Location(Bukkit.getWorld(locationParts[0]), Double.parseDouble(locationParts[1]), Double.parseDouble(locationParts[2]), Double.parseDouble(locationParts[3]), Float.parseFloat(locationParts[4]), Float.parseFloat(locationParts[5]));

            }
        }
        return null;
    }

    public String locationToString(final Location location) {
        return location.getWorld().getName() + ":" + location.getX() + ":" + location.getY() + ":" + location.getZ() + ":" + location.getYaw() + ":" + location.getPitch();
    }

    public void sendTitle(Player player, int fadein, int stay, int fadeout, String title, String subtitle) {
        SkyWarsReloaded.getNMS().sendTitle(player, fadein, stay, fadeout, title, subtitle);
    }

    public void clear(final Player player) {
        player.getInventory().clear();
        player.getInventory().setArmorContents(new ItemStack[] {null, null, null, null});
        for (final PotionEffect a1 : player.getActivePotionEffects()) {
            player.removePotionEffect(a1.getType());
        }
    }

    public boolean isBusy(UUID uuid) {
        Player player = SkyWarsReloaded.get().getServer().getPlayer(uuid);

        if (player == null) return true;
        if (player.isDead()) return true;

        if (MatchManager.get().isSpectating(player)) return true;

        PlayerStat ps = PlayerStat.getPlayerStats(player);
        if (ps == null) {
            if (SkyWarsReloaded.getCfg().debugEnabled()) SkyWarsReloaded.get().getLogger().info("#isBusy pStats " + player.getName() + ": null");
            ps = new PlayerStat(player);
            PlayerStat.getPlayers().add(ps);
            ps.updatePlayerIfInLobby(player);
            return true;
        } else {
            return !ps.isInitialized();
        }
    }

    public void fireworks(final Player player, final int length, final int fireworksPer5Tick) {
        final List<FireworkEffect.Type> type = new ArrayList<>(Arrays.asList(FireworkEffect.Type.BALL, FireworkEffect.Type.BALL_LARGE, FireworkEffect.Type.BURST, FireworkEffect.Type.STAR, FireworkEffect.Type.CREEPER));
        final List<Color> colors = new ArrayList<>(Arrays.asList(Color.AQUA, Color.BLACK, Color.BLUE, Color.FUCHSIA, Color.GRAY, Color.GREEN, Color.LIME, Color.MAROON, Color.NAVY, Color.OLIVE, Color.ORANGE, Color.PURPLE, Color.RED, Color.SILVER, Color.TEAL, Color.WHITE, Color.YELLOW));
        final long currentTime = System.currentTimeMillis();
        Random rand = new Random();
        if (SkyWarsReloaded.get().isEnabled()) {
            new BukkitRunnable() {
                public void run() {
                    if (System.currentTimeMillis() >= currentTime + length * 1000 || SkyWarsReloaded.get().getServer().getPlayer(player.getUniqueId()) == null) {
                        this.cancel();
                    } else {
                        for (int i = 0; i < fireworksPer5Tick; ++i) {
                            final Location loc = player.getLocation();
                            @SuppressWarnings({"unchecked", "rawtypes"}) final Firework firework = (Firework) player.getLocation().getWorld().spawn(loc, (Class) Firework.class);
                            final FireworkMeta fMeta = firework.getFireworkMeta();
                            FireworkEffect fe = FireworkEffect.builder().withColor(colors.get(rand.nextInt(17))).withColor(colors.get(rand.nextInt(17)))
                                    .withColor(colors.get(rand.nextInt(17))).with(type.get(rand.nextInt(5))).trail(rand.nextBoolean())
                                    .flicker(rand.nextBoolean()).build();
                            fMeta.addEffects(fe);
                            fMeta.setPower(new Random().nextInt(2) + 2);
                            firework.setFireworkMeta(fMeta);
                        }
                    }
                }
            }.runTaskTimer(SkyWarsReloaded.get(), 0L, 5L);
        }
    }

    public void sendParticles(final World world, final String type, final float x, final float y, final float z, final float offsetX, final float offsetY, final float offsetZ, final float data, final int amount) {
        if (SkyWarsReloaded.get().isEnabled()) {
            new BukkitRunnable() {
                public void run() {
                    SkyWarsReloaded.getNMS().sendParticles(world, type, x, y, z, offsetX, offsetY, offsetZ, data, amount);
                }
            }.runTaskAsynchronously(SkyWarsReloaded.get());
        }
    }

    private List<Block> getBlocks(Location center, int radius) {
        List<Location> locs = circle(center, radius);
        List<Block> blocks = new ArrayList<>();

        for (Location loc : locs) {
            blocks.add(loc.getBlock());
        }

        return blocks;
    }

    private List<Location> circle(Location loc, int radius) {
        List<Location> circleblocks = new ArrayList<>();
        int cx = loc.getBlockX();
        int cy = loc.getBlockY();
        int cz = loc.getBlockZ();

        for (int x = cx - radius; x <= cx + radius; x++) {
            for (int z = cz - radius; z <= cz + radius; z++) {
                for (int y = (cy - radius); y < (cy + radius); y++) {
                    double dist = (cx - x) * (cx - x) + (cz - z) * (cz - z) + ((cy - y) * (cy - y));
                    if (dist < radius * radius
                            && !(dist < (radius - 1) * (radius - 1))) {
                        Location l = new Location(loc.getWorld(), x, y, z);
                        circleblocks.add(l);
                    }
                }
            }
        }

        return circleblocks;
    }

    public void surroundParticles(Player player, int r, final List<String> types, final int density, final double speed) {
        if (SkyWarsReloaded.get().isEnabled()) {
            if (player != null) {
                Location l = player.getLocation().add(0, 1, 0);
                final Random random = new Random();
                for (final Block b : getBlocks(l, r)) {
                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            sendParticles(b.getWorld(), types.get(random.nextInt(types.size())), (float) b.getLocation().getX(), (float) b.getLocation().getY(), (float) b.getLocation().getZ(), random.nextFloat(), random.nextFloat(), random.nextFloat(), (float) speed, random.nextInt((density - density / 2) + density / 2) + 1);
                            sendParticles(b.getWorld(), types.get(random.nextInt(types.size())), (float) b.getLocation().getX(), (float) b.getLocation().getY(), (float) b.getLocation().getZ(), random.nextFloat(), random.nextFloat(), random.nextFloat(), (float) speed, random.nextInt((density - density / 2) + density / 2) + 1);
                            sendParticles(b.getWorld(), types.get(random.nextInt(types.size())), (float) b.getLocation().getX(), (float) b.getLocation().getY(), (float) b.getLocation().getZ(), random.nextFloat(), random.nextFloat(), random.nextFloat(), (float) speed, random.nextInt((density - density / 2) + density / 2) + 1);
                        }
                    }.runTaskAsynchronously(SkyWarsReloaded.get());
                }
            }
        }
    }

    public void respawnPlayer(Player player) {
        SkyWarsReloaded.getNMS().respawnPlayer(player);
    }

    public String getDeathMessage(@Nullable DamageCause dCause, boolean withHelp, Player target, @Nullable Player killer) {
        String first;
        String second = null;
        if (withHelp) {
            second = new Messaging.MessageFormatter()
                    .setVariable("killer", killer.getName())
                    .format("game.death.killer-section");
        }

        if (dCause == null) dCause = DamageCause.CUSTOM;

        if (dCause.equals(DamageCause.BLOCK_EXPLOSION) || dCause.equals(DamageCause.ENTITY_EXPLOSION)) {
            first = new Messaging.MessageFormatter()
                    .withPrefix()
                    .setVariable("player", target.getName())
                    .format("game.death.explosion");
        } else if (dCause.equals(DamageCause.DROWNING)) {
            first = new Messaging.MessageFormatter()
                    .withPrefix()
                    .setVariable("player", target.getName())
                    .format("game.death.drowning");
        } else if (dCause.equals(DamageCause.FIRE) || dCause.equals(DamageCause.FIRE_TICK)) {
            first = new Messaging.MessageFormatter()
                    .withPrefix()
                    .setVariable("player", target.getName())
                    .format("game.death.fire");
        } else if (dCause.equals(DamageCause.ENTITY_ATTACK) && killer != null) {
            first = new Messaging.MessageFormatter()
                    .withPrefix()
                    .setVariable("player", target.getName())
                    .setVariable("killer", killer.getName())
                    .format("game.death.pvp");
            second = "";
        } else if (dCause.equals(DamageCause.FALLING_BLOCK)) {
            first = new Messaging.MessageFormatter()
                    .withPrefix()
                    .setVariable("player", target.getName())
                    .format("game.death.falling-block");
        } else if (dCause.equals(DamageCause.LAVA)) {
            first = new Messaging.MessageFormatter()
                    .withPrefix()
                    .setVariable("player", target.getName())
                    .format("game.death.lava");
        } else if (dCause.equals(DamageCause.PROJECTILE) && killer != null) {
            first = new Messaging.MessageFormatter()
                    .withPrefix()
                    .setVariable("player", target.getName())
                    .setVariable("killer", killer.getName())
                    .format("game.death.projectile");
            second = "";
        } else if (dCause.equals(DamageCause.SUFFOCATION)) {
            first = new Messaging.MessageFormatter()
                    .withPrefix()
                    .setVariable("player", target.getName())
                    .format("game.death.suffocation");
        } else if (dCause.equals(DamageCause.VOID)) {
            first = new Messaging.MessageFormatter()
                    .withPrefix()
                    .setVariable("player", target.getName())
                    .format("game.death.void");
        } else {
            first = new Messaging.MessageFormatter()
                    .withPrefix()
                    .setVariable("player", target.getName())
                    .format("game.death.general");
        }

        if (withHelp) {
            return first + second;
        } else {
            return first + "!";
        }
    }

    public List<Chunk> getChunks(World mapWorld) {
        int size = 400;
        int maxX = size / 2;
        int minX = -size / 2;
        int maxZ = size / 2;
        int minZ = -size / 2;
        int minY = 0;
        int maxY = 0;
        Block min = mapWorld.getBlockAt(minX, minY, minZ);
        Block max = mapWorld.getBlockAt(maxX, maxY, maxZ);
        Chunk cMin = min.getChunk();
        Chunk cMax = max.getChunk();
        List<Chunk> chunks = new ArrayList<>();

        for (int cx = cMin.getX(); cx < cMax.getX(); cx++) {
            for (int cz = cMin.getZ(); cz < cMax.getZ(); cz++) {
                Chunk currentChunk = mapWorld.getChunkAt(cx, cz);
                chunks.add(currentChunk);
            }
        }
        return chunks;
    }

    public ItemStack name(ItemStack itemStack, String name, String... lores) {
        ItemMeta itemMeta = itemStack.getItemMeta();

        if (!name.isEmpty()) {
            itemMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', name));
        }

        if (lores.length > 0) {
            List<String> loreList = new ArrayList<>(lores.length);

            for (String lore : lores) {
                loreList.add(ChatColor.translateAlternateColorCodes('&', lore));
            }

            itemMeta.setLore(loreList);
        }

        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }

    public void logToFile(String message) {
        ConsoleCommandSender console = SkyWarsReloaded.get().getServer().getConsoleSender();
        console.sendMessage(message);

        try {
            File dataFolder = SkyWarsReloaded.get().getDataFolder();
            if (dataFolder.exists() || dataFolder.mkdir()) {
                File saveTo = new File(dataFolder, "DebugLog.txt");
                if (saveTo.exists() || saveTo.createNewFile()) {
                    FileWriter fw = new FileWriter(saveTo, true);
                    PrintWriter pw = new PrintWriter(fw);
                    pw.println(ChatColor.stripColor(message));
                    pw.flush();
                    pw.close();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendActionBar(Player p, String msg) {
        SkyWarsReloaded.getNMS().sendActionBar(p, msg);
    }

    public byte getByteFromColor(String color) {
        switch (color.toLowerCase()) {
            case "white":
                return (byte) 0;
            case "orange":
                return (byte) 1;
            case "magenta":
                return (byte) 2;
            case "lightblue":
                return (byte) 3;
            case "yellow":
                return (byte) 4;
            case "lime":
                return (byte) 5;
            case "pink":
                return (byte) 6;
            case "gray":
                return (byte) 7;
            case "lightgray":
                return (byte) 8;
            case "cyan":
                return (byte) 9;
            case "purple":
                return (byte) 10;
            case "blue":
                return (byte) 11;
            case "brown":
                return (byte) 12;
            case "green":
                return (byte) 13;
            case "red":
                return (byte) 14;
            case "black":
                return (byte) 15;
            case "none":
                return (byte) -2;
            case "lapis":
                return (byte) -3;
            case "redstone":
                return (byte) -4;
            case "emerald":
                return (byte) -5;
            case "diamond":
                return (byte) -6;
            default:
                return (byte) -1;
        }
    }

    public void setPlayerExperience(Player player, int amount) {
        if (amount <= 352) {
            int level = (int) Math.floor(quadraticEquationRoot(1, 6, -amount));
            double nextLevel = 2 * level + 7;
            double levelExp = (level * level) + 6 * level;
            double leftOver = amount - levelExp;
            player.setLevel(level);
            player.setExp((float) (leftOver / nextLevel));
        } else if (amount <= 1507) {
            int level = (int) Math.floor(quadraticEquationRoot(2.5, -40.5, 360 - amount));
            double nextLevel = 5 * level - 38;
            double levelExp = (int) (2.5 * (level * level) - 40.5 * level + 360);
            double leftOver = amount - levelExp;
            player.setLevel(level);
            player.setExp((float) (leftOver / nextLevel));
        } else {
            int level = (int) Math.floor(quadraticEquationRoot(4.5, -162.5, 2220 - amount));
            double nextLevel = 9 * level - 158;
            double levelExp = (int) (4.5 * (level * level) - 162.5 * level + 2220);
            double leftOver = amount - levelExp;
            player.setLevel(level);
            player.setExp((float) (leftOver / nextLevel));
        }
    }

    public static HashMap<GameMap, Integer> getSortedGames(List<GameMap> hm) {
        HashMap<GameMap, Integer> games = new HashMap<>();
        for (GameMap g : hm) {
            if (g.canAddPlayer()) games.put(g, g.getAllPlayers().size());
        }

        // Create a list from elements of HashMap
        List<Map.Entry<GameMap, Integer>> list = new LinkedList<>(games.entrySet());

        // Sort the list
        Collections.sort(list, new Comparator<Map.Entry<GameMap, Integer>>() {
            public int compare(Map.Entry<GameMap, Integer> o1,
                               Map.Entry<GameMap, Integer> o2) {
                return (o1.getValue()).compareTo(o2.getValue());
            }
        });

        // put data from sorted list to hashmap
        HashMap<GameMap, Integer> temp = new LinkedHashMap<GameMap, Integer>();
        for (Map.Entry<GameMap, Integer> aa : list) {
            temp.put(aa.getKey(), aa.getValue());
        }
        return temp;
    }

    public int getPlayerLevel(Player player, boolean checkForActualEXP) {
        if (SkyWarsReloaded.getCfg().displayPlayerExeperience() && checkForActualEXP) {
            return player.getLevel();
        } else {
            PlayerStat ps = PlayerStat.getPlayerStats(player);
            if (ps != null) {
                int amount = ps.getXp();
                if (amount <= 352) {
                    return (int) Math.floor(quadraticEquationRoot(1, 6, -amount));
                } else if (amount <= 1507) {
                    return (int) Math.floor(quadraticEquationRoot(2.5, -40.5, 360 - amount));
                } else {
                    return (int) Math.floor(quadraticEquationRoot(4.5, -162.5, 2220 - amount));
                }
            } else {
                return 0;
            }
        }
    }

    public String getFormattedTime(int x) {
        return secondsToTimeString(x);
       /* String hms;
        if (x >= 3600) {
            hms = String.format("%02d:%02d:%02d", TimeUnit.SECONDS.toHours(x),
                    TimeUnit.SECONDS.toMinutes(x) % TimeUnit.HOURS.toMinutes(1),
                    TimeUnit.SECONDS.toSeconds(x) % TimeUnit.MINUTES.toSeconds(1));
        } else {
            hms = String.format("%02d:%02d", TimeUnit.SECONDS.toMinutes(x) % TimeUnit.HOURS.toMinutes(1),
                    TimeUnit.SECONDS.toSeconds(x) % TimeUnit.MINUTES.toSeconds(1));
        }
        return hms;*/
    }

    public boolean isSpawnWorld(World world) {
        return SkyWarsReloaded.getCfg().getSpawn() != null && world.equals(SkyWarsReloaded.getCfg().getSpawn().getWorld());
    }

    public CoordLoc getCoordLocFromString(String location) {
        if (location != null) {
            final String[] locationParts = location.split(":");
            if (locationParts.length != 3) {
                return null;
            } else {
                return new CoordLoc(Integer.parseInt(locationParts[0]), Integer.parseInt(locationParts[1]), Integer.parseInt(locationParts[2]));
            }
        }
        return null;
    }

    public String secondsToTimeString(int seconds) {
        String returnFormat = SkyWarsReloaded.getCfg().getTimeFormat();
        int secs;
        int mins = 0;

        if (seconds < 60) {
            secs = seconds;
        } else {
            mins = (int) seconds / 60;
            secs = (int) seconds % 60;
        }

        if (returnFormat.equals("simplified")) {
            if (mins > 0) {
                return mins + ":" + (secs < 10 ? "0" + secs : secs);
            }
            else {
                return secs + "";
            }
        }
        if (returnFormat.equals("simplified-zeros")) {
            if (mins > 0) {
                return (mins < 10 ? "0" + mins : mins) + ":" + (secs < 10 ? "0" + secs : secs);
            }
            else {
                return secs + "";
            }
        }

        returnFormat = returnFormat.replaceAll("(?<!\\\\)mm", mins < 10 ? "0" + mins : mins +"");
        returnFormat = returnFormat.replaceAll("(?<!\\\\)m", mins + "");
        returnFormat = returnFormat.replaceAll("(?<!\\\\)ss", secs < 10 ? "0" + secs : secs +"");
        returnFormat = returnFormat.replaceAll("(?<!\\\\)s", secs + "");

        return returnFormat;
    }

    public int getPlayerLevel(Player player) {
        return getPlayerLevel(player, true);
    }

    public void glowItem(Inventory inv, int slot) {
        if (inv == null) return;
        ItemStack item = inv.getItem(slot);
        if (item == null) return;
        ItemMeta meta = item.getItemMeta();
        meta.addEnchant(Enchantment.DURABILITY,1,true);
        meta.addItemFlags(ItemFlag.values());
        item.setItemMeta(meta);
    }

    public void ejectPassengers(Player player) {
        if (player.getVehicle() != null) player.getVehicle().eject();
        player.eject();
    }

}