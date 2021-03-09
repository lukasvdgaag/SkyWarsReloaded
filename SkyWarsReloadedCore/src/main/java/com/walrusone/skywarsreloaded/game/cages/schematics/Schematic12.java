package com.walrusone.skywarsreloaded.game.cages.schematics;

import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.MaxChangedBlocksException;
import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.bukkit.BukkitWorld;
import com.sk89q.worldedit.extent.Extent;
import com.sk89q.worldedit.extent.clipboard.Clipboard;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardFormat;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardReader;
import com.sk89q.worldedit.function.mask.ExistingBlockMask;
import com.sk89q.worldedit.function.operation.ForwardExtentCopy;
import com.sk89q.worldedit.function.operation.Operations;
import com.sk89q.worldedit.math.transform.AffineTransform;
import com.sk89q.worldedit.regions.Region;
import com.sk89q.worldedit.world.World;
import com.walrusone.skywarsreloaded.game.GameMap;
import com.walrusone.skywarsreloaded.menus.gameoptions.objects.CoordLoc;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.UUID;

public class Schematic12 {

    public void pasteSchematic(File schematicFile, GameMap map, CoordLoc spawn, Player player) {
        try {
            World weWorld = new BukkitWorld(map.getCurrentWorld());
            Object worldData = weWorld.getClass().getDeclaredMethod("getWorldData").invoke(weWorld);
            ClipboardFormat cf = ClipboardFormat.class.getEnumConstants()[0];
            Method cm = cf.getClass().getDeclaredMethod("getReader", InputStream.class);
            cm.setAccessible(true);
            ClipboardReader reader = (ClipboardReader) cm.invoke(cf, new FileInputStream(schematicFile));
            Clipboard clipboard = (Clipboard) reader.getClass().getMethod("read", Class.forName("com.sk89q.worldedit.world.registry.WorldData")).invoke(reader, worldData);
            // Clipboard clipboard = ClipboardFormat.SCHEMATIC.getReader(new FileInputStream(schematicFile)).read(worldData); // clipboard format

            EditSession extent = WorldEdit.getInstance().getEditSessionFactory().getEditSession(weWorld, 250);
            AffineTransform transform = new AffineTransform();

            Class vector = Class.forName("com.sk89q.worldedit.Vector");

            //ForwardExtentCopy copy = new ForwardExtentCopy(clipboard, clipboard.getRegion(), clipboard.getOrigin(), extent, new Vector(spawn.getX(), spawn.getY(), spawn.getZ()));
            Constructor<ForwardExtentCopy> con = ForwardExtentCopy.class.getConstructor(Extent.class, Region.class, vector, Extent.class, vector);
            con.setAccessible(true);

            Object newVector = vector.getDeclaredConstructor(int.class, int.class, int.class).newInstance(spawn.getX(), spawn.getY(), spawn.getZ());

            ForwardExtentCopy copy = con.newInstance(clipboard, clipboard.getRegion(), clipboard.getClass().getMethod("getOrigin").invoke(clipboard), extent, vector.cast(newVector));

            // new ForwardExtentCopy(clipboard, region, origin, BlockVector3, EditSession, Vector)
            if (!transform.isIdentity()) copy.setTransform(transform);
            copy.setSourceMask(new ExistingBlockMask(clipboard));
            Operations.completeLegacy(copy);
            extent.getClass().getMethod("flushQueue").invoke(extent);

            // invoke method flushQueue

            HashMap<UUID, EditSession> sessions = SchematicCage.pastedSessions.containsKey(map) ? SchematicCage.pastedSessions.get(map) : new HashMap<>();
            sessions.put(player.getUniqueId(), extent);
            SchematicCage.pastedSessions.put(map, sessions);
        } catch (MaxChangedBlocksException | NoSuchMethodException | InstantiationException | InvocationTargetException | IllegalAccessException | IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
