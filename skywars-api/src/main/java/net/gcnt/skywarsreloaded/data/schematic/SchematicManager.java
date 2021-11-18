package net.gcnt.skywarsreloaded.data.schematic;

import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.extent.clipboard.Clipboard;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldedit.world.World;
import net.gcnt.skywarsreloaded.game.GameWorld;
import org.jetbrains.annotations.Nullable;

import java.io.File;

public interface SchematicManager {

    @Nullable
    Clipboard getSchematic(File subFolderName, String schemName);

    @Nullable
    EditSession pasteSchematic(Clipboard schem, World world, BlockVector3 location, boolean ignoreAir);

    void undoSchematicPaste(EditSession session);

    boolean saveGameWorldToSchematic(GameWorld gameWorld, World world);

}
