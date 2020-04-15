package com.walrusone.skywarsreloaded.game.cages;

import com.google.common.collect.Lists;
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
import com.sk89q.worldedit.regions.Region;
import com.sk89q.worldedit.world.World;
import com.sk89q.worldedit.world.registry.WorldData;
import com.walrusone.skywarsreloaded.SkyWarsReloaded;
import com.walrusone.skywarsreloaded.game.GameMap;
import com.walrusone.skywarsreloaded.game.TeamCard;
import com.walrusone.skywarsreloaded.managers.PlayerStat;
import com.walrusone.skywarsreloaded.menus.gameoptions.objects.CoordLoc;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class SchematicCage {

    private static HashMap<GameMap, HashMap<UUID, EditSession>> pastedSessions = new HashMap<>();

    public List<File> getSchematics() {
        List<File> files = Lists.newArrayList();
        File folder = new File(SkyWarsReloaded.get().getDataFolder(), "cages");
        if (folder.exists() && folder.isDirectory()) {
            for (File f : folder.listFiles()) {
                if (f.getName().endsWith(".schematic")) {
                    files.add(f);
                }
            }
        }
        return files;
    }

    public boolean createSpawnPlatform(GameMap map, Player player) {
        if (!Bukkit.getPluginManager().isPluginEnabled("WorldEdit")) {
            return false;
        }

        PlayerStat pStat = PlayerStat.getPlayerStats(player);
        if (pStat != null) {
            if (map.getTeamSize() == 1) {
                if (pastedSessions.containsKey(map)) {
                    if (pastedSessions.get(map).containsKey(player.getUniqueId())) {
                        return true;
                    }
                }


                String cage = pStat.getGlassColor();
                if (cage.startsWith("custom-")) {
                    cage = cage.replace("custom-", "");
                    File schematicFile = null;
                    for (File f : getSchematics()) {
                        if (f.getName().equals(cage + ".schematic")) {
                            schematicFile = f;
                        }
                    }

                    if (schematicFile == null) {
                        return false;
                    }

                    TeamCard team = map.getTeamCard(player);
                    CoordLoc spawn = team.getSpawn();

                    try {
                        World weWorld = new BukkitWorld(map.getCurrentWorld());
                        WorldData worldData = weWorld.getWorldData();
                        Clipboard clipboard = ClipboardFormat.SCHEMATIC.getReader(new FileInputStream(schematicFile)).read(worldData);
                        Region region = clipboard.getRegion();

                        EditSession extent = WorldEdit.getInstance().getEditSessionFactory().getEditSession(weWorld, 250);
                        AffineTransform transform = new AffineTransform();

                        ForwardExtentCopy copy = new ForwardExtentCopy(clipboard, clipboard.getRegion(), clipboard.getOrigin(), extent, new Vector(spawn.getX(), spawn.getY(), spawn.getZ()));
                        if (!transform.isIdentity()) copy.setTransform(transform);
                        copy.setSourceMask(new ExistingBlockMask(clipboard));
                        Operations.completeLegacy(copy);
                        extent.flushQueue();

                        HashMap<UUID, EditSession> sessions = pastedSessions.containsKey(map) ? pastedSessions.get(map) : new HashMap<>();
                        sessions.put(player.getUniqueId(), extent);
                        pastedSessions.put(map, sessions);
                        return true;
                    } catch (MaxChangedBlocksException | IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return false;
    }

    public void removeSpawnPlatform(GameMap map, Player player) {
        if (pastedSessions.containsKey(map)) {
            if (pastedSessions.get(map).containsKey(player.getUniqueId())) {
                EditSession session = pastedSessions.get(map).get(player.getUniqueId());
                session.undo(session);
                pastedSessions.get(map).remove(player.getUniqueId());
            }
        }
    }

}
