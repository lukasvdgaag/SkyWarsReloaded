package net.gcnt.skywarsreloaded.command.maps;

import net.gcnt.skywarsreloaded.SkyWarsReloaded;
import net.gcnt.skywarsreloaded.command.Cmd;
import net.gcnt.skywarsreloaded.game.GameTemplate;
import net.gcnt.skywarsreloaded.wrapper.sender.SWCommandSender;

import java.util.ArrayList;
import java.util.List;

public class SetMapTeamsizeCmd extends Cmd {

    public SetMapTeamsizeCmd(SkyWarsReloaded plugin) {
        super(plugin, "skywarsmap", "teamsize", "skywars.command.map.teamsize", true, "<map> <teamsize>", "Set the team size.");
    }

    @Override
    public boolean run(SWCommandSender sender, String[] args) {
        if (args.length == 0) {
            sender.sendMessage(plugin.getUtils().colorize("&cPlease enter a game template name."));
            return true;
        } else if (args.length == 1) {
            sender.sendMessage(plugin.getUtils().colorize("&cPlease enter the number of players per team."));
            return true;
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

        // todo check if new team size is lower than current one and check all player spawnpoints for errors.

        template.setTeamSize(size);
        template.setIsTeamSizeSetup(true);
        sender.sendMessage(plugin.getUtils().colorize("&aThe team size of the map &e%s &ahas been changed to &e%d&a!".formatted(template.getName(), size)));

        template.checkToDoList(sender);
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
