package net.gcnt.skywarsreloaded.command.general;

import net.gcnt.skywarsreloaded.SkyWarsReloaded;
import net.gcnt.skywarsreloaded.command.Cmd;
import net.gcnt.skywarsreloaded.command.SWCommand;
import net.gcnt.skywarsreloaded.utils.properties.MessageProperties;
import net.gcnt.skywarsreloaded.wrapper.SWCommandSender;

import java.util.ArrayList;
import java.util.List;

public class MainCmd extends Cmd {

    public MainCmd(SkyWarsReloaded plugin) {
        super(plugin, "skywars", "", "skywars.command.main", false, null, "Get a list of commands.");
    }

    @Override
    public boolean run(SWCommandSender sender, String[] args) {
        // sending header.
        sender.sendMessage(plugin.getUtils().colorize(plugin.getMessages().getString(MessageProperties.CHAT_HEADER.toString())));

        // Sending stuff
        for (SWCommand cmd : plugin.getCommandManager().getCommands("skywars")) {
            // Permission check
            if (!sender.hasPermission(cmd.getPermission())) continue;

            sender.sendMessage(cmd.sendUsage(sender, false));
        }
        return true;
    }

    @Override
    public List<String> onTabCompletion(SWCommandSender sender, String[] args) {
        List<String> options = new ArrayList<>();
        for (SWCommand cmd : plugin.getCommandManager().getCommands(getParentCommand())) {
            options.add(cmd.getName());
        }
        return options;
    }
}
