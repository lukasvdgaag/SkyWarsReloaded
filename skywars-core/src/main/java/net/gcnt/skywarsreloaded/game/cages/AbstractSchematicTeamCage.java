package net.gcnt.skywarsreloaded.game.cages;

import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.bukkit.BukkitWorld;
import com.sk89q.worldedit.extent.clipboard.Clipboard;
import com.sk89q.worldedit.math.BlockVector3;
import net.gcnt.skywarsreloaded.AbstractSkyWarsReloaded;
import net.gcnt.skywarsreloaded.data.schematic.SchematicManager;
import net.gcnt.skywarsreloaded.game.TeamCage;
import net.gcnt.skywarsreloaded.game.TeamSpawn;
import net.gcnt.skywarsreloaded.utils.SWCompletableFuture;
import net.gcnt.skywarsreloaded.utils.SWCoord;

import java.io.File;

public abstract class AbstractSchematicTeamCage implements TeamCage {

    private final AbstractSkyWarsReloaded main;
    private final TeamSpawn spawn;

    private EditSession editSession;
    private boolean placed;

    public AbstractSchematicTeamCage(AbstractSkyWarsReloaded mainIn, TeamSpawn spawn) {
        this.main = mainIn;
        this.spawn = spawn;
        this.placed = false;
    }

    @Override
    public TeamSpawn getSpawn() {
        return this.spawn;
    }

    @Override
    public abstract SWCompletableFuture<Boolean> placeCage(String cageId);

    public boolean placeCageNow(String cageId) {
        // In the case that the cage already exists, remove the old one
        removeCage(cageId);

        // Paste schematic
        SchematicManager schemManager = main.getSchematicManager();
        File dataFolder = main.getDataFolder();
        File schemsFolder = new File(dataFolder, "cages");

        Clipboard clipboard = schemManager.getSchematic(schemsFolder, cageId + ".schem"); // todo get random file fro

        if (clipboard == null) {
            return false;
        }

        final SWCoord loc = spawn.getLocation();
        final BukkitWorld bukkitWorld = this.getWorldEditWorldByName(spawn.getTeam().getGameWorld().getWorldName());
        final BlockVector3 locationVec = BlockVector3.at(loc.x(), loc.y(), loc.z());

        this.editSession = schemManager.pasteSchematic(clipboard, bukkitWorld, locationVec, false);
        setPlaced(true);
        return true;
    }

    @Override
    public abstract SWCompletableFuture<Boolean> removeCage(String cage);

    public boolean removeCageNow() {
        if (editSession == null) return false;

        SchematicManager schemManager = main.getSchematicManager();
        schemManager.undoSchematicPaste(editSession);
        setPlaced(false);

        this.editSession = null;
        return true;
    }

    @Override
    public boolean isPlaced() {
        return placed;
    }

    @Override
    public void setPlaced(boolean placed) {
        this.placed = placed;
    }

    // Utils

    public abstract BukkitWorld getWorldEditWorldByName(String worldName);

}
