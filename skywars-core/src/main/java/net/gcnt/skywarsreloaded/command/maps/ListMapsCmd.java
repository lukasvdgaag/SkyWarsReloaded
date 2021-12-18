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
        sender.sendMessage(plugin.getUtils().colorize(plugin.getMessages().getString(MessageProperties.CHAT_HEADER.toString())));
        sender.sendMessage(plugin.getUtils().colorize(String.format("  %-26s %-26s %s", "&e&nTemplate&r", "&7&nCreator&r", "&b&nGame count&r")));
//        sender.sendMessage(plugin.getUtils().colorize("&r  &e&nTemplate&r  &7&nCreator&r  &b&nGame count&r"));

        List<GameTemplate> templates = plugin.getGameManager().getGameTemplates();
        if (templates.isEmpty()) {
            sender.sendMessage(plugin.getUtils().colorize("&cThere are no templates."));
            return true;
        }

        int gameCount;
        for (GameTemplate template : templates) {
            gameCount = plugin.getGameManager().getGameWorlds(template).size();
            sender.sendMessage(plugin.getUtils().colorize("&8- %-22s %-22s %s".formatted("&e" + template.getName(), "&7" + template.getCreator(), "&b" + gameCount)));
        }
        return true;
    }
}
