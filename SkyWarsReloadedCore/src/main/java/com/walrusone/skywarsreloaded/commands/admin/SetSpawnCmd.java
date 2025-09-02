package com.walrusone.skywarsreloaded.commands.admin;

import com.walrusone.skywarsreloaded.SkyWarsReloaded;
import com.walrusone.skywarsreloaded.utilities.Messaging;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SetSpawnCmd extends com.walrusone.skywarsreloaded.commands.BaseCmd {
    public SetSpawnCmd(SkyWarsReloaded plugin, String t) {
        super(plugin);
        type = t;
        forcePlayer = true;
        cmdName = "setspawn";
        alias = new String[]{"sspawn"};
        argLength = 1;
    }

    public boolean run(CommandSender sender, Player player, String[] args) {
        org.bukkit.Location spawn = player.getLocation();
        SkyWarsReloaded.getCfg().setSpawn(spawn);
        SkyWarsReloaded.getCfg().save();
        player.sendMessage(new Messaging.MessageFormatter().format("command.spawnset"));
        return true;
    }
}
