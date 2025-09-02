package com.walrusone.skywarsreloaded.commands.maps;

import com.walrusone.skywarsreloaded.SkyWarsReloaded;
import com.walrusone.skywarsreloaded.commands.BaseCmd;
import com.walrusone.skywarsreloaded.enums.GameType;
import com.walrusone.skywarsreloaded.game.GameMap;
import com.walrusone.skywarsreloaded.game.TeamCard;
import com.walrusone.skywarsreloaded.managers.MatchManager;
import com.walrusone.skywarsreloaded.menus.gameoptions.objects.CoordLoc;
import com.walrusone.skywarsreloaded.utilities.Messaging;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class DebugCmd extends BaseCmd {
    public DebugCmd(SkyWarsReloaded plugin, String t) {
        super(plugin);
        type = t;
        forcePlayer = false;
        cmdName = "debug";
        alias = new String[0];
        argLength = 2;
    }


    public boolean run(CommandSender sender, Player player, String[] args) {
        String worldName = args[1];
        GameMap gMap = SkyWarsReloaded.getGameMapMgr().getMap(worldName);
        if (worldName.equalsIgnoreCase("null")) {
            sender.sendMessage("Is random vote enabled? " + SkyWarsReloaded.getCfg().isRandomVoteEnabled());
            return true;
        }

        if (gMap == null) {
            sender.sendMessage(new Messaging.MessageFormatter().format("error.map-does-not-exist"));
            return true;
        }

        sender.sendMessage("-- Debug of arena: " + worldName);
        sender.sendMessage("Registered: " + gMap.isRegistered());
        sender.sendMessage("Status: " + gMap.getMatchState().name());
        sender.sendMessage("Players: " + gMap.getPlayerCount() + "/" + gMap.getMaxPlayers());
        sender.sendMessage("Playable arenas amount: " + SkyWarsReloaded.getGameMapMgr().getPlayableArenas(GameType.ALL).size());
        sender.sendMessage(" ");
        sender.sendMessage(" ");

        sender.sendMessage("-- Debug of teams");
        sender.sendMessage("Teamcards: " + gMap.getTeamCards().size());
        for (TeamCard card : gMap.getTeamCards()) {
            sender.sendMessage("#" + card.getPosition() + ": p=" + card.getPlayerCards().size() + ", s=" + card.getSpawns().size());
        }
        sender.sendMessage(" ");

        sender.sendMessage("Spawn teams: " + gMap.getSpawnLocations().size());
        for (TeamCard key : gMap.getSpawnLocations().keySet()) {
            for (CoordLoc loc : gMap.getSpawnLocations().get(key)) {
                sender.sendMessage("T-" + key.getTeamName() + ": " + loc.getLocationString());
            }
        }

        sender.sendMessage(" ");

        // List all chests
        sender.sendMessage("Island chests: " + gMap.getChests().size());
        for (CoordLoc loc : gMap.getChests()) {
            sender.sendMessage(loc.getLocationString());
        }

        sender.sendMessage(" ");

        // List all center chests
        sender.sendMessage("Center chests: " + gMap.getCenterChests().size());
        for (CoordLoc loc : gMap.getCenterChests()) {
            sender.sendMessage(loc.getLocationString());
        }

        sender.sendMessage(" ");

        if (sender instanceof Player) {
            sender.sendMessage("-- Debug of player: " + sender.getName());
            GameMap map = MatchManager.get().getPlayerMap(player);
            GameMap dead = MatchManager.get().getPlayerMap(player);
            GameMap spec = MatchManager.get().getPlayerMap(player);

            sender.sendMessage("Are you in-game as player? " + (map == null ? "no" : "yes") + " " + (map != null ? map.getName() : ""));
            sender.sendMessage("Are you in-game as dead? " + (dead == null ? "no" : "yes") + " " + (dead != null ? dead.getName() : ""));
            sender.sendMessage("Are you in-game as spec? " + (spec == null ? "no" : "yes") + " " + (spec != null ? spec.getName() : ""));
        }
        return true;
    }
}
