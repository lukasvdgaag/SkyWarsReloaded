package com.walrusone.skywarsreloaded.game.cages.schematics;

import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.MaxChangedBlocksException;
import com.sk89q.worldedit.Vector;
import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.bukkit.BukkitWorld;
import com.sk89q.worldedit.extent.clipboard.Clipboard;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardFormat;
import com.sk89q.worldedit.function.mask.ExistingBlockMask;
import com.sk89q.worldedit.function.operation.ForwardExtentCopy;
import com.sk89q.worldedit.function.operation.Operations;
import com.sk89q.worldedit.math.transform.AffineTransform;
import com.sk89q.worldedit.world.World;
import com.sk89q.worldedit.world.registry.WorldData;
import com.walrusone.skywarsreloaded.game.GameMap;
import com.walrusone.skywarsreloaded.menus.gameoptions.objects.CoordLoc;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.UUID;

public class Schematic12 {

    public void pasteSchematic(File schematicFile, GameMap map, CoordLoc spawn, Player player) {
        try {
            World weWorld = new BukkitWorld(map.getCurrentWorld());
            WorldData worldData = weWorld.getWorldData();
            Clipboard clipboard = ClipboardFormat.SCHEMATIC.getReader(new FileInputStream(schematicFile)).read(worldData);

            EditSession extent = WorldEdit.getInstance().getEditSessionFactory().getEditSession(weWorld, 250);
            AffineTransform transform = new AffineTransform();

            ForwardExtentCopy copy = new ForwardExtentCopy(clipboard, clipboard.getRegion(), clipboard.getOrigin(), extent, new Vector(spawn.getX(), spawn.getY(), spawn.getZ()));
            if (!transform.isIdentity()) copy.setTransform(transform);
            copy.setSourceMask(new ExistingBlockMask(clipboard));
            Operations.completeLegacy(copy);
            extent.flushQueue();

            HashMap<UUID, EditSession> sessions = SchematicCage.pastedSessions.containsKey(map) ? SchematicCage.pastedSessions.get(map) : new HashMap<>();
            sessions.put(player.getUniqueId(), extent);
            SchematicCage.pastedSessions.put(map, sessions);
        } catch (IOException | MaxChangedBlocksException e) {
            e.printStackTrace();
        }
    }
}
