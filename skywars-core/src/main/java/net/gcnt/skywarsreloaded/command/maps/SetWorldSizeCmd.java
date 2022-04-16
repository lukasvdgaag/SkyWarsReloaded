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

public class SetWorldSizeCmd extends Cmd {

    public SetWorldSizeCmd(SkyWarsReloaded plugin) {
        super(plugin, "skywarsmap", "worldsize", "skywars.command.map.worldsize", true, "[map] <worldsize>", "Set the world size.", "ws");
    }

    @Override
    public boolean run(SWCommandSender sender, String[] args) {
        SWPlayer player = (SWPlayer) sender;
        GameTemplate template;
        int creatorArgStart = 1;

        if (args.length == 0) {
            plugin.getMessages().getMessage(MessageProperties.MAPS_ENTER_WORLD_SIZE.toString()).send(sender);
            return true;
        } else if (args.length == 1) {
            GameWorld world = plugin.getGameManager().getGameWorldByName(player.getLocation().getWorld().getName());
            if (world == null || !world.isEditing() || world.getTemplate() == null) {
                plugin.getMessages().getMessage(MessageProperties.ERROR_NO_TEMPLATE_WORLD_FOUND.toString()).send(sender);
                return true;
            }
            template = world.getTemplate();
            creatorArgStart = 0;
        } else {
            final String templateName = args[0];
            template = plugin.getGameManager().getGameTemplateByName(templateName);
            if (template == null) {
                plugin.getMessages().getMessage(MessageProperties.MAPS_DOESNT_EXIST.toString()).send(sender);
                return true;
            }
        }

        if (!plugin.getUtils().isInt(args[creatorArgStart])) {
            plugin.getMessages().getMessage(MessageProperties.MAPS_ENTER_WORLD_SIZE_NUMBER.toString()).send(sender);
            return false;
        }
        int size = Integer.parseInt(args[creatorArgStart]);
        if (size < 1) {
            plugin.getMessages().getMessage(MessageProperties.MAPS_ENTER_WORLD_SIZE_GREATER.toString()).send(sender);
            return false;
        }

        template.setBorderRadius(size);

        List<GameWorld> gameWorld = plugin.getGameManager().getGameWorlds(template);
        for (GameWorld world : gameWorld) {
            if (world.isEditing()) {
                plugin.getWorldLoader().updateWorldBorder(world);
            }
        }

        plugin.getMessages().getMessage(MessageProperties.MAPS_SET_WORLD_SIZE.toString()).replace("%template%", template.getName()).send(sender);
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
