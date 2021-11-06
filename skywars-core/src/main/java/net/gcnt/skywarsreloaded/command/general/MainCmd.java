package net.gcnt.skywarsreloaded.command.general;

import net.gcnt.skywarsreloaded.SkyWarsReloaded;
import net.gcnt.skywarsreloaded.command.Cmd;
import net.gcnt.skywarsreloaded.command.SWCommand;
import net.gcnt.skywarsreloaded.utils.properties.MessageProperties;
import net.gcnt.skywarsreloaded.wrapper.SWCommandSender;

public class MainCmd extends Cmd {

    public MainCmd(SkyWarsReloaded plugin) {
        super(plugin, "skywars", "", "skywars.command.main", false, null, null);
    }

    @Override
    public boolean run(SWCommandSender sender, String[] args) {
        // sending header.
        sender.sendMessage(plugin.getUtils().colorize(plugin.getMessages().getString(MessageProperties.CHAT_HEADER.toString())));

        // Sending stuff
        for (SWCommand cmd : plugin.getCommandManager().getBaseCommands()) {
            // Permission check
            if (!sender.hasPermission(cmd.getPermission())) continue;

            cmd.sendUsage(sender, true);
        }
        return false;
    }
}
