package net.gcnt.skywarsreloaded.command.maps;

import net.gcnt.skywarsreloaded.SkyWarsReloaded;
import net.gcnt.skywarsreloaded.command.Cmd;
import net.gcnt.skywarsreloaded.game.GameTemplate;
import net.gcnt.skywarsreloaded.game.GameWorld;
import net.gcnt.skywarsreloaded.utils.properties.InternalProperties;
import net.gcnt.skywarsreloaded.utils.properties.MessageProperties;
import net.gcnt.skywarsreloaded.wrapper.player.SWPlayer;
import net.gcnt.skywarsreloaded.wrapper.sender.SWCommandSender;

import java.util.concurrent.CompletableFuture;

public class CreateMapCmd extends Cmd {

    public CreateMapCmd(SkyWarsReloaded plugin) {
        super(plugin, "skywarsmap", "create", "skywars.command.map.create", true, "<name>", "Create a new map template.", "c");
    }

    @Override
    public boolean run(SWCommandSender sender, String[] args) {
        // Sanity checks
        if (args.length == 0) {
            plugin.getMessages().getMessage(MessageProperties.MAPS_ENTER_NAME.toString()).send(sender);
            return true;
        }

        if (plugin.getDataConfig().getCoord("lobby") == null) {
            plugin.getMessages().getMessage(MessageProperties.ERROR_LOBBY_SPAWN_NOT_SET.toString()).send(sender);
            return true;
        }

        // Verify that the template exists
        final String templateName = args[0];
        GameTemplate template = plugin.getGameManager().createGameTemplate(templateName);
        if (template == null) {
            plugin.getMessages().getMessage(MessageProperties.MAPS_ALREADY_EXIST.toString()).send(sender);
            return true;
        }

        final SWPlayer player = (SWPlayer) sender;

        // User progress feedback
        plugin.getMessages().getMessage(MessageProperties.MAPS_TEMPLATE_CREATED.toString()).replace("%template%", template.getName()).send(sender);
        plugin.getMessages().getMessage(MessageProperties.MAPS_GENERATING_WORLD.toString()).replace("%template%", template.getName()).send(sender);
        plugin.getMessages().getMessage(MessageProperties.TITLES_MAPS_GENERATING_WORLD.toString()).replace("%template%", template.getName()).sendTitle(20, 600, 20, sender);

        // Generate game instance to use as template editor
        GameWorld world = plugin.getGameManager().createGameWorld(template);
        world.setEditing(true);

        // Create instance of the world given the template data, or create a new one if it doesn't exist.
        CompletableFuture<Boolean> templateExistsFuture;
        try {
            templateExistsFuture = plugin.getWorldLoader().generateWorldInstance(world);
        } catch (IllegalArgumentException | IllegalStateException e) {
            plugin.getMessages().getMessage(MessageProperties.MAPS_GENERATING_WORLD_FAIL.toString()).replace("%template%", template.getName()).send(sender);
            return true;
        }

        templateExistsFuture.thenAccept(templateExists -> {
            // Handle the initialization of a world if this was the creation of the template
            if (!templateExists) {
                plugin.getWorldLoader().createBasePlatform(world);
            }

            world.readyForEditing();

            // Teleport the player onto the platform that was just created
            player.teleport(world.getWorldName(),
                    InternalProperties.MAP_CREATE_PLATFORM_X,
                    InternalProperties.MAP_CREATE_PLATFORM_Y + 1,
                    InternalProperties.MAP_CREATE_PLATFORM_Z);

            // User feedback
            plugin.getMessages().getMessage(MessageProperties.TITLES_MAPS_GENERATED_WORLD.toString()).replace("%template%", template.getName()).sendTitle(0, 100, 0, sender);
            plugin.getMessages().getMessage(MessageProperties.MAPS_GENERATED_WORLD.toString()).replace("%template%", template.getName()).send(sender);
        });

        return true;
    }
}
