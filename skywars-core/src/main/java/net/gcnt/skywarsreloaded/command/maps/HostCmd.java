package net.gcnt.skywarsreloaded.command.maps;

import net.gcnt.skywarsreloaded.SkyWarsReloaded;
import net.gcnt.skywarsreloaded.command.Cmd;
import net.gcnt.skywarsreloaded.game.GameTemplate;
import net.gcnt.skywarsreloaded.game.GameWorld;
import net.gcnt.skywarsreloaded.game.types.GameStatus;
import net.gcnt.skywarsreloaded.utils.properties.ConfigProperties;
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
        GameTemplate template = plugin.getGameManager().getGameTemplateByName(templateName);
        if (template == null) {
            plugin.getMessages().getMessage(MessageProperties.MAPS_DOESNT_EXIST.toString()).send(sender);
            return true;
        }
        if (!template.isEnabled()) {
            plugin.getMessages().getMessage(MessageProperties.MAPS_NOT_ENABLED.toString()).send(sender);
            return true;
        }

        GameWorld gameWorld = plugin.getGameManager().createGameWorld(template);
        plugin.getWorldLoader().generateWorldInstance(gameWorld);
        gameWorld.setStatus(template.getTeamSize() >= 2 ? GameStatus.WAITING_LOBBY : GameStatus.WAITING_CAGES); // todo make this configurable for separate cages.
        gameWorld.startScheduler();

        plugin.getMessages().getMessage(MessageProperties.MAPS_HOSTED.toString())
                .replace("%template%", template.getName())
                .replace("%gameworld%", gameWorld.getId())
                .send(sender);
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
