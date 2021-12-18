package net.gcnt.skywarsreloaded.command.general;

import net.gcnt.skywarsreloaded.SkyWarsReloaded;
import net.gcnt.skywarsreloaded.command.Cmd;
import net.gcnt.skywarsreloaded.game.GameTemplate;
import net.gcnt.skywarsreloaded.game.GameWorld;
import net.gcnt.skywarsreloaded.utils.SWCoord;
import net.gcnt.skywarsreloaded.wrapper.player.SWPlayer;
import net.gcnt.skywarsreloaded.wrapper.sender.SWCommandSender;

import java.util.ArrayList;
import java.util.List;

public class LobbySpawnCmd extends Cmd {

    public LobbySpawnCmd(SkyWarsReloaded plugin) {
        super(plugin, "skywars", "setlobby", "skywars.command.setlobby", true, "", "Set the lobby spawn.");
    }

    @Override
    public boolean run(SWCommandSender sender, String[] args) {
        SWPlayer player = (SWPlayer) sender;
        SWCoord loc = player.getLocation();

        if (loc.world() == null) {
            sender.sendMessage(plugin.getUtils().colorize("&cThe world you're in was not recognized by SkyWars, so we couldn't set the lobby spawn!"));
            return true;
        }
        for (GameWorld gw : plugin.getGameManager().getGameWorlds()) {
            if (gw.getWorldName().equals(loc.world().getName())) {
                sender.sendMessage(plugin.getUtils().colorize("&cYou can't set the lobby spawn in a game world!"));
                return true;
            }
        }

        plugin.getDataConfig().set("lobby", loc.toString());
        plugin.getDataConfig().save();
        sender.sendMessage(plugin.getUtils().colorize("&aSet the lobby spawn to your current location!"));
        return true;
    }

    @Override
    public List<String> onTabCompletion(SWCommandSender sender, String[] args) {
        if (args.length == 1) {
            List<String> maps = new ArrayList<>();
            plugin.getGameManager().getGameTemplates().forEach(template -> maps.add(template.getName()));
            return maps;
        }
        return new ArrayList<>();
    }
}
