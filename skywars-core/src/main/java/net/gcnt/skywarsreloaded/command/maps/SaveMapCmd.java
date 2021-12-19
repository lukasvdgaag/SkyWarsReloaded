package net.gcnt.skywarsreloaded.command.maps;

import net.gcnt.skywarsreloaded.SkyWarsReloaded;
import net.gcnt.skywarsreloaded.command.Cmd;
import net.gcnt.skywarsreloaded.game.GameTemplate;
import net.gcnt.skywarsreloaded.game.GameWorld;
import net.gcnt.skywarsreloaded.utils.properties.MessageProperties;
import net.gcnt.skywarsreloaded.wrapper.player.SWPlayer;
import net.gcnt.skywarsreloaded.wrapper.sender.SWCommandSender;

import java.util.ArrayList;
import java.util.List;

public class SaveMapCmd extends Cmd {

    public SaveMapCmd(SkyWarsReloaded plugin) {
        super(plugin, "skywarsmap", "save", "skywars.command.map.creator", true, "[map]", "Save a map template.", "s");
    }

    @Override
    public boolean run(SWCommandSender sender, String[] args) {
        SWPlayer player = (SWPlayer) sender;

        GameTemplate template;
        if (args.length == 0) {
            GameWorld world = plugin.getGameManager().getGameWorldFromWorldName(player.getLocation().world().getName());
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

        List<GameWorld> worlds = plugin.getGameManager().getGameWorlds(template);
        for (GameWorld world : worlds) {
            if (world.isEditing()) {
                // schematic creating and saving
                boolean successful = plugin.getSchematicManager().saveGameWorldToSchematic(world, plugin.getUtils().getWorldEditWorld(world.getWorldName()));
                if (successful) {
                    plugin.getMessages().getMessage(MessageProperties.MAPS_WORLD_SAVED.toString()).replace("%template%", template.getName()).send(sender);
                } else {
                    plugin.getMessages().getMessage(MessageProperties.MAPS_WORLD_SAVED_FAIL.toString()).replace("%template%", template.getName()).send(sender);
                }
                break;
            }
        }

        template.saveData();

        plugin.getMessages().getMessage(MessageProperties.MAPS_SAVED.toString()).replace("%template%", template.getName()).send(sender);
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
