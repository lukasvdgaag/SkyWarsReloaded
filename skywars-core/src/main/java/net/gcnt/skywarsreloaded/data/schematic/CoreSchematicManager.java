package net.gcnt.skywarsreloaded.data.schematic;

import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.WorldEditException;
import com.sk89q.worldedit.extent.clipboard.BlockArrayClipboard;
import com.sk89q.worldedit.extent.clipboard.Clipboard;
import com.sk89q.worldedit.extent.clipboard.io.*;
import com.sk89q.worldedit.function.operation.ForwardExtentCopy;
import com.sk89q.worldedit.function.operation.Operation;
import com.sk89q.worldedit.function.operation.Operations;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldedit.regions.CuboidRegion;
import com.sk89q.worldedit.session.ClipboardHolder;
import com.sk89q.worldedit.world.World;
import net.gcnt.skywarsreloaded.SkyWarsReloaded;
import net.gcnt.skywarsreloaded.game.GameWorld;
import net.gcnt.skywarsreloaded.utils.CoreSWCoord;
import net.gcnt.skywarsreloaded.utils.SWCoord;
import net.gcnt.skywarsreloaded.utils.properties.FolderProperties;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.file.Files;

public class CoreSchematicManager implements SchematicManager {

    private final SkyWarsReloaded plugin;

    public CoreSchematicManager(SkyWarsReloaded plugin) {
        this.plugin = plugin;
    }

    public Clipboard getSchematic(File file) {
        Clipboard clipboard;
        ClipboardFormat format = ClipboardFormats.findByFile(file);
        ClipboardReader reader = null;

        try {
            reader = format.getReader(new FileInputStream(file));
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
    public Clipboard getSchematic(File subFolder, String schemName) {
        return getSchematic(new File(subFolder, schemName));
    }

    @Override
    public EditSession pasteSchematic(Clipboard schem, SWCoord loc, boolean ignoreAir) {
        final World world = plugin.getUtils().getWorldEditWorld(loc.getWorld().getName());
        final BlockVector3 locationVec = BlockVector3.at(loc.x(), loc.y(), loc.z());

        return pasteSchematic(schem, world, locationVec, ignoreAir);
    }

    @Override
    public EditSession pasteSchematic(Clipboard schem, World world, BlockVector3 location, boolean ignoreAir) {
        EditSession editSession = null;

        try {
            editSession = this.getNewEditSession(world);

            Operation operation = new ClipboardHolder(schem)
                    .createPaste(editSession)
                    .to(location)
                    .ignoreAirBlocks(ignoreAir)
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
    public boolean saveGameWorldToSchematic(GameWorld gameWorld, World world) {
        try {
            final int borderSize = gameWorld.getTemplate().getBorderRadius();
            SWCoord lowest = new CoreSWCoord(-borderSize, 0, -borderSize);
            SWCoord highest = new CoreSWCoord(borderSize, plugin.getUtils().getBuildLimit(), borderSize);

            CuboidRegion reg = new CuboidRegion(world, BlockVector3.at(lowest.x(), lowest.y(), lowest.z()), BlockVector3.at(highest.x(), highest.y(), highest.z()));
            BlockArrayClipboard clipboard = new BlockArrayClipboard(reg);
            clipboard.setOrigin(BlockVector3.at(0, 0, 0));

            EditSession session = this.getNewEditSession(world);
            if (session == null) return false;
            ForwardExtentCopy forwardExtentCopy = new ForwardExtentCopy(session, reg, clipboard, reg.getMinimumPoint());
            Operations.complete(forwardExtentCopy);
            File folder = new File(plugin.getDataFolder(), FolderProperties.WORLD_SCHEMATICS_FOLDER.toString());
            if (!folder.exists()) {
                folder.mkdir();
            }

            File file = new File(folder, gameWorld.getTemplate().getName() + ".schem");
            Files.deleteIfExists(file.toPath());
            try (ClipboardWriter writer = BuiltInClipboardFormat.SPONGE_SCHEMATIC.getWriter(new FileOutputStream(file))) {
                writer.write(clipboard);
            }

            session.close();
            return true;
        } catch (Exception e) {
            plugin.getLogger().error(String.format("Failed to save schematic of world %s to file system. (%s)", gameWorld.getTemplate().getName(), e.getClass().getName() + ": " + e.getLocalizedMessage()));
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public void undoSchematicPaste(EditSession session) {
        session.undo(session);
        session.close();
    }

    // Internal tools

    private EditSession getNewEditSession(World worldForEditing) {
        // Get if using FAWE
        Class<?> editSessionBuilderClass = null;
        try {
            editSessionBuilderClass = Class.forName("com.fastasyncworldedit.core.util.EditSessionBuilder");
        } catch (ClassNotFoundException ignored) {
        }

        boolean isFAWE = editSessionBuilderClass != null;

        if (isFAWE) {
            try {

                Object editSessionBuilderUtil = editSessionBuilderClass
                        .getConstructor(com.sk89q.worldedit.world.World.class)
                        .newInstance(worldForEditing);

                Method buildMethod = editSessionBuilderClass.getMethod("build");
                return (EditSession) buildMethod.invoke(editSessionBuilderUtil);

            } catch (InvocationTargetException | InstantiationException | NoSuchMethodException | IllegalAccessException e) {
                e.printStackTrace();
                return null;
            }
        } else {
            return WorldEdit.getInstance().newEditSession(worldForEditing);
        }
    }

}
