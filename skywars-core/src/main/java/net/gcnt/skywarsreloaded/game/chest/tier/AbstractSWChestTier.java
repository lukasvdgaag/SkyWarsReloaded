package net.gcnt.skywarsreloaded.game.chest.tier;

import net.gcnt.skywarsreloaded.SkyWarsReloaded;
import net.gcnt.skywarsreloaded.data.config.YAMLConfig;
import net.gcnt.skywarsreloaded.game.chest.SWChestTier;
import net.gcnt.skywarsreloaded.game.types.ChestType;
import net.gcnt.skywarsreloaded.utils.Item;
import net.gcnt.skywarsreloaded.utils.properties.ChestTierProperties;
import net.gcnt.skywarsreloaded.utils.properties.ConfigProperties;
import net.gcnt.skywarsreloaded.utils.properties.FolderProperties;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

public abstract class AbstractSWChestTier implements SWChestTier {

    protected final SkyWarsReloaded plugin;
    protected final String name;

    protected String displayName;

    public AbstractSWChestTier(SkyWarsReloaded plugin, String name) {
        this.plugin = plugin;
        this.name = name;
        this.displayName = name;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getDisplayName() {
        return displayName;
    }
}
