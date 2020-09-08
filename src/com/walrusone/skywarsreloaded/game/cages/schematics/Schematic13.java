package com.walrusone.skywarsreloaded.game.cages.schematics;

import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.WorldEditException;
import com.sk89q.worldedit.bukkit.BukkitWorld;
import com.sk89q.worldedit.extent.clipboard.Clipboard;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardFormat;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardFormats;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardReader;
import com.sk89q.worldedit.function.operation.Operation;
import com.sk89q.worldedit.function.operation.Operations;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldedit.session.ClipboardHolder;
import com.walrusone.skywarsreloaded.game.GameMap;
import com.walrusone.skywarsreloaded.menus.gameoptions.objects.CoordLoc;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.UUID;

public class Schematic13 {

    public void pasteSchematic(File schematicFile, GameMap map, CoordLoc spawn, Player player) {
        try {
            Clipboard clipboard;
            ClipboardFormat format = ClipboardFormats.findByFile(schematicFile);

            try (ClipboardReader reader = format.getReader(new FileInputStream(schematicFile))) {
                clipboard = reader.read();
            }

            try (EditSession editSession = WorldEdit.getInstance().getEditSessionFactory().getEditSession(new BukkitWorld(map.getCurrentWorld()), -1)) {
                Operation operation = new ClipboardHolder(clipboard)
                        .createPaste(editSession)
                        .to(BlockVector3.at(spawn.getX(), spawn.getY(), spawn.getZ()))
                        .build();
                Operations.complete(operation);
                HashMap<UUID, EditSession> sessions = SchematicCage.pastedSessions.containsKey(map) ? SchematicCage.pastedSessions.get(map) : new HashMap<>();
                sessions.put(player.getUniqueId(), editSession);
                SchematicCage.pastedSessions.put(map, sessions);
            } catch (WorldEditException e) {
                e.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
