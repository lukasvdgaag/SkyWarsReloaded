package net.gcnt.skywarsreloaded.command.maps;

import net.gcnt.skywarsreloaded.SkyWarsReloaded;
import net.gcnt.skywarsreloaded.command.Cmd;
import net.gcnt.skywarsreloaded.game.GameTemplate;
import net.gcnt.skywarsreloaded.game.GameWorld;
import net.gcnt.skywarsreloaded.wrapper.player.SWPlayer;
import net.gcnt.skywarsreloaded.wrapper.sender.SWCommandSender;

import java.util.ArrayList;
import java.util.Arrays;
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
            GameWorld world = plugin.getGameManager().getGameWorldFromWorldName(player.getLocation().world().getName());
            if (world == null || !world.isEditing() || world.getTemplate() == null) {
                sender.sendMessage(plugin.getUtils().colorize("&cThe world you're in either isn't a SkyWars template world, or it's not in edit mode."));
                return true;
            }
            template = world.getTemplate();
        } else {
            final String templateName = args[0];
            template = plugin.getGameManager().getGameTemplateByName(templateName);
            if (template == null) {
                sender.sendMessage(plugin.getUtils().colorize("&cThere is no game template with that name."));
                return true;
            }
        }

        if (template.checkToDoList(sender)) {
            sender.sendMessage(plugin.getUtils().colorize("&aTemplate &e%s &ais all set up!".formatted(template.getName())));
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
