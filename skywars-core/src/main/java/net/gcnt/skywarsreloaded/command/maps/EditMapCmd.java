package net.gcnt.skywarsreloaded.command.maps;

import net.gcnt.skywarsreloaded.SkyWarsReloaded;
import net.gcnt.skywarsreloaded.command.Cmd;
import net.gcnt.skywarsreloaded.game.GameTemplate;
import net.gcnt.skywarsreloaded.game.GameWorld;
import net.gcnt.skywarsreloaded.game.types.GameStatus;
import net.gcnt.skywarsreloaded.utils.properties.InternalProperties;
import net.gcnt.skywarsreloaded.wrapper.player.SWPlayer;
import net.gcnt.skywarsreloaded.wrapper.sender.SWCommandSender;

import java.util.ArrayList;
import java.util.List;

public class EditMapCmd extends Cmd {

    public EditMapCmd(SkyWarsReloaded plugin) {
        super(plugin, "skywarsmap", "edit", "skywars.command.map.edit", true, "<name>", "Edit a map template.");
    }

    @Override
    public boolean run(SWCommandSender sender, String[] args) {
        if (args.length == 0) {
            sender.sendMessage(plugin.getUtils().colorize("&cPlease enter a game template name."));
            return false; // todo not sure if I should return false/true here to show the usage too or not.
        }

        final String templateName = args[0];
        GameTemplate template = plugin.getGameManager().getGameTemplateByName(templateName);
        if (template == null) {
            sender.sendMessage(plugin.getUtils().colorize("&cThere is no game template with that name."));
            return true;
        }
        final SWPlayer player = (SWPlayer) sender;

        List<GameWorld> worlds = plugin.getGameManager().getGameWorlds(template);
        for (GameWorld world : worlds) {
            if (world.isEditing()) {
                player.sendMessage(plugin.getUtils().colorize("&7Teleporting you to the current existing map template to edit..."));
                world.readyForEditing();
                player.teleport(world.getWorldName(), 0, 51, 0);
                return true;
            } else if (world.getStatus() != GameStatus.DISABLED) {
                player.sendMessage(plugin.getUtils().colorize("&cIt seems like there is a game running for the current game template. Please stop the all its games before editing the template."));
                return true;
            }
        }

        sender.sendMessage(plugin.getUtils().colorize("&7Please hold while we generate the template world..."));
        player.sendTitle(plugin.getUtils().colorize("&6Generating World..."), plugin.getUtils().colorize("&7Please hold while we generate the template world"), 20, 20 * 30, 20);
        GameWorld world = plugin.getGameManager().createGameWorld(template);
        world.setEditing(true);

        // Create instance of the world given the template data, or create a new one if it doesn't exist.
        boolean templateExists;
        try {
            templateExists = plugin.getWorldLoader().generateWorldInstance(world);
        } catch (IllegalArgumentException | IllegalStateException e) {
            sender.sendMessage(plugin.getUtils().colorize("&cAn error occurred while generating the world, please check the server console for details."));
            return true;
        }

        // Handle the initialization of a world if this was the creation of the template
        if (!templateExists) {
            plugin.getWorldLoader().createBasePlatform(world);
        }

        world.readyForEditing();

        player.teleport(world.getWorldName(),
                InternalProperties.MAP_CREATE_PLATFORM_X,
                InternalProperties.MAP_CREATE_PLATFORM_Y + 1,
                InternalProperties.MAP_CREATE_PLATFORM_Z);
        player.sendTitle(plugin.getUtils().colorize("&aGenerated World!"), plugin.getUtils().colorize("&7We completed generating the template world"), 0, 100, 20);
        sender.sendMessage(plugin.getUtils().colorize("&aWe finished generating the template world! &7You can now start building within the world borders. We will automatically convert the world to a schematic file when you're done."));
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
