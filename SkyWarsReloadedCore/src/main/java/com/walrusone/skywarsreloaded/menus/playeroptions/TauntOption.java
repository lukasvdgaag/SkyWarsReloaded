package com.walrusone.skywarsreloaded.menus.playeroptions;

import com.walrusone.skywarsreloaded.SkyWarsReloaded;
import com.walrusone.skywarsreloaded.managers.PlayerStat;
import com.walrusone.skywarsreloaded.utilities.Messaging;
import com.walrusone.skywarsreloaded.utilities.Util;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class TauntOption extends PlayerOption {

    private static ArrayList<PlayerOption> playerOptions = new ArrayList<>();
    private List<String> lore;
    private String message;
    private String sound;
    private boolean useCustomSound;
    private float volume;
    private float pitch;
    private double speed;
    private int density;
    private List<String> stringParticles;

    private TauntOption(String key, String name, List<String> lore, String message, String sound, boolean useCustomSound, double volume, double pitch, double speed, int density, List<String> particles, Material icon, int level, int cost, int position, int page, int menuSize) {
        this.key = key;
        this.name = name;
        this.lore = lore;
        this.message = message;
        this.useCustomSound = useCustomSound;
        this.sound = sound;
        this.volume = (float) volume;
        this.pitch = (float) pitch;
        this.density = density;
        this.speed = speed;
        this.item = new ItemStack(icon, 1);
        this.stringParticles = particles;
        this.level = level;
        this.cost = cost;
        this.position = position;
        this.page = page;
        this.menuSize = menuSize;
    }

    private static void saveTauntFile(String filename) {
        SkyWarsReloaded.get().saveResource(filename, false);
        File sf = new File(SkyWarsReloaded.get().getDataFolder(), filename);
        if (sf.exists()) {
            boolean result = sf.renameTo(new File(SkyWarsReloaded.get().getDataFolder(), "taunts.yml"));
            if (!result) {
                SkyWarsReloaded.get().getLogger().info("Failed to rename Taunts File");
            }
        }
    }

    public static void loadPlayerOptions() {
        playerOptions.clear();
        File tauntFile = new File(SkyWarsReloaded.get().getDataFolder(), "taunts.yml");

        if (!tauntFile.exists()) {
            if (SkyWarsReloaded.getNMS().getVersion() < 9) {
                saveTauntFile("taunts18.yml");
            } else if (SkyWarsReloaded.getNMS().getVersion() < 13) {
                saveTauntFile("taunts112.yml");
            } else {
                SkyWarsReloaded.get().saveResource("taunts.yml", false);
            }
        }

        if (tauntFile.exists()) {
            FileConfiguration storage = YamlConfiguration.loadConfiguration(tauntFile);

            if (storage.getConfigurationSection("taunts") != null) {
                for (String key : storage.getConfigurationSection("taunts").getKeys(false)) {
                    String name = storage.getString("taunts." + key + ".name");
                    List<String> lore = storage.getStringList("taunts." + key + ".lore");
                    int level = storage.getInt("taunts." + key + ".level");
                    int cost = storage.getInt("taunts." + key + ".cost");
                    String message = storage.getString("taunts." + key + ".message");
                    String sound = storage.getString("taunts." + key + ".sound");
                    boolean useCustomSound = storage.getBoolean("taunts." + key + ".useCustomSound", false);
                    double volume = storage.getDouble("taunts." + key + ".volume");
                    double pitch = storage.getDouble("taunts." + key + ".pitch");
                    double speed = storage.getDouble("taunts." + key + ".particleSpeed");
                    int density = storage.getInt("taunts." + key + ".particleDensity");
                    List<String> particles = storage.getStringList("taunts." + key + ".particles");
                    Material icon = Material.valueOf(storage.getString("taunts." + key + ".icon", "DIAMOND"));
                    int position = storage.getInt("taunts." + key + ".position");
                    int page = storage.getInt("taunts." + key + ".page");
                    int menuSize = storage.getInt("menuSize");
                    playerOptions.add(new TauntOption(key, name, lore, message, sound, useCustomSound, volume, pitch, speed, density, particles, icon, level, cost, position, page, menuSize));
                }
            }
        }
        Collections.sort(playerOptions);
        if (playerOptions.size()>=4 && playerOptions.get(3) != null && playerOptions.get(3).getPosition() == 0 || playerOptions.get(3).getPage() == 0) {
            FileConfiguration storage = YamlConfiguration.loadConfiguration(tauntFile);
            updateFile(tauntFile, storage);
        }
    }

    private static void updateFile(File file, FileConfiguration storage) {
        ArrayList<Integer> placement = new ArrayList<>(Arrays.asList(0, 2, 4, 6, 8, 9, 11, 13, 15, 17, 18, 20, 22, 24, 26, 27, 29, 31, 33, 35,
                36, 38, 40, 42, 44, 45, 47, 49, 51, 53));
        storage.set("menuSize", 45);
        for (int i = 0; i < playerOptions.size(); i++) {
            playerOptions.get(i).setPosition(placement.get(i) % 45);
            playerOptions.get(i).setPage((Math.floorDiv(placement.get(i), 45)) + 1);
            playerOptions.get(i).setMenuSize(45);
            storage.set("taunts." + playerOptions.get(i).getKey() + ".position", playerOptions.get(i).getPosition());
            storage.set("taunts." + playerOptions.get(i).getKey() + ".page", playerOptions.get(i).getPage());
        }
        try {
            storage.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    static PlayerOption getPlayerOptionByName(String name) {
        for (PlayerOption pOption : playerOptions) {
            if (ChatColor.stripColor(ChatColor.translateAlternateColorCodes('&', pOption.getName())).equalsIgnoreCase(ChatColor.stripColor(name))) {
                return pOption;
            }
        }
        return null;
    }

    public static PlayerOption getPlayerOptionByKey(String key) {
        for (PlayerOption pOption : playerOptions) {
            if (pOption.getKey().equalsIgnoreCase(key)) {
                return pOption;
            }
        }
        return null;
    }

    static ArrayList<PlayerOption> getPlayerOptions() {
        return playerOptions;
    }

    public List<String> getLore() {
        return this.lore;
    }

    public String getMessage() {
        return this.message;
    }

    public float getVolume() {
        return this.volume;
    }

    /**
     * Does the taunt for a player
     */
    public void performTaunt(Player player) {
        if (!this.getKey().equalsIgnoreCase("none")) {
            SkyWarsReloaded.getNMS().playGameSound(player.getLocation(), sound, volume, pitch, useCustomSound);

            doTauntParticles(player.getUniqueId().toString());

            if (this.getMessage() != null && this.getMessage().length() != 0) {
                String prefix = new Messaging.MessageFormatter().setVariable("player", player.getDisplayName()).format("taunt.prefix");
                List<Player> players = player.getWorld().getPlayers();
                for (Player p : players) {
                    if (p.getLocation().distance(player.getLocation()) < this.getVolume() * 15) {
                        p.sendMessage(ChatColor.translateAlternateColorCodes('&', prefix + " " + this.getMessage()));
                    }
                }
            }
        }
    }

    /**
     * Creates a sphere of particles around the player
     */
    private void doTauntParticles(String uuid) {
        Player player = SkyWarsReloaded.get().getServer().getPlayer(UUID.fromString(uuid));
        if (player != null) {
            Util.get().surroundParticles(player, 2, stringParticles, density, speed);
        }
    }

    @Override
    public String getPermission() {
        return ("sw.taunt." + key);
    }

    @Override
    public String getMenuName() {
        return "menu.usetaunt-menu-title";
    }

    @Override
    public String getPurchaseMessage() {
        return new Messaging.MessageFormatter().setVariable("cost", "" + cost)
                .setVariable("item", name).format("menu.purchase-taunt");
    }

    @Override
    public String getUseMessage() {
        return new Messaging.MessageFormatter().setVariable("taunt", name).format("menu.usetaunt-playermsg");
    }

    @Override
    public void setEffect(PlayerStat stat) {
        stat.setTaunt(key);
    }

    @Override
    public String getUseLore() {
        return "menu.usetaunt-settaunt";
    }

}
