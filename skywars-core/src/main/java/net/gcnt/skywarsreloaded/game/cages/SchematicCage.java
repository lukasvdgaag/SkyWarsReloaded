package net.gcnt.skywarsreloaded.game.cages;

import net.gcnt.skywarsreloaded.SkyWarsReloaded;
import net.gcnt.skywarsreloaded.data.CoreUnlockable;

import java.io.File;
import java.io.FileNotFoundException;

public class SchematicCage extends CoreUnlockable implements Cage {

    private final String id;
    private final File file;

    public SchematicCage(SkyWarsReloaded plugin, String id, String fileLocation) throws FileNotFoundException {
        this.id = id;
        this.file = new File(new File(plugin.getDataFolder(), "cages"), fileLocation);

        if (!file.exists()) {
            throw new FileNotFoundException("Cage file " + file.getName() + " does not exist!");
        }
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
