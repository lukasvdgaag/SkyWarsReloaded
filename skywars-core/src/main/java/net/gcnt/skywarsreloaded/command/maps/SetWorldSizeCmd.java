package net.gcnt.skywarsreloaded.command.maps;

import net.gcnt.skywarsreloaded.SkyWarsReloaded;
import net.gcnt.skywarsreloaded.command.Cmd;
import net.gcnt.skywarsreloaded.game.GameTemplate;
import net.gcnt.skywarsreloaded.wrapper.sender.SWCommandSender;

import java.util.ArrayList;
import java.util.List;

public class SetWorldSizeCmd extends Cmd {

    public SetWorldSizeCmd(SkyWarsReloaded plugin) {
        super(plugin, "skywarsmap", "worldsize", "skywars.command.map.worldsize", true, "<map> <worldsize>", "Set the world size.");
    }

    @Override
    public boolean run(SWCommandSender sender, String[] args) {
        if (args.length == 0) {
            sender.sendMessage(plugin.getUtils().colorize("&cPlease enter a game template name."));
            return true;
        } else if (args.length == 1) {
            sender.sendMessage(plugin.getUtils().colorize("&cPlease enter the world size."));
            return true;
        }

        if (!plugin.getUtils().isInt(args[1])) {
            sender.sendMessage(plugin.getUtils().colorize("&cPlease enter a valid world size (number)."));
            return false;
        }
        int size = Integer.parseInt(args[1]);
        if (size < 1) {
            sender.sendMessage(plugin.getUtils().colorize("&cPlease enter a valid world size (greater than 0)"));
            return false;
        }

        final String templateName = args[0];
        GameTemplate template = plugin.getGameManager().getGameTemplateByName(templateName);
        if (template == null) {
            sender.sendMessage(plugin.getUtils().colorize("&cThere is no game template with that name."));
            return true;
        }

        template.setBorderRadius(size);
        sender.sendMessage(plugin.getUtils().colorize("&aThe world size of the map &e" + templateName + " &ahas been changed to &e" + size + "&a!"));
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
