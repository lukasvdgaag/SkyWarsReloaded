package net.gcnt.skywarsreloaded.command.maps;

import net.gcnt.skywarsreloaded.SkyWarsReloaded;
import net.gcnt.skywarsreloaded.command.Cmd;
import net.gcnt.skywarsreloaded.game.GameTemplate;
import net.gcnt.skywarsreloaded.game.GameWorld;
import net.gcnt.skywarsreloaded.wrapper.player.SWPlayer;
import net.gcnt.skywarsreloaded.wrapper.sender.SWCommandSender;

import java.util.ArrayList;
import java.util.List;

public class SetMapTeamsizeCmd extends Cmd {

    public SetMapTeamsizeCmd(SkyWarsReloaded plugin) {
        super(plugin, "skywarsmap", "teamsize", "skywars.command.map.teamsize", true, "[map]<teamsize>", "Set the team size.", "ts", "size");
    }

    @Override
    public boolean run(SWCommandSender sender, String[] args) {
        SWPlayer player = (SWPlayer) sender;
        GameTemplate template;
        int creatorArgStart = 1;

        if (args.length == 0) {
            sender.sendMessage(plugin.getUtils().colorize("&cPlease enter the team size."));
            return true;
        } else if (args.length == 1) {
            GameWorld world = plugin.getGameManager().getGameWorldFromWorldName(player.getLocation().world().getName());
            if (world == null || !world.isEditing() || world.getTemplate() == null) {
                sender.sendMessage(plugin.getUtils().colorize("&cThe world you're in either isn't a SkyWars template world, or it's not in edit mode."));
                return true;
            }
            template = world.getTemplate();
            creatorArgStart = 0;
        } else {
            final String templateName = args[0];
            template = plugin.getGameManager().getGameTemplateByName(templateName);
            if (template == null) {
                sender.sendMessage(plugin.getUtils().colorize("&cThere is no game template with that name."));
                return true;
            }
        }

        if (!plugin.getUtils().isInt(args[creatorArgStart])) {
            sender.sendMessage(plugin.getUtils().colorize("&cPlease enter a valid team size (number)."));
            return false;
        }
        int size = Integer.parseInt(args[creatorArgStart]);
        if (size < 1) {
            sender.sendMessage(plugin.getUtils().colorize("&cPlease enter a valid team size (greater than 0)"));
            return false;
        }

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
