package net.gcnt.skywarsreloaded.command.maps;

import net.gcnt.skywarsreloaded.SkyWarsReloaded;
import net.gcnt.skywarsreloaded.command.Cmd;
import net.gcnt.skywarsreloaded.game.GameTemplate;
import net.gcnt.skywarsreloaded.game.gameinstance.GameInstance;
import net.gcnt.skywarsreloaded.utils.properties.MessageProperties;
import net.gcnt.skywarsreloaded.wrapper.entity.SWPlayer;
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
            plugin.getMessages().getMessage(MessageProperties.MAPS_ENTER_TEAM_SIZE.toString()).send(sender);
            return true;
        } else if (args.length == 1) {
            GameInstance world = plugin.getGameInstanceManager().getGameWorldByName(player.getLocation().getWorld().getName());
            if (world == null || !world.isEditing() || world.getTemplate() == null) {
                plugin.getMessages().getMessage(MessageProperties.ERROR_NO_TEMPLATE_WORLD_FOUND.toString()).send(sender);
                return true;
            }
            template = world.getTemplate();
            creatorArgStart = 0;
        } else {
            final String templateName = args[0];
            template = plugin.getGameInstanceManager().getGameTemplateByName(templateName);
            if (template == null) {
                plugin.getMessages().getMessage(MessageProperties.MAPS_DOESNT_EXIST.toString()).send(sender);
                return true;
            }
        }

        if (!plugin.getUtils().isInt(args[creatorArgStart])) {
            plugin.getMessages().getMessage(MessageProperties.MAPS_ENTER_TEAM_SIZE_NUMBER.toString()).send(sender);
            return false;
        }

        int size = Integer.parseInt(args[creatorArgStart]);
        if (size < 1) {
            plugin.getMessages().getMessage(MessageProperties.MAPS_ENTER_TEAM_SIZE_GREATER.toString()).send(sender);
            return false;
        }

        template.setTeamSize(size);
        template.setIsTeamSizeSetup(true);
        plugin.getMessages().getMessage(MessageProperties.MAPS_SET_TEAM_SIZE.toString())
                .replace("%template%", template.getName())
                .replace("%size%", size + "")
                .send(sender);

        template.checkToDoList(sender);
        return true;
    }

    @Override
    public List<String> onTabCompletion(SWCommandSender sender, String[] args) {
        List<String> suggestions = new ArrayList<>();
        if (args.length == 1) {
            for (int i = 1; i < 5; i++) {
                suggestions.add(String.valueOf(i));
            }
        } else if (args.length == 2) {
            plugin.getGameInstanceManager().getGameTemplates().forEach(template -> suggestions.add(template.getName()));
        }
        return suggestions;
    }
}
