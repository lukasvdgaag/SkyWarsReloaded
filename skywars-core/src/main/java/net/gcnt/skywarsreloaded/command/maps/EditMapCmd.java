package net.gcnt.skywarsreloaded.command.maps;

import net.gcnt.skywarsreloaded.SkyWarsReloaded;
import net.gcnt.skywarsreloaded.command.Cmd;
import net.gcnt.skywarsreloaded.data.config.YAMLConfig;
import net.gcnt.skywarsreloaded.game.GameTemplate;
import net.gcnt.skywarsreloaded.game.chest.SWChestTier;
import net.gcnt.skywarsreloaded.game.gameinstance.LocalGameInstance;
import net.gcnt.skywarsreloaded.game.types.GameState;
import net.gcnt.skywarsreloaded.utils.properties.InternalProperties;
import net.gcnt.skywarsreloaded.utils.properties.MessageProperties;
import net.gcnt.skywarsreloaded.utils.properties.RuntimeDataProperties;
import net.gcnt.skywarsreloaded.wrapper.entity.SWPlayer;
import net.gcnt.skywarsreloaded.wrapper.sender.SWCommandSender;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class EditMapCmd extends Cmd {

    public EditMapCmd(SkyWarsReloaded plugin) {
        super(plugin, "skywarsmap", "edit", "skywars.command.map.edit",
                true, "<name>", "Edit a map template.", "e");
    }

    @Override
    public boolean run(SWCommandSender sender, String[] args) {
        // Utils
        // todo make edit map command do a request to a server to host a map editing session somewhere if it's on proxy.
        YAMLConfig msgConfig = plugin.getMessages();

        if (args.length == 0) {
            msgConfig.getMessage(MessageProperties.MAPS_ENTER_NAME.toString()).send(sender);
            return true;
        }

        if (plugin.getDataConfig().getCoord(RuntimeDataProperties.LOBBY_SPAWN.toString()) == null) {
            msgConfig.getMessage(MessageProperties.ERROR_LOBBY_SPAWN_NOT_SET.toString()).send(sender);
            return true;
        }

        final String templateName = args[0];
        GameTemplate template = plugin.getGameInstanceManager().getGameTemplateByName(templateName);
        if (template == null) {
            msgConfig.getMessage(MessageProperties.MAPS_DOESNT_EXIST.toString()).send(sender);
            return true;
        }
        final SWPlayer player = (SWPlayer) sender;

        List<LocalGameInstance> instances = (List<LocalGameInstance>) plugin.getGameInstanceManager().getGameInstancesByTemplate(template);
        for (LocalGameInstance instance : instances) {
            if (instance.isEditing()) {
                msgConfig.getMessage(MessageProperties.MAPS_EDIT_EXISTING_WORLD.toString())
                        .replace("%template%", template.getName())
                        .send(sender);
                instance.requestEditSession(); // todo: in progress
                player.teleport(instance.getWorldName(), 0, 51, 0);
                return true;
            } else if (instance.getState() != GameState.DISABLED) {
                msgConfig.getMessage(MessageProperties.ERROR_CANNOT_SET_LOBBYSPAWN_IN_GAMEWORLD.toString())
                        .replace("%template%", template.getName())
                        .send(sender);
                return true;
            }
        }


        msgConfig.getMessage(MessageProperties.MAPS_GENERATING_WORLD.toString())
                .replace("%template%", template.getName())
                .send(sender);
        msgConfig.getMessage(MessageProperties.TITLES_MAPS_GENERATING_WORLD.toString())
                .replace("%template%", template.getName())
                .sendTitle(20, 600, 20, sender);

        CompletableFuture<LocalGameInstance> completableWorld = (CompletableFuture<LocalGameInstance>) plugin.getGameInstanceManager().createGameWorld(template);
        completableWorld.thenAccept(world -> {

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
                return;
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

                world.makeReadyForEditing();

                SWChestTier defaultChestType = plugin.getChestManager().getChestTierByName("normal");
            /*Collection<SWChestType> chests = template.getEnabledChestTypes();
            // todo fix this.
            chests.forEach(System.out::println);
            if (!chests.isEmpty()) {
                for (SWChestType chest : chests) {
                    defaultChestType = chest;
                    break;
                }
            }
            System.out.println("defaultChestType = " + defaultChestType);*/
                world.setChestTypeSelected(player.getUuid(), defaultChestType);

                // Teleport the player onto the platform that was just created
                player.teleportAsync(world.getWorldName(),
                                InternalProperties.MAP_CREATE_PLATFORM_X + 0.5,
                                InternalProperties.MAP_CREATE_PLATFORM_Y + 1,
                                InternalProperties.MAP_CREATE_PLATFORM_Z + 0.5)
                        .thenRun(() -> {
                                    player.setGameMode(1);
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

                                    template.checkToDoList(sender);
                                }
                        );

            });
        });
        return true;
    }

    @Override
    public List<String> onTabCompletion(SWCommandSender sender, String[] args) {
        if (args.length == 1) {
            List<String> maps = new ArrayList<>();
            plugin.getGameInstanceManager().getGameTemplatesCopy().forEach(template -> maps.add(template.getName()));
            return maps;
        }
        return new ArrayList<>();
    }
}
