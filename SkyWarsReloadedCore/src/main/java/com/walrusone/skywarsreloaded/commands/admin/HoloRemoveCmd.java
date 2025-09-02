package com.walrusone.skywarsreloaded.commands.admin;

import com.walrusone.skywarsreloaded.SkyWarsReloaded;
import com.walrusone.skywarsreloaded.utilities.Messaging;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class HoloRemoveCmd extends com.walrusone.skywarsreloaded.commands.BaseCmd {
    public HoloRemoveCmd(SkyWarsReloaded plugin, String t) {
        super(plugin);
        type = t;
        forcePlayer = true;
        cmdName = "holoremove";
        alias = new String[]{"hr", "removeholo", "removehologram", "hologramremove"};
        argLength = 1;
    }

    public boolean run(CommandSender sender, Player player, String[] args) {
        if (plugin.getHologramManager() == null) {
            player.sendMessage(new Messaging.MessageFormatter().format("error.holograms-not-enabled"));
            return true;
        }

        boolean result = plugin.getHologramManager().removeHologram(player.getLocation());
        if (result) {
            player.sendMessage(new Messaging.MessageFormatter().format("command.hologram-removed"));
            return true;
        }
        player.sendMessage(new Messaging.MessageFormatter().format("error.no-holograms-found"));
        return true;
    }
}
