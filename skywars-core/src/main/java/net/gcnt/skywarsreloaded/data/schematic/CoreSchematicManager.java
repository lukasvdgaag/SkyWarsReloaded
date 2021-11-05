package net.gcnt.skywarsreloaded.data.schematic;

import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.WorldEditException;
import com.sk89q.worldedit.extent.clipboard.Clipboard;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardFormat;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardFormats;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardReader;
import com.sk89q.worldedit.function.operation.Operation;
import com.sk89q.worldedit.function.operation.Operations;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldedit.session.ClipboardHolder;
import com.sk89q.worldedit.world.World;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class CoreSchematicManager implements SchematicManager {

    @Override
    public Clipboard getSchematic(File subFolder, String schemName) {
        File schemFile = new File(subFolder, schemName);

        Clipboard clipboard;
        ClipboardFormat format = ClipboardFormats.findByFile(schemFile);
        ClipboardReader reader = null;

        try {
            reader = format.getReader(new FileInputStream(schemFile));
            clipboard = reader.read();
            reader.close();
        } catch (IOException | NullPointerException ex) {
            ex.printStackTrace();

            try {
                if (reader != null) reader.close();
            } catch (IOException ignored) {
            }

            return null;
        }

        return clipboard;
    }

    @Override
    public EditSession pasteSchematic(Clipboard schem, World world, BlockVector3 location) {
        EditSession editSession = null;

        try {
            editSession = WorldEdit.getInstance().newEditSession(world);

            Operation operation = new ClipboardHolder(schem)
                    .createPaste(editSession)
                    .to(BlockVector3.at(location.getX(), location.getY(), location.getZ()))
                    // configure here
                    .build();

            Operations.complete(operation);
            editSession.close();

            return editSession;

        } catch (WorldEditException ex) {
            ex.printStackTrace();

            editSession.close();

            return null;
        }
    }

    @Override
    public void undoSchematicPaste(EditSession session) {
        session.undo(session);
        session.close();
    }

}
