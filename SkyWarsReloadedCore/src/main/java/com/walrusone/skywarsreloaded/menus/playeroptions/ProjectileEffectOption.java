package com.walrusone.skywarsreloaded.menus.playeroptions;

import com.walrusone.skywarsreloaded.SkyWarsReloaded;
import com.walrusone.skywarsreloaded.managers.PlayerStat;
import com.walrusone.skywarsreloaded.menus.playeroptions.objects.ParticleEffect;
import com.walrusone.skywarsreloaded.utilities.Messaging;
import com.walrusone.skywarsreloaded.utilities.Util;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class ProjectileEffectOption extends PlayerOption {

    private static ArrayList<PlayerOption> playerOptions = new ArrayList<>();
    private List<ParticleEffect> effects;

    private ProjectileEffectOption(String key, List<ParticleEffect> effects, String name, Material material, int level, int cost, int position, int page, int menuSize) {
        this.key = key;
        this.level = level;
        this.cost = cost;
        this.effects = effects;
        this.name = name;
        this.item = new ItemStack(material, 1);
        this.position = position;
        this.page = page;
        this.menuSize = menuSize;
    }

    public static void loadPlayerOptions() {
        playerOptions.clear();
        File particleFile = new File(SkyWarsReloaded.get().getDataFolder(), "projectileeffects.yml");

        if (!particleFile.exists()) {
            SkyWarsReloaded.get().saveResource("projectileeffects.yml", false);
        }

        if (particleFile.exists()) {
            FileConfiguration storage = YamlConfiguration.loadConfiguration(particleFile);

            if (storage.getConfigurationSection("effects") != null) {
                for (String key : storage.getConfigurationSection("effects").getKeys(false)) {
                    String name = storage.getString("effects." + key + ".displayname");
                    String material = storage.getString("effects." + key + ".icon");
                    int level = storage.getInt("effects." + key + ".level");
                    int cost = storage.getInt("effects." + key + ".cost");
                    List<String> particles = storage.getStringList("effects." + key + ".particles");
                    int position = storage.getInt("effects." + key + ".position");
                    int page = storage.getInt("effects." + key + ".page");
                    int menuSize = storage.getInt("menuSize");

                    List<ParticleEffect> effects = new ArrayList<>();
                    if (particles != null) {
                        for (String part : particles) {
                            final String[] parts = part.split(":");
                            if (parts.length == 6
                                    && SkyWarsReloaded.getNMS().isValueParticle(parts[0].toUpperCase())
                                    && Util.get().isFloat(parts[1])
                                    && Util.get().isFloat(parts[2])
                                    && Util.get().isFloat(parts[3])
                                    && Util.get().isInteger(parts[4])
                                    && Util.get().isInteger(parts[5])) {
                                effects.add(new ParticleEffect(parts[0].toUpperCase(), Float.valueOf(parts[1]), Float.valueOf(parts[2]), Float.valueOf(parts[3]), Integer.parseInt(parts[4]), Integer.parseInt(parts[5])));
                            } else {
                                SkyWarsReloaded.get().getLogger().info("The particle effect " + key + " has an invalid particle effect");
                            }
                        }
                    }

                    Material mat = Material.matchMaterial(material);
                    if (mat != null) {
                        playerOptions.add(new ProjectileEffectOption(key, effects, name, mat, level, cost, position, page, menuSize));
                    }
                }
            }
        }

        Collections.sort(playerOptions);
        if (playerOptions.size()>=4 && playerOptions.get(3) != null && playerOptions.get(3).getPosition() == 0 || playerOptions.get(3).getPage() == 0) {
            FileConfiguration storage = YamlConfiguration.loadConfiguration(particleFile);
            updateFile(particleFile, storage);
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
            storage.set("effects." + playerOptions.get(i).getKey() + ".position", playerOptions.get(i).getPosition());
            storage.set("effects." + playerOptions.get(i).getKey() + ".page", playerOptions.get(i).getPage());
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

    public List<ParticleEffect> getEffects() {
        return effects;
    }

    @Override
    public String getPermission() {
        return ("sw.proeffect." + key);
    }

    @Override
    public String getMenuName() {
        return "menu.useprojeffect-menu-title";
    }

    @Override
    public String getPurchaseMessage() {
        return new Messaging.MessageFormatter().setVariable("cost", "" + cost)
                .setVariable("item", name).format("menu.purchase-projeffect");
    }

    @Override
    public String getUseMessage() {
        return new Messaging.MessageFormatter().setVariable("effect", name).format("menu.useeffect-playermsg");
    }

    @Override
    public void setEffect(PlayerStat stat) {
        stat.setProjectileEffect(key);
    }

    @Override
    public String getUseLore() {
        return "menu.useprojeffect-seteffect";
    }
}