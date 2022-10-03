package net.gcnt.skywarsreloaded.bukkit.data.config;

import net.gcnt.skywarsreloaded.AbstractSkyWarsReloaded;
import net.gcnt.skywarsreloaded.bukkit.utils.BukkitItem;
import net.gcnt.skywarsreloaded.data.config.AbstractYAMLConfig;
import net.gcnt.skywarsreloaded.data.player.stats.PlayerStat;
import net.gcnt.skywarsreloaded.unlockable.Unlockable;
import net.gcnt.skywarsreloaded.utils.*;
import net.gcnt.skywarsreloaded.utils.properties.KitProperties;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.jetbrains.annotations.Nullable;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.util.*;

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
    public List<?> getList(String property) {
        return getList(property, this.defaultFileConfiguration.getList(property, null));
    }

    @Override
    public List<?> getList(String property, List<?> defaultValue) {
        return fileConfiguration.getList(property, defaultValue);
    }

    @Override
    public List<Map<?, ?>> getMapList(String property) {
        return fileConfiguration.getMapList(property);
    }

    @Override
    public List<Item> getItemList(String property) {
        final List<Map<?, ?>> mapList = getMapList(property);
        List<Item> items = new ArrayList<>();

        mapList.forEach(map -> {
            if (map != null) {
                final Item item = plugin.getItemManager().getItem(map);
                if (item != null) items.add(item);
            }
        });

        return items;
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

        Map<String, Object> map = section.getValues(true);
        final Item item = plugin.getItemManager().getItem(map);

        return item == null ? def : item;
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
    public <E extends Enum<E>> E getEnum(Class<E> enumClass, String property) {
        return getEnum(enumClass, property, null);
    }

    @Override
    public <E extends Enum<E>> E getEnum(Class<E> enumClass, String property, String def) {
        if (enumClass == null || !enumClass.isEnum()) return null;

        String value = getString(property).toUpperCase();

        try {
            return Enum.valueOf(enumClass, value);
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public void loadUnlockableData(Unlockable unlockable, String property) {
        if (!contains(property)) return;
        ConfigurationSection section = fileConfiguration.getConfigurationSection(property);
        if (section == null) return;

        unlockable.setNeedPermission(section.getBoolean(KitProperties.REQUIREMENTS_PERMISSION.toString(), false));
        unlockable.setCost(section.getInt(KitProperties.REQUIREMENTS_COST.toString(), 0));
        if (section.contains(KitProperties.REQUIREMENTS_STATS.toString())) {
            section.getConfigurationSection(KitProperties.REQUIREMENTS_STATS.toString()).getKeys(false).forEach(stat -> {
                try {
                    PlayerStat playerStat = PlayerStat.fromString(stat);
                    if (playerStat == null) {
                        plugin.getLogger().error("Invalid stat '" + stat + "' in unlockable '" + unlockable.getId() + "'");
                        return;
                    }

                    unlockable.addMinimumStat(playerStat, section.getInt(KitProperties.REQUIREMENTS_STATS + "." + stat, 0));
                } catch (Exception e) {
                    plugin.getLogger().error(String.format("Failed to load %s stat requirement for kit %s. Ignoring it. (%s)", stat, unlockable.getId(), e.getClass().getName() + ": " + e.getLocalizedMessage()));
                }
            });
        }
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
