package net.gcnt.skywarsreloaded.command.maps;

import net.gcnt.skywarsreloaded.SkyWarsReloaded;
import net.gcnt.skywarsreloaded.command.Cmd;
import net.gcnt.skywarsreloaded.game.GameTemplate;
import net.gcnt.skywarsreloaded.wrapper.SWCommandSender;

public class CreateMapCmd extends Cmd {

    public CreateMapCmd(SkyWarsReloaded plugin) {
        super(plugin, "skywarsmap", "create", "skywars.command.map.create", true, "<name>", "Create a new map template.");
    }

    @Override
    public boolean run(SWCommandSender sender, String[] args) {
        if (args.length == 0) {
            sender.sendMessage(plugin.getUtils().colorize("&cPlease enter a game template name."));
            return false; // todo not sure if I should return false/true here to show the usage too or not.
        }

        final String templateName = args[0];
        GameTemplate template = plugin.getGameManager().createGameTemplate(templateName);
        if (template == null) {
            sender.sendMessage(plugin.getUtils().colorize("&cThere already is a game template with that name."));
            return true;
        }

        sender.sendMessage(plugin.getUtils().colorize("&aA new game template with the name &e" + templateName + "&a has successfully been created."));
        // todo create a temporary world for the player to set up the arena in.
        return true;
    }
}
