package net.gcnt.skywarsreloaded.unlockable.cage;

import net.gcnt.skywarsreloaded.SkyWarsReloaded;
import net.gcnt.skywarsreloaded.data.player.stats.UnlockableType;
import net.gcnt.skywarsreloaded.unlockable.CoreUnlockable;

import java.io.File;
import java.io.FileNotFoundException;

public class SchematicCage extends CoreUnlockable implements Cage {

    private final File file;

    public SchematicCage(SkyWarsReloaded plugin, String id, String fileLocation) throws FileNotFoundException {
        super(id);
        this.file = new File(new File(plugin.getDataFolder(), "cages"), fileLocation);

        if (!file.exists()) {
            throw new FileNotFoundException("Cage file " + file.getName() + " does not exist!");
        }
    }

    @Override
    public UnlockableType getType() {
        return UnlockableType.CAGE;
    }

    public File getFile() {
        return file;
    }

    @Override
    public String getPermissionPrefix() {
        return "sw.kit.";
    }

    @Override
    public String getCageType() {
        return "schematic";
    }
}
