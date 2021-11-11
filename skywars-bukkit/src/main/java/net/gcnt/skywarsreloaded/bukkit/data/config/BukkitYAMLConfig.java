package net.gcnt.skywarsreloaded.bukkit.data.config;

import net.gcnt.skywarsreloaded.AbstractSkyWarsReloaded;
import net.gcnt.skywarsreloaded.bukkit.utils.BukkitItem;
import net.gcnt.skywarsreloaded.data.config.AbstractYAMLConfig;
import net.gcnt.skywarsreloaded.utils.Item;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.jetbrains.annotations.Nullable;

import java.io.*;
import java.util.List;
import java.util.Set;

public class BukkitYAMLConfig extends AbstractYAMLConfig {

    private final Object reloadLock = new Object();
    private FileConfiguration fileConfiguration;

    public BukkitYAMLConfig(AbstractSkyWarsReloaded skyWars, String id, @Nullable String directory, String fileName) {
        super(skyWars, id, directory, fileName);

        this.onSetup(this.getFile());
    }

    public BukkitYAMLConfig(AbstractSkyWarsReloaded skyWars, String id, File directory, String fileName) {
        super(skyWars, id, directory, fileName);

        this.onSetup(this.getFile());
    }

    public BukkitYAMLConfig(AbstractSkyWarsReloaded skyWars, String id, @Nullable String directory, String fileName, String defaultFile) {
        super(skyWars, id, directory, fileName, defaultFile);

        this.onSetup(this.getFile());
    }

    public BukkitYAMLConfig(AbstractSkyWarsReloaded skyWars, String id, File directory, String fileName, String defaultFile) {
        super(skyWars, id, directory, fileName, defaultFile);

        this.onSetup(this.getFile());
    }

    protected boolean onSetup(File configFile) {
        synchronized (reloadLock) {
            this.fileConfiguration = YamlConfiguration.loadConfiguration(configFile);
            return true;
        }
    }

    protected boolean onDisable(boolean save) {
        if (!save) return true;
        try {
            synchronized (reloadLock) {
                this.fileConfiguration.save(this.getFile());
            }
        } catch (IOException ex) {
            ex.printStackTrace();
            return false;
        }
        return true;
    }

    public FileConfiguration getFileConfiguration() {
        return fileConfiguration;
    }

    @Override
    public String getString(String property) {
        return getString(property, "");
    }

    @Override
    public String getString(String property, String defaultValue) {
        return fileConfiguration.getString(property, defaultValue);
    }

    @Override
    public int getInt(String property) {
        return getInt(property, 0);
    }

    @Override
    public int getInt(String property, int defaultValue) {
        return fileConfiguration.getInt(property, defaultValue);
    }

    @Override
    public double getDouble(String property) {
        return getDouble(property, 0);
    }

    @Override
    public double getDouble(String property, double defaultValue) {
        return fileConfiguration.getDouble(property, defaultValue);
    }

    @Override
    public List<String> getStringList(String property) {
        return fileConfiguration.getStringList(property);
    }

    @Override
    public boolean getBoolean(String property) {
        return getBoolean(property, false);
    }

    @Override
    public boolean getBoolean(String property, boolean defaultValue) {
        return fileConfiguration.getBoolean(property, defaultValue);
    }

    @Override
    public Object get(String property) {
        return get(property, null);
    }

    @Override
    public Object get(String property, Object defaultValue) {
        return fileConfiguration.get(property, defaultValue);
    }

    @Override
    public void set(String property, Object value) {
        if (value instanceof BukkitItem item) {
            fileConfiguration.set(property + ".material", item.getMaterial());
            if (item.getAmount() != 1) fileConfiguration.set(property + ".amount", item.getAmount());
            if (item.getDamage() != 0) fileConfiguration.set(property + ".damage", item.getDamage());
            if (item.getDurability() != -1) fileConfiguration.set(property + ".durability", item.getDurability());
            if (item.getDisplayName() != null) fileConfiguration.set(property + ".display-name", item.getDisplayName());
            if (!item.getLore().isEmpty()) fileConfiguration.set(property + ".lore", item.getLore());
            if (!item.getEnchantments().isEmpty())
                fileConfiguration.set(property + ".enchantments", item.getEnchantments());
            if (!item.getItemFlags().isEmpty()) fileConfiguration.set(property + ".item-flags", item.getItemFlags());
        } else {
            fileConfiguration.set(property, value);
        }
    }

    @Override
    public boolean isset(String property) {
        return fileConfiguration.isSet(property);
    }

    @Override
    public boolean contains(String property) {
        return fileConfiguration.contains(property);
    }

    @Override
    public Set<String> getKeys(String property) {
        return fileConfiguration.getConfigurationSection(property).getKeys(false);
    }

    @Override
    public Item getItem(String category) {
        if (!contains(category)) return null;
        ConfigurationSection section = fileConfiguration.getConfigurationSection(category);
        if (section == null || !section.isSet("material")) return null;

        try {
            BukkitItem item = new BukkitItem(plugin, section.getString("material"));
            item.setAmount(section.getInt("amount", 0));
            item.setEnchantments(section.getStringList("enchantments"));
            item.setDisplayName(section.getString("display-name", null));
            item.setLore(section.getStringList("lore"));
            item.setItemFlags(section.getStringList("item-flags"));
            try {
                item.setDamage(Byte.parseByte(section.getString("damage")));
            } catch (Exception ignored) {
            }
            try {
                item.setDurability(Short.parseShort(section.getString("durability")));
            } catch (Exception ignored) {
            }
            return item;
        } catch (Exception e) {
            plugin.getLogger().severe(String.format("Failed to load item with material %s. Ignoring it. (%s)", section.getString("material"), e.getClass().getName() + ": " + e.getLocalizedMessage()));
        }

        return null;
    }

    @Override
    public void save() {
        try {
            fileConfiguration.save(getFile());
        } catch (IOException e) {
            plugin.getLogger().severe("SkyWarsReloaded failed to save the YAML file called '" + getFileName() + "'.");
        }
    }
}
