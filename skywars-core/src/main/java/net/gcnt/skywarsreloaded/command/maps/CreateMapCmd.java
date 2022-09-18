package net.gcnt.skywarsreloaded.command.maps;

import net.gcnt.skywarsreloaded.SkyWarsReloaded;
import net.gcnt.skywarsreloaded.command.Cmd;
import net.gcnt.skywarsreloaded.data.config.YAMLConfig;
import net.gcnt.skywarsreloaded.game.GameTemplate;
import net.gcnt.skywarsreloaded.game.gameinstance.GameInstance;
import net.gcnt.skywarsreloaded.utils.properties.InternalProperties;
import net.gcnt.skywarsreloaded.utils.properties.MessageProperties;
import net.gcnt.skywarsreloaded.utils.properties.RuntimeDataProperties;
import net.gcnt.skywarsreloaded.wrapper.entity.SWPlayer;
import net.gcnt.skywarsreloaded.wrapper.sender.SWCommandSender;

import java.util.concurrent.CompletableFuture;

public class CreateMapCmd extends Cmd {

    public CreateMapCmd(SkyWarsReloaded plugin) {
        super(plugin, "skywarsmap", "create", "skywars.command.map.create",
                true, "<name>", "Create a new map template.", "c");
    }

    @Override
    public boolean run(SWCommandSender sender, String[] args) {
        // Utils
        YAMLConfig msgConfig = plugin.getMessages();

        // Sanity checks
        if (args.length == 0) {
            msgConfig.getMessage(MessageProperties.MAPS_ENTER_NAME.toString()).send(sender);
            return true;
        }

        if (plugin.getDataConfig().getCoord(RuntimeDataProperties.LOBBY_SPAWN.toString()) == null) {
            msgConfig.getMessage(MessageProperties.ERROR_LOBBY_SPAWN_NOT_SET.toString()).send(sender);
            return true;
        }

        // Verify that the template exists
        final String templateName = args[0];
        GameTemplate template = plugin.getGameInstanceManager().createGameTemplate(templateName);
        if (template == null) {
            msgConfig.getMessage(MessageProperties.MAPS_ALREADY_EXIST.toString()).send(sender);
            return true;
        }

        final SWPlayer player = (SWPlayer) sender;

        // User progress feedback
        msgConfig.getMessage(MessageProperties.MAPS_TEMPLATE_CREATED.toString())
                .replace("%template%", template.getName())
                .send(sender);
        msgConfig.getMessage(MessageProperties.MAPS_GENERATING_WORLD.toString())
                .replace("%template%", template.getName())
                .send(sender);
        msgConfig.getMessage(MessageProperties.TITLES_MAPS_GENERATING_WORLD.toString())
                .replace("%template%", template.getName())
                .sendTitle(20, 600, 20, sender);

        // Generate game instance to use as template editor
        GameInstance world = plugin.getGameInstanceManager().createGameWorld(template);
        world.setEditing(true);

        // Create instance of the world given the template data, or create a new one if it doesn't exist.
        CompletableFuture<Boolean> templateExistsFuture;
        long preGenMillis;
        long timeDiffCreation;
        try {
            preGenMillis = System.currentTimeMillis();
            templateExistsFuture = plugin.getWorldLoader().generateWorldInstance(world);
            timeDiffCreation = System.currentTimeMillis() - preGenMillis;
        } catch (IllegalArgumentException | IllegalStateException e) {
            plugin.getLogger().error(String.format("Failed to edit template %s. (%s)",
                    template.getName(), e.getClass().getName() + ": " + e.getLocalizedMessage()));
            e.printStackTrace();
            msgConfig.getMessage(MessageProperties.MAPS_GENERATING_WORLD_FAIL.toString())
                    .replace("%template%", template.getName())
                    .send(sender);
            return true;
        }

        plugin.getMessages().getMessage(MessageProperties.MAPS_GENERATING_WORLD_STAGE2.toString())
                .replace("%template%", template.getName())
                .replace("%millis%", String.valueOf(timeDiffCreation))
                .send(sender);

        templateExistsFuture.thenAccept(templateExists -> {
            // Handle the initialization of a world if this was the creation of the template
            if (!templateExists) {
                plugin.getWorldLoader().createBasePlatform(world);
            }

            world.readyForEditing();

            // Teleport the player onto the platform that was just created
            player.teleportAsync(world.getWorldName(),
                            InternalProperties.MAP_CREATE_PLATFORM_X + 0.5,
                            InternalProperties.MAP_CREATE_PLATFORM_Y + 1,
                            InternalProperties.MAP_CREATE_PLATFORM_Z + 0.5)
                    .thenRun(() -> {
                                // User feedback
                                // Not going to use more than 24 days =P
                                int timeDiffChunkLoad = (int) (System.currentTimeMillis() - preGenMillis);
                                int timeDiffLoadSec = timeDiffChunkLoad / 1000;
                                int timeDiffLoadDecimal = timeDiffChunkLoad / 100 - timeDiffLoadSec * 10;

                                plugin.getMessages().getMessage(MessageProperties.TITLES_MAPS_GENERATED_WORLD.toString())
                                        .replace("%template%", template.getName())
                                        .sendTitle(0, 100, 0, sender);
                                plugin.getMessages().getMessage(MessageProperties.MAPS_GENERATED_WORLD.toString())
                                        .replace("%template%", template.getName())
                                        .replace("%seconds%", timeDiffLoadSec + "." + timeDiffLoadDecimal)
                                        .send(sender);
                            }
                    );
        });

        return true;
    }
}
