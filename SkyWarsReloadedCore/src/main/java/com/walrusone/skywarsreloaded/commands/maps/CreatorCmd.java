package com.walrusone.skywarsreloaded.commands.maps;

import com.walrusone.skywarsreloaded.SkyWarsReloaded;
import com.walrusone.skywarsreloaded.game.GameMap;
import com.walrusone.skywarsreloaded.utilities.Messaging;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.StringJoiner;

public class CreatorCmd extends com.walrusone.skywarsreloaded.commands.BaseCmd {
    public CreatorCmd(SkyWarsReloaded plugin, String t) {
        super(plugin);
        type = t;
        forcePlayer = false;
        cmdName = "creator";
        alias = new String[]{"maker"};
        argLength = 3;
    }

    public boolean run(CommandSender sender, Player player, String[] args) {
        String worldName = args[1];
        StringJoiner creator = new StringJoiner(" ");
        for (int i = 2; i < args.length; i++) {
            creator.add(args[i]);
        }
        //creator.substring(0, creator.length() - 1);
        if (creator.length() == 0) {
            sender.sendMessage(new Messaging.MessageFormatter().format("error.map-creator"));
            return true;
        }
        GameMap map = SkyWarsReloaded.getGameMapMgr().getMap(worldName);
        if (map != null) {
            map.setCreator(creator.toString().trim());
            sender.sendMessage(new Messaging.MessageFormatter().setVariable("mapname", worldName).setVariable("creator", creator.toString().trim()).format("maps.creator"));

            return true;
        }
        sender.sendMessage(new Messaging.MessageFormatter().format("error.map-does-not-exist"));
        return true;
    }
}
