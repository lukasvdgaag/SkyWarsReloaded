package net.gcnt.skywarsreloaded.command.maps;

import net.gcnt.skywarsreloaded.SkyWarsReloaded;
import net.gcnt.skywarsreloaded.command.Cmd;
import net.gcnt.skywarsreloaded.game.GameTemplate;
import net.gcnt.skywarsreloaded.utils.properties.MessageProperties;
import net.gcnt.skywarsreloaded.wrapper.sender.SWCommandSender;

import java.util.List;

public class ListMapsCmd extends Cmd {

    public ListMapsCmd(SkyWarsReloaded plugin) {
        super(plugin, "skywarsmap", "list", "skywars.command.map.list", true, null, "Get a list of all templates.", "l");
    }

    @Override
    public boolean run(SWCommandSender sender, String[] args) {
        plugin.getMessages().getMessage(MessageProperties.CHAT_HEADER.toString()).send(sender);
        plugin.getMessages().getMessage(MessageProperties.MAPS_LIST_HEADER.toString()).send(sender);

        List<GameTemplate> templates = plugin.getGameInstanceManager().getGameTemplates();
        if (templates.isEmpty()) {
            plugin.getMessages().getMessage(MessageProperties.MAPS_LIST_EMPTY.toString()).send(sender);
            return true;
        }

        int gameCount;
        for (GameTemplate template : templates) {
            gameCount = plugin.getGameInstanceManager().getGameWorlds(template).size();
            plugin.getMessages().getMessage(MessageProperties.MAPS_LIST_LINE.toString())
                    .replace("%template%", template.getDisplayName())
                    .replace("%creator%", template.getCreator())
                    .replace("%count%", gameCount + "")
                    .send(sender);
        }
        return true;
    }
}
