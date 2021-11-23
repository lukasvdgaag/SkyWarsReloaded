package net.gcnt.skywarsreloaded.command.maps;

import net.gcnt.skywarsreloaded.SkyWarsReloaded;
import net.gcnt.skywarsreloaded.command.Cmd;
import net.gcnt.skywarsreloaded.game.GameTemplate;
import net.gcnt.skywarsreloaded.game.GameWorld;
import net.gcnt.skywarsreloaded.utils.properties.InternalProperties;
import net.gcnt.skywarsreloaded.wrapper.player.SWPlayer;
import net.gcnt.skywarsreloaded.wrapper.sender.SWCommandSender;

public class CreateMapCmd extends Cmd {

    public CreateMapCmd(SkyWarsReloaded plugin) {
        super(plugin, "skywarsmap", "create", "skywars.command.map.create", true, "<name>", "Create a new map template.");
    }

    @Override
    public boolean run(SWCommandSender sender, String[] args) {
        // Sanity checks
        if (args.length == 0) {
            sender.sendMessage(plugin.getUtils().colorize("&cPlease enter a game template name."));
            return true;
        }

        final SWPlayer player = (SWPlayer) sender;

        // Verify that the template exists
        final String templateName = args[0];
        GameTemplate template = plugin.getGameManager().createGameTemplate(templateName);
        if (template == null) {
            sender.sendMessage(plugin.getUtils().colorize("&cThere already is a game template with that name."));
            return true;
        }

        // User progress feedback
        sender.sendMessage(plugin.getUtils().colorize("&aA new game template with the name &e" + templateName + "&a has successfully been created."));
        sender.sendMessage(plugin.getUtils().colorize("&7Please hold while we generate the template world..."));
        player.sendTitle(plugin.getUtils().colorize("&6Generating World..."), plugin.getUtils().colorize("&7Please hold while we generate the template world"), 20, Integer.MAX_VALUE, 20);

        // Generate game instance to use as template editor
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
            plugin.getWorldLoader().updateWorldBorder(world);
        }

        // Teleport the player onto the platform that was just created
        player.teleport(world.getWorldName(),
                InternalProperties.MAP_CREATE_PLATFORM_X,
                InternalProperties.MAP_CREATE_PLATFORM_Y + 1,
                InternalProperties.MAP_CREATE_PLATFORM_Z);

        // User feedback
        player.sendTitle(plugin.getUtils().colorize("&aGenerated World!"), plugin.getUtils().colorize("&7We completed generating the template world"), 0, 100, 20);
        sender.sendMessage(plugin.getUtils().colorize("&aWe finished generating the template world! &7You can now start building within the world borders. We will automatically convert the world to a schematic file when you're done."));
        return true;
    }
}
