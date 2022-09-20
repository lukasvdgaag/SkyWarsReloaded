package net.gcnt.skywarsreloaded.command.maps;

import net.gcnt.skywarsreloaded.SkyWarsReloaded;
import net.gcnt.skywarsreloaded.command.Cmd;
import net.gcnt.skywarsreloaded.game.GameTemplate;
import net.gcnt.skywarsreloaded.game.gameinstance.GameInstance;
import net.gcnt.skywarsreloaded.utils.properties.MessageProperties;
import net.gcnt.skywarsreloaded.wrapper.sender.SWCommandSender;

import java.util.ArrayList;
import java.util.List;

public class HostCmd extends Cmd {

    public HostCmd(SkyWarsReloaded plugin) {
        super(plugin, "skywarsmap", "host", "skywars.command.map.host", true, "<map>", "Host a game.", "h");
    }

    @Override
    public boolean run(SWCommandSender sender, String[] args) {
        if (args.length == 0) {
            plugin.getMessages().getMessage(MessageProperties.MAPS_ENTER_NAME.toString()).send(sender);
            return true;
        }

        final String templateName = args[0];
        GameTemplate template = plugin.getGameInstanceManager().getGameTemplateByName(templateName);
        if (template == null) {
            plugin.getMessages().getMessage(MessageProperties.MAPS_DOESNT_EXIST.toString()).send(sender);
            return true;
        }
        if (!template.isEnabled()) {
            plugin.getMessages().getMessage(MessageProperties.MAPS_NOT_ENABLED.toString()).send(sender);
            return true;
        }

        sender.sendMessage("Hosting game: " + template.getName());
        sender.sendMessage(plugin.getWorldLoader().getClass().getName());

        GameInstance gameWorld = plugin.getGameInstanceManager().createGameWorld(template);
        // todo move this game instance creation to the game instance manager
        plugin.getWorldLoader().generateWorldInstance(gameWorld).thenAccept((result) -> {
            System.out.println("World generation result: " + result);
            try {
                if (result) {
                    gameWorld.readyForGame();

                    sender.sendMessage("Game world ready for game");
                    plugin.getMessages().getMessage(MessageProperties.MAPS_HOSTED.toString())
                            .replace("%template%", template.getName())
                            .replace("%gameworld%", gameWorld.getId())
                            .send(sender);
                } else {
                    plugin.getLogger().error("Could not create instance!");// todo send error
                    sender.sendMessage("Internal server has occurred!");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        return true;
    }

    @Override
    public List<String> onTabCompletion(SWCommandSender sender, String[] args) {
        if (args.length == 1) {
            List<String> maps = new ArrayList<>();
            plugin.getGameInstanceManager().getGameTemplates().forEach(template -> maps.add(template.getName()));
            return maps;
        }
        return new ArrayList<>();
    }
}
