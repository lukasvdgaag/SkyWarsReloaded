package com.walrusone.skywarsreloaded.game.cages.schematics;

import com.google.common.collect.Lists;
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

    public static HashMap<GameMap, HashMap<UUID, EditSession>> pastedSessions = new HashMap<>();

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

                    if (SkyWarsReloaded.getNMS().getVersion() < 13) {
                        new Schematic12().pasteSchematic(schematicFile,map,spawn,player);
                    }
                    else {
                        new Schematic13().pasteSchematic(schematicFile,map,spawn,player);
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
