package net.gcnt.skywarsreloaded.game.cages;

import net.gcnt.skywarsreloaded.SkyWarsReloaded;
import net.gcnt.skywarsreloaded.data.CoreUnlockable;

import java.io.File;

public class SchematicCage extends CoreUnlockable implements Cage {

    private final String id;
    private final File file;

    public SchematicCage(SkyWarsReloaded plugin, String id, String fileLocation) {
        this.id = id;
        this.file = new File(new File(plugin.getDataFolder(), "cages"), fileLocation);
    }

    @Override
    public String getId() {
        return this.id;
    }

    public File getFile() {
        return file;
    }

    @Override
    public String getPermissionPrefix() {
        return "sw.kit.";
    }

    @Override
    public String getType() {
        return "schematic";
    }
}
