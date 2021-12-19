package net.gcnt.skywarsreloaded.command.maps;

import net.gcnt.skywarsreloaded.SkyWarsReloaded;
import net.gcnt.skywarsreloaded.command.Cmd;
import net.gcnt.skywarsreloaded.game.GameTemplate;
import net.gcnt.skywarsreloaded.game.GameWorld;
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
            sender.sendMessage(plugin.getUtils().colorize("&cPlease enter the world size."));
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
            sender.sendMessage(plugin.getUtils().colorize("&cPlease enter a valid world size (number)."));
            return false;
        }
        int size = Integer.parseInt(args[creatorArgStart]);
        if (size < 1) {
            sender.sendMessage(plugin.getUtils().colorize("&cPlease enter a valid world size (greater than 0)"));
            return false;
        }

        template.setBorderRadius(size);

        List<GameWorld> gameWorld = plugin.getGameManager().getGameWorlds(template);
        for (GameWorld world : gameWorld) {
            if (world.isEditing()) {
                plugin.getWorldLoader().updateWorldBorder(world);
            }
        }

        sender.sendMessage(plugin.getUtils().colorize("&aThe world size of the map &e%s &ahas been changed to &e%d&a!".formatted(template.getName(), size)));

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
