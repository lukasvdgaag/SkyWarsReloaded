package net.gcnt.skywarsreloaded.command.maps;

import net.gcnt.skywarsreloaded.SkyWarsReloaded;
import net.gcnt.skywarsreloaded.command.Cmd;
import net.gcnt.skywarsreloaded.game.GameTemplate;
import net.gcnt.skywarsreloaded.wrapper.SWCommandSender;

import java.util.Arrays;

public class SetMapTeamsizeCmd extends Cmd {

    public SetMapTeamsizeCmd(SkyWarsReloaded plugin) {
        super(plugin, "skywarsmap", "teamsize", "skywars.command.map.teamsize", true, "<map> <teamsize>", "Set the team size.");
    }

    @Override
    public boolean run(SWCommandSender sender, String[] args) {
        if (args.length == 0) {
            sender.sendMessage(plugin.getUtils().colorize("&cPlease enter a game template name."));
            return false; // todo not sure if I should return false/true here to show the usage too or not.
        } else if (args.length == 1) {
            sender.sendMessage(plugin.getUtils().colorize("&cPlease enter the number of players per team."));
            return false; // todo not sure if I should return false/true here to show the usage too or not.
        }

        if (!plugin.getUtils().isInt(args[1])) {
            sender.sendMessage(plugin.getUtils().colorize("&cPlease enter a valid team size (number)."));
            return false;
        }
        int size = Integer.parseInt(args[1]);
        if (size < 1) {
            sender.sendMessage(plugin.getUtils().colorize("&cPlease enter a valid team size (greater than 0)"));
            return false;
        }

        final String templateName = args[0];
        GameTemplate template = plugin.getGameManager().getGameTemplateByName(templateName);
        if (template == null) {
            sender.sendMessage(plugin.getUtils().colorize("&cThere is no game template with that name."));
            return true;
        }

        template.setTeamSize(size);
        sender.sendMessage(plugin.getUtils().colorize("&aThe team size of the map &e" + templateName + " &ahas been changed to &e" + size + "&a!"));
        return true;
    }
}
