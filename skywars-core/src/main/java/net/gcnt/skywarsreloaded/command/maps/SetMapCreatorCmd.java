package net.gcnt.skywarsreloaded.command.maps;

import net.gcnt.skywarsreloaded.SkyWarsReloaded;
import net.gcnt.skywarsreloaded.command.Cmd;
import net.gcnt.skywarsreloaded.game.GameTemplate;
import net.gcnt.skywarsreloaded.wrapper.SWCommandSender;

import java.util.Arrays;

public class SetMapCreatorCmd extends Cmd {

    public SetMapCreatorCmd(SkyWarsReloaded plugin) {
        super(plugin, "skywarsmap", "creator", "skywars.command.map.creator", true, "<map> <creator>", "Set the map creator.");
    }

    @Override
    public boolean run(SWCommandSender sender, String[] args) {
        if (args.length == 0) {
            sender.sendMessage(plugin.getUtils().colorize("&cPlease enter a game template name."));
            return false; // todo not sure if I should return false/true here to show the usage too or not.
        } else if (args.length == 1) {
            sender.sendMessage(plugin.getUtils().colorize("&cPlease enter the creator of the map."));
            return false; // todo not sure if I should return false/true here to show the usage too or not.
        }

        final String templateName = args[0];
        GameTemplate template = plugin.getGameManager().getGameTemplateByName(templateName);
        if (template == null) {
            sender.sendMessage(plugin.getUtils().colorize("&cThere is no game template with that name."));
            return true;
        }

        args = Arrays.copyOfRange(args, 1, args.length);
        String creator = String.join(" ", args);
        template.setCreator(creator);
        sender.sendMessage(plugin.getUtils().colorize("&aThe creator of the map &e" + templateName + " &ahas been changed to &e" + creator + "&a!"));
        return true;
    }
}
