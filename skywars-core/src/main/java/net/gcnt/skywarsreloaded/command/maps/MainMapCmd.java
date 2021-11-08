package net.gcnt.skywarsreloaded.command.maps;

import net.gcnt.skywarsreloaded.SkyWarsReloaded;
import net.gcnt.skywarsreloaded.command.Cmd;
import net.gcnt.skywarsreloaded.command.SWCommand;
import net.gcnt.skywarsreloaded.utils.properties.MessageProperties;
import net.gcnt.skywarsreloaded.wrapper.SWCommandSender;

public class MainMapCmd extends Cmd {

    public MainMapCmd(SkyWarsReloaded plugin) {
        super(plugin, "skywarsmap", "", "skywars.command.map.main", false, null, "Get a list of commands.");
    }

    @Override
    public boolean run(SWCommandSender sender, String[] args) {
        // sending header.
        sender.sendMessage(plugin.getUtils().colorize(plugin.getMessages().getString(MessageProperties.CHAT_HEADER.toString())));

        // Sending stuff
        for (SWCommand cmd : plugin.getCommandManager().getCommands("skywarsmap")) {
            // Permission check
            if (!sender.hasPermission(cmd.getPermission())) continue;

            sender.sendMessage(cmd.sendUsage(sender, false));
        }
        return false;
    }
}
