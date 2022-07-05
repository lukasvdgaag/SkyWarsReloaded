package net.gcnt.skywarsreloaded.command.maps;

import net.gcnt.skywarsreloaded.SkyWarsReloaded;
import net.gcnt.skywarsreloaded.command.Cmd;
import net.gcnt.skywarsreloaded.game.GameTemplate;
import net.gcnt.skywarsreloaded.game.GameWorld;
import net.gcnt.skywarsreloaded.utils.properties.MessageProperties;
import net.gcnt.skywarsreloaded.wrapper.entity.SWPlayer;
import net.gcnt.skywarsreloaded.wrapper.sender.SWCommandSender;

import java.util.ArrayList;
import java.util.List;

public class TodoMapCmd extends Cmd {

    public TodoMapCmd(SkyWarsReloaded plugin) {
        super(plugin, "skywarsmap", "todo", "skywars.command.map.todo", true, "[map]", "Check the todo list.", "td");
    }

    @Override
    public boolean run(SWCommandSender sender, String[] args) {
        SWPlayer player = (SWPlayer) sender;
        GameTemplate template;
        if (args.length == 0) {
            GameWorld world = plugin.getGameManager().getGameWorldByName(player.getLocation().getWorld().getName());
            if (world == null || !world.isEditing() || world.getTemplate() == null) {
                plugin.getMessages().getMessage(MessageProperties.ERROR_NO_TEMPLATE_WORLD_FOUND.toString()).send(sender);
                return true;
            }
            template = world.getTemplate();
        } else {
            final String templateName = args[0];
            template = plugin.getGameManager().getGameTemplateByName(templateName);
            if (template == null) {
                plugin.getMessages().getMessage(MessageProperties.MAPS_DOESNT_EXIST.toString()).send(sender);
                return true;
            }
        }

        if (template.checkToDoList(sender)) {
            plugin.getMessages().getMessage(MessageProperties.MAPS_ALL_SET_UP.toString())
                    .replace("%template%", template.getName())
                    .send(sender);
        }
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
