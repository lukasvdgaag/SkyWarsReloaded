package com.walrusone.skywarsreloaded.game.cages;

import com.google.common.collect.Lists;
import com.sk89q.worldedit.CuboidClipboard;
import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.MaxChangedBlocksException;
import com.sk89q.worldedit.Vector;
import com.sk89q.worldedit.bukkit.BukkitWorld;
import com.sk89q.worldedit.bukkit.WorldEditPlugin;
import com.sk89q.worldedit.schematic.MCEditSchematicFormat;
import com.sk89q.worldedit.world.DataException;
import com.walrusone.skywarsreloaded.SkyWarsReloaded;
import com.walrusone.skywarsreloaded.game.GameMap;
import com.walrusone.skywarsreloaded.game.TeamCard;
import com.walrusone.skywarsreloaded.managers.PlayerStat;
import com.walrusone.skywarsreloaded.menus.gameoptions.objects.CoordLoc;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;

public class SchematicCage {

    private static HashMap<GameMap, HashMap<TeamCard, EditSession>> pastedSessions = new HashMap<>();

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

    public void createSpawnPlatform(GameMap map, Player player) {
        PlayerStat pStat = PlayerStat.getPlayerStats(player);
        if (pStat != null) {
            if (map.getTeamSize() == 1) {
                if (pastedSessions.containsKey(map)) {
                    if (pastedSessions.get(map).containsKey(map.getTeamCard(player))) {
                        return;
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
                        return;
                    }

                    TeamCard team = map.getTeamCard(player);
                    CoordLoc spawn = team.getSpawn();

                    WorldEditPlugin worldEditPlugin = (WorldEditPlugin) Bukkit.getPluginManager().getPlugin("WorldEdit");
                    EditSession session = worldEditPlugin.getWorldEdit().getEditSessionFactory().getEditSession(new BukkitWorld(map.getCurrentWorld()), 10000);
                    try {
                        CuboidClipboard clipboard = MCEditSchematicFormat.getFormat(schematicFile).load(schematicFile);
                        clipboard.paste(session, new Vector(spawn.getX(), spawn.getY(), spawn.getZ()), true);
                        HashMap<TeamCard, EditSession> sessions = pastedSessions.containsKey(map) ? pastedSessions.get(map) : new HashMap<>();
                        sessions.put(team, session);
                        pastedSessions.put(map, sessions);
                    } catch (MaxChangedBlocksException | DataException | IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    public void removeSpawnPlatform(GameMap map, Player player) {
        if (pastedSessions.containsKey(map)) {
            EditSession session = pastedSessions.get(map).get(map.getTeamCard(player));
            session.undo(session);
        }
    }

}
