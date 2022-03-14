package net.gcnt.skywarsreloaded.game.kits;

import net.gcnt.skywarsreloaded.SkyWarsReloaded;
import net.gcnt.skywarsreloaded.data.CoreUnlockable;
import net.gcnt.skywarsreloaded.data.config.YAMLConfig;
import net.gcnt.skywarsreloaded.data.player.PlayerStat;
import net.gcnt.skywarsreloaded.utils.Item;
import net.gcnt.skywarsreloaded.utils.properties.FolderProperties;
import net.gcnt.skywarsreloaded.utils.properties.KitProperties;
import net.gcnt.skywarsreloaded.wrapper.player.SWPlayer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public abstract class AbstractSWKit extends CoreUnlockable implements SWKit {

    public final SkyWarsReloaded plugin;
    private final String id;
    private final String permission;
    private final YAMLConfig config;

    private String displayName;
    private String description;
    private Item icon;
    private Item unavailableIcon;
    private List<String> lore;
    private int slot;

    private Item helmet;
    private Item chestplate;
    private Item leggings;
    private Item boots;
    private Item offHand;

    private HashMap<Integer, Item> inventoryContents;
    private List<String> effects;

    public AbstractSWKit(SkyWarsReloaded plugin, String id) {
        super();
        this.plugin = plugin;
        this.id = id;
        this.permission = "sw.kit." + id;
        this.displayName = id;
        this.description = "Kit " + id;
        this.inventoryContents = new HashMap<>();
        this.effects = new ArrayList<>();
        this.lore = new ArrayList<>();
        this.config = plugin.getYAMLManager().loadConfig("kit-" + id, FolderProperties.KITS_FOLDER.toString(), id + ".yml", "/kits/default.yml");
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public String getDisplayName() {
        return displayName;
    }

    @Override
    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    @Override
    public Item getIcon() {
        return icon;
    }

    @Override
    public void setIcon(Item icon) {
        this.icon = icon;
    }

    @Override
    public Item getUnavailableIcon() {
        return unavailableIcon;
    }

    @Override
    public void setUnavailableIcon(Item unavailableIcon) {
        this.unavailableIcon = unavailableIcon;
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public List<String> getLore() {
        return lore;
    }

    @Override
    public void setLore(List<String> lore) {
        this.lore = lore;
    }

    @Override
    public String getPermission() {
        return permission;
    }

    @Override
    public Item getHelmet() {
        return helmet;
    }

    @Override
    public void setHelmet(Item helmet) {
        this.helmet = helmet;
    }

    @Override
    public Item getChestplate() {
        return chestplate;
    }

    @Override
    public void setChestplate(Item chestplate) {
        this.chestplate = chestplate;
    }

    @Override
    public Item getLeggings() {
        return leggings;
    }

    @Override
    public void setLeggings(Item leggings) {
        this.leggings = leggings;
    }

    @Override
    public Item getBoots() {
        return boots;
    }

    @Override
    public void setBoots(Item boots) {
        this.boots = boots;
    }

    @Override
    public HashMap<Integer, Item> getContents() {
        return inventoryContents;
    }

    @Override
    public void setContents(HashMap<Integer, Item> inventoryContents) {
        this.inventoryContents = inventoryContents;
    }

    @Override
    public Item getOffHand() {
        return offHand;
    }

    @Override
    public void setOffHand(Item item) {
        this.offHand = item;
    }

    @Override
    public List<String> getEffects() {
        return effects;
    }

    @Override
    public void setEffects(List<String> effects) {
        this.effects = effects;
    }

    @Override
    public int getSlot() {
        return slot;
    }

    @Override
    public void setSlot(int slot) {
        this.slot = slot;
    }

    @Override
    public synchronized void loadData() {
        // basic kit info init
        this.displayName = config.getString(KitProperties.DISPLAY_NAME.toString(), id);
        // todo load default item when not in config.
        this.icon = config.getItem(KitProperties.ICON.toString());
        this.unavailableIcon = config.getItem(KitProperties.UNAVAILABLE_ICON.toString());
        this.description = config.getString(KitProperties.DESCRIPTION.toString(), "SkyWarsReloaded Kit");
        this.lore = config.getStringList(KitProperties.LORE.toString());
        this.effects = config.getStringList(KitProperties.EFFECTS.toString());
        this.slot = config.getInt(KitProperties.SLOT.toString(), -1);

        // kit requirement init
        setNeedPermission(config.getBoolean(KitProperties.REQUIREMENTS_PERMISSION.toString(), false));
        setCost(config.getInt(KitProperties.REQUIREMENTS_COST.toString(), 0));
        if (config.contains(KitProperties.REQUIREMENTS_STATS.toString())) {
            config.getKeys(KitProperties.REQUIREMENTS_STATS.toString()).forEach(stat -> {
                try {
                    addMinimumStat(PlayerStat.fromString(stat), config.getInt(KitProperties.REQUIREMENTS_STATS + "." + stat));
                } catch (Exception e) {
                    plugin.getLogger().error(String.format("Failed to load %s stat requirement for kit %s. Ignoring it. (%s)", stat, id, e.getClass().getName() + ": " + e.getLocalizedMessage()));
                }
            });
        }

        // inventory content init
        inventoryContents = new HashMap<>();
        if (!config.contains(KitProperties.CONTENTS.toString())) return;
        this.offHand = config.getItem(KitProperties.OFF_HAND.toString());
        if (config.isSet(KitProperties.ARMOR_CONTENTS.toString())) {
            // armor init
            try {
                this.helmet = config.getItem(KitProperties.HELMET.toString());
            } catch (Exception e) {
                plugin.getLogger().error(String.format("Failed to load helmet for kit %s. Ignoring it. (%s)", id, e.getClass().getName() + ": " + e.getLocalizedMessage()));
            }
            try {
                this.chestplate = config.getItem(KitProperties.CHESTPLATE.toString());
            } catch (Exception e) {
                plugin.getLogger().error(String.format("Failed to load chestplate for kit %s. Ignoring it. (%s)", id, e.getClass().getName() + ": " + e.getLocalizedMessage()));
            }
            try {
                this.leggings = config.getItem(KitProperties.LEGGINGS.toString());
            } catch (Exception e) {
                plugin.getLogger().error(String.format("Failed to load leggings for kit %s. Ignoring it. (%s)", id, e.getClass().getName() + ": " + e.getLocalizedMessage()));
            }
            try {
                this.boots = config.getItem(KitProperties.BOOTS.toString());
            } catch (Exception e) {
                plugin.getLogger().error(String.format("Failed to load boots for kit %s. Ignoring it. (%s)", id, e.getClass().getName() + ": " + e.getLocalizedMessage()));
            }
        }

        String invContentsKey = KitProperties.INVENTORY_CONTENTS.toString();
        if (config.isSet(invContentsKey)) {
            config.getKeys(invContentsKey).forEach(slot1 -> {
                try {
                    int number = Integer.parseInt(slot1);
                    this.inventoryContents.put(number, config.getItem(KitProperties.INVENTORY_CONTENTS + "." + number));
                } catch (Exception e) {
                    plugin.getLogger().error(String.format("Failed to load slot '%s' for kit %s. Ignoring it. (%s)", slot1, id, e.getClass().getName() + ": " + e.getLocalizedMessage()));
                }
            });
        }

    }

    @Override
    public void saveData() {
        config.set(KitProperties.DISPLAY_NAME.toString(), displayName);
        config.set(KitProperties.ICON.toString(), icon);
        config.set(KitProperties.UNAVAILABLE_ICON.toString(), unavailableIcon);
        config.set(KitProperties.DESCRIPTION.toString(), description);
        config.set(KitProperties.LORE.toString(), lore);
        config.set(KitProperties.EFFECTS.toString(), effects);
        config.set(KitProperties.SLOT.toString(), slot);

        config.set(KitProperties.REQUIREMENTS_PERMISSION.toString(), needsPermission());
        config.set(KitProperties.REQUIREMENTS_COST.toString(), getCost());

        // clearing required stats from current file
        config.set(KitProperties.REQUIREMENTS_STATS.toString(), null);
        getMinimumStats().forEach(
                (stat, value) -> config.set(KitProperties.REQUIREMENTS_STATS + "." + stat.getProperty(), value)
        );

        config.set(KitProperties.HELMET.toString(), helmet);
        config.set(KitProperties.CHESTPLATE.toString(), chestplate);
        config.set(KitProperties.LEGGINGS.toString(), leggings);
        config.set(KitProperties.BOOTS.toString(), boots);
        config.set(KitProperties.OFF_HAND.toString(), offHand);

        // clearing contents of inventory from file.
        config.set(KitProperties.INVENTORY_CONTENTS.toString(), null);
        getContents().forEach(
                (slot, item) -> config.set(KitProperties.INVENTORY_CONTENTS + "." + slot, item)
        );

        config.save();
    }

    @Override
    public abstract void giveToPlayer(SWPlayer sp);
}
