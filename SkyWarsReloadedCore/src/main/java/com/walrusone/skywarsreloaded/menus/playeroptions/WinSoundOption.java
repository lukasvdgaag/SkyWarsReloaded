package com.walrusone.skywarsreloaded.menus.playeroptions;

import com.walrusone.skywarsreloaded.SkyWarsReloaded;
import com.walrusone.skywarsreloaded.managers.PlayerStat;
import com.walrusone.skywarsreloaded.utilities.Messaging;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class WinSoundOption extends PlayerOption {

    private static ArrayList<PlayerOption> playerOptions = new ArrayList<>();
    private String sound;
    private float volume;
    private float pitch;
    private boolean customSound;

    private WinSoundOption(String key, String sound, String displayname, int level, int cost, float volume, float pitch, Material material, boolean customSound, int position, int page, int menuSize) {
        this.level = level;
        this.cost = cost;
        this.key = key;
        this.sound = sound;
        this.name = displayname;
        this.customSound = customSound;
        this.item = new ItemStack(material, 1);
        this.volume = volume;
        this.pitch = pitch;
        this.position = position;
        this.page = page;
        this.menuSize = menuSize;
    }

    private static void saveWinFile(String filename) {
        SkyWarsReloaded.get().saveResource(filename, false);
        File sf = new File(SkyWarsReloaded.get().getDataFolder(), filename);
        if (sf.exists()) {
            boolean result = sf.renameTo(new File(SkyWarsReloaded.get().getDataFolder(), "winsounds.yml"));
            if (!result) {
                SkyWarsReloaded.get().getLogger().info("Failed to rename Winsounds File");
            }
        }
    }

    public static void loadPlayerOptions() {
        playerOptions.clear();
        File soundFile = new File(SkyWarsReloaded.get().getDataFolder(), "winsounds.yml");

        if (!soundFile.exists()) {
            if (SkyWarsReloaded.getNMS().getVersion() < 9) {
                saveWinFile("winsounds18.yml");
            } else if (SkyWarsReloaded.getNMS().getVersion() < 13) {
                saveWinFile("winsounds112.yml");
            } else {
                SkyWarsReloaded.get().saveResource("winsounds.yml", false);
            }
        }

        if (soundFile.exists()) {
            FileConfiguration storage = YamlConfiguration.loadConfiguration(soundFile);

            if (storage.getConfigurationSection("sounds") != null) {
                for (String key : storage.getConfigurationSection("sounds").getKeys(false)) {
                    String sound = storage.getString("sounds." + key + ".sound");
                    String name = storage.getString("sounds." + key + ".displayName");
                    int volume = storage.getInt("sounds." + key + ".volume");
                    int pitch = storage.getInt("sounds." + key + ".pitch");
                    String material = storage.getString("sounds." + key + ".icon");
                    int level = storage.getInt("sounds." + key + ".level");
                    int cost = storage.getInt("sounds." + key + ".cost");
                    boolean isCustom = storage.getBoolean("sounds." + key + ".isCustomSound");
                    int position = storage.getInt("sounds." + key + ".position");
                    int page = storage.getInt("sounds." + key + ".page");
                    int menuSize = storage.getInt("menuSize");

                    Material mat = Material.matchMaterial(material);
                    if (mat != null) {
                        if (!isCustom) {
                            try {
                                @SuppressWarnings("unused") Sound s = Sound.valueOf(sound);
                                playerOptions.add(new WinSoundOption(key, sound, name, level, cost, volume, pitch, mat, false, position, page, menuSize));
                            } catch (IllegalArgumentException e) {
                                SkyWarsReloaded.get().getServer().getLogger().info(sound + " is not a valid sound in winsounds.yml");
                            }
                        } else {
                            playerOptions.add(new WinSoundOption(key, sound, name, level, cost, volume, pitch, mat, true, position, page, menuSize));
                        }

                    } else {
                        SkyWarsReloaded.get().getServer().getLogger().info(material + " is not a valid Material in winsounds.yml");
                    }
                }
            }
        }
        Collections.sort(playerOptions);
        if (playerOptions.size()>=4 && playerOptions.get(3) != null && playerOptions.get(3).getPosition() == 0 || playerOptions.get(3).getPage() == 0) {
            FileConfiguration storage = YamlConfiguration.loadConfiguration(soundFile);
            updateFile(soundFile, storage);
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
            storage.set("sounds." + playerOptions.get(i).getKey() + ".position", playerOptions.get(i).getPosition());
            storage.set("sounds." + playerOptions.get(i).getKey() + ".page", playerOptions.get(i).getPage());
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

    public String getSound() {
        return sound;
    }

    public void playSound(Location loc) {
        if (SkyWarsReloaded.get().getConfig().getBoolean("soundsEnabled", true)) {
            SkyWarsReloaded.getNMS().playGameSound(loc, sound, volume, pitch, customSound);
        }
    }

    @Override
    public String getPermission() {
        return ("sw.winsound." + key);
    }

    @Override
    public String getMenuName() {
        return "menu.usewin-menu-title";
    }

    @Override
    public String getPurchaseMessage() {
        return new Messaging.MessageFormatter().setVariable("cost", "" + cost)
                .setVariable("item", name).format("menu.purchase-winsound");
    }

    @Override
    public String getUseMessage() {
        return new Messaging.MessageFormatter().setVariable("sound", name).format("menu.usewin-playermsg");
    }

    @Override
    public void setEffect(PlayerStat stat) {
        stat.setWinSound(key);
    }

    @Override
    public String getUseLore() {
        return "menu.usewin-setsound";
    }
}