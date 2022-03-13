package net.gcnt.skywarsreloaded.bukkit.data.config;

import net.gcnt.skywarsreloaded.AbstractSkyWarsReloaded;
import net.gcnt.skywarsreloaded.bukkit.utils.BukkitItem;
import net.gcnt.skywarsreloaded.data.config.AbstractYAMLConfig;
import net.gcnt.skywarsreloaded.utils.*;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.jetbrains.annotations.Nullable;

import java.awt.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class BukkitYAMLConfig extends AbstractYAMLConfig {

    private final Object reloadLock = new Object();
    private FileConfiguration fileConfiguration;
    private FileConfiguration defaultFileConfiguration;

    public BukkitYAMLConfig(AbstractSkyWarsReloaded skyWars, String id, File directory, String fileName) {
        super(skyWars, id, directory, fileName);

        this.onSetup();
    }

    public BukkitYAMLConfig(AbstractSkyWarsReloaded skyWars, String id, @Nullable String directory, String fileName) {
        super(skyWars, id, directory, fileName);

        this.onSetup();
    }

    public BukkitYAMLConfig(AbstractSkyWarsReloaded skyWars, String id, File directory, String fileName, String defaultFile) {
        super(skyWars, id, directory, fileName, defaultFile);

        this.onSetup();
    }

    public BukkitYAMLConfig(AbstractSkyWarsReloaded skyWars, String id, @Nullable String directory, String fileName, String defaultFile) {
        super(skyWars, id, directory, fileName, defaultFile);

        this.onSetup();
    }

    protected boolean onSetup() {
        synchronized (reloadLock) {
            this.fileConfiguration = YamlConfiguration.loadConfiguration(this.getFile());
            BufferedReader defaultFileReader = this.getDefaultFileReader();
            this.defaultFileConfiguration = YamlConfiguration.loadConfiguration(defaultFileReader);
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
        return getString(property, this.defaultFileConfiguration.getString(property, ""));
    }

    @Override
    public String getString(String property, String defaultValue) {
        return fileConfiguration.getString(property, defaultValue);
    }

    @Override
    public int getInt(String property) {
        return getInt(property, this.defaultFileConfiguration.getInt(property, 0));
    }

    @Override
    public int getInt(String property, int defaultValue) {
        return fileConfiguration.getInt(property, defaultValue);
    }

    @Override
    public double getDouble(String property) {
        return getDouble(property, this.defaultFileConfiguration.getDouble(property, 0));
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
    public Map<String, String> getStringMap(String property) {
        ConfigurationSection section = this.fileConfiguration.getConfigurationSection(property);
        if (section == null) return new HashMap<>();
        HashMap<String, String> convertedMap = new HashMap<>();
        for (Map.Entry<String, Object> entry : section.getValues(false).entrySet()) {
            convertedMap.put(entry.getKey(), entry.getValue().toString());
        }
        return convertedMap;
    }

    @Override
    public boolean getBoolean(String property) {
        return getBoolean(property, this.defaultFileConfiguration.getBoolean(property, false));
    }

    @Override
    public boolean getBoolean(String property, boolean defaultValue) {
        return fileConfiguration.getBoolean(property, defaultValue);
    }

    @Override
    public Object get(String property) {
        return get(property, this.defaultFileConfiguration.get(property, null));
    }

    @Override
    public Object get(String property, Object defaultValue) {
        return fileConfiguration.get(property, defaultValue);
    }

    @Override
    public void set(String property, Object value) {
        if (value instanceof BukkitItem) {
            BukkitItem item = (BukkitItem) value;
            fileConfiguration.set(property + ".material", item.getMaterial());
            if (item.getAmount() != 1) fileConfiguration.set(property + ".amount", item.getAmount());
            if (item.getDamage() != 0) fileConfiguration.set(property + ".damage", item.getDamage());
            if (item.getDurability() != -1) fileConfiguration.set(property + ".durability", item.getDurability());
            if (item.getDisplayName() != null) fileConfiguration.set(property + ".display-name", item.getDisplayName());
            if (!item.getLore().isEmpty()) fileConfiguration.set(property + ".lore", item.getLore());
            if (!item.getEnchantments().isEmpty())
                fileConfiguration.set(property + ".enchantments", item.getEnchantments());
            if (!item.getItemFlags().isEmpty()) fileConfiguration.set(property + ".item-flags", item.getItemFlags());
            if (item.getSkullOwner() != null) fileConfiguration.set(property + ".owner", item.getSkullOwner());
            if (item.getColor() != null) fileConfiguration.set(property + ".color", item.getColor().getRGB());
        } else {
            fileConfiguration.set(property, value);
        }
    }

    @Override
    public boolean isSet(String property) {
        return fileConfiguration.isSet(property);
    }

    @Override
    public boolean contains(String property) {
        return fileConfiguration.contains(property);
    }

    @Override
    public Set<String> getKeys(String property) {
        final ConfigurationSection sect = fileConfiguration.getConfigurationSection(property);
        if (sect == null) return null;
        return sect.getKeys(false);
    }

    @Override
    public Item getItem(String category, Item def) {
        if (!contains(category)) return def;
        ConfigurationSection section = fileConfiguration.getConfigurationSection(category);
        if (section == null || !section.isSet("material")) return def;

        try {
            BukkitItem item = new BukkitItem(plugin, section.getString("material"));
            item.setAmount(section.getInt("amount", 1));
            item.setEnchantments(section.getStringList("enchantments"));
            item.setDisplayName(section.getString("display-name", null));
            item.setLore(section.getStringList("lore"));
            item.setItemFlags(section.getStringList("item-flags"));
            item.setSkullOwner(section.getString("owner", null));

            Object val = section.get("color", null);
            if (val != null) {
                if (val instanceof String) {
                    String str = (String) val;
                    if (str.startsWith("#")) item.setColor(Color.decode(str));
                } else if (val instanceof Integer) {
                    item.setColor(new Color((Integer) val));
                }
            }

            try {
                int rawNumber = section.getInt("damage", 0);
                assert rawNumber >= -128 && rawNumber <= 127;
                byte castedByte = (byte) rawNumber;
                item.setDamage(castedByte);
            } catch (Exception ignored) {
            }
            try {
                int rawNumber = section.getInt("durability", 0);
                assert rawNumber >= -128 && rawNumber <= 127;
                byte castedByte = (byte) rawNumber;
                item.setDurability(castedByte);
            } catch (Exception ignored) {
            }
            return item;
        } catch (Exception e) {
            plugin.getLogger().error(String.format("Failed to load item with material %s. Ignoring it. (%s)",
                    section.getString("material"), e.getClass().getName() + ": " + e.getLocalizedMessage()));
        }

        return def;
    }

    @Override
    public Item getItem(String category) {
        return getItem(category, new BukkitItem(plugin, null));
    }

    @Override
    public SWCoord getCoord(String property, SWCoord def) {
        if (!contains(property)) {

            return null;
        }

        return new CoreSWCoord(plugin, fileConfiguration.getString(property));
    }

    @Override
    public SWCoord getCoord(String property) {
        return getCoord(property, null);
    }

    @Override
    public Message getMessage(String property) {
        Object res = get(property, null);
        if (res == null) {
            res = this.defaultFileConfiguration.get(property, null);
        }

        // Auto report if still null, the default config should catch this
        if (res == null) {
            String msg = "Return value of " + property + " is null";
            SWLogger logger = this.plugin.getLogger();
            logger.error(msg);
            logger.reportException(new NullPointerException("Return value of " + property + " is null"));
            return new CoreMessage(plugin, "<error, please check console and report this>");
        }

        if (res instanceof String) {
            return new CoreMessage(plugin, (String) res);
        } else if (res instanceof List) {
            // Sanity check

            @SuppressWarnings("rawtypes")
            List list = (List) res;
            if (!list.isEmpty() && !(list.get(0) instanceof String)) {
                this.plugin.getLogger().error("Invalid configuration message list for '" + property +
                        "': the type of the first list item is a " + list.get(0).getClass().getTypeName() + ". " +
                        "Please make sure the message is surrounded with quotes.");
            }
            @SuppressWarnings("unchecked")
            List<String> stringList = (List<String>) list;
            return new CoreMessage(plugin, stringList.toArray(new String[0]));
        }
        return null;
    }

    @Override
    public Message getMessage(String property, String def) {
        if (!contains(property)) return new CoreMessage(plugin, def);
        return new CoreMessage(plugin, getString(property));
    }

    @Override
    public Message getMessage(String property, List<String> def) {
        if (!contains(property)) return new CoreMessage(plugin, def.toArray(new String[0]));
        return new CoreMessage(plugin, getStringList(property).toArray(new String[0]));
    }

    @Override
    public void save() {
        try {
            fileConfiguration.save(getFile());
        } catch (IOException e) {
            plugin.getLogger().error("SkyWarsReloaded failed to save the YAML file called '" + getFileName() + "'.");
        }
    }
}
