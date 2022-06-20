package net.gcnt.skywarsreloaded.game.cages;

import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.extent.clipboard.Clipboard;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldedit.world.World;
import net.gcnt.skywarsreloaded.SkyWarsReloaded;
import net.gcnt.skywarsreloaded.data.schematic.SchematicManager;
import net.gcnt.skywarsreloaded.game.TeamCage;
import net.gcnt.skywarsreloaded.game.TeamSpawn;
import net.gcnt.skywarsreloaded.unlockable.cage.Cage;
import net.gcnt.skywarsreloaded.unlockable.cage.SchematicCage;
import net.gcnt.skywarsreloaded.utils.CoreSWCCompletableFuture;
import net.gcnt.skywarsreloaded.utils.SWCompletableFuture;
import net.gcnt.skywarsreloaded.utils.SWCoord;

public class CoreSchematicTeamCage implements TeamCage {

    private final SkyWarsReloaded plugin;
    private final TeamSpawn spawn;
    private final SchematicCage cage;

    private EditSession editSession;
    private boolean placed;

    public CoreSchematicTeamCage(SkyWarsReloaded mainIn, TeamSpawn spawn, SchematicCage cage) {
        this.plugin = mainIn;
        this.spawn = spawn;
        this.placed = false;
        this.cage = cage;
    }

    @Override
    public SWCompletableFuture<Boolean> placeCage() {

        SWCompletableFuture<Boolean> future = new CoreSWCCompletableFuture<>(plugin);
        plugin.getScheduler().runSync(() -> future.complete(placeCageNow()));

        return future;
    }

    public Cage getCage() {
        return cage;
    }

    public boolean placeCageNow() {
        // Paste schematic
        SchematicManager schemManager = plugin.getSchematicManager();

        Clipboard clipboard = schemManager.getSchematic(cage.getFile());

        if (clipboard == null) {
            return false;
        }

        final SWCoord loc = spawn.getLocation();
        final World bukkitWorld = plugin.getUtils().getWorldEditWorld(spawn.getTeam().getGameWorld().getWorldName());
        final BlockVector3 locationVec = BlockVector3.at(loc.x(), loc.y(), loc.z());

        this.editSession = schemManager.pasteSchematic(clipboard, bukkitWorld, locationVec, false);
        setPlaced(true);
        return true;
    }

    @Override
    public SWCompletableFuture<Boolean> removeCage() {

        SWCompletableFuture<Boolean> future = new CoreSWCCompletableFuture<>(plugin);
        plugin.getScheduler().runSync(() -> future.complete(removeCageNow()));

        return future;
    }

    public boolean removeCageNow() {
        if (editSession == null) return false;

        SchematicManager schemManager = plugin.getSchematicManager();
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

    @Override
    public TeamSpawn getSpawn() {
        return spawn;
    }
}
