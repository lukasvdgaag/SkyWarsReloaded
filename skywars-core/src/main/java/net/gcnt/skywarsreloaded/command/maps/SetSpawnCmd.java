package net.gcnt.skywarsreloaded.command.maps;

import net.gcnt.skywarsreloaded.SkyWarsReloaded;
import net.gcnt.skywarsreloaded.command.Cmd;
import net.gcnt.skywarsreloaded.game.GameTemplate;
import net.gcnt.skywarsreloaded.game.SpawnType;
import net.gcnt.skywarsreloaded.wrapper.SWCommandSender;

import java.util.Arrays;
import java.util.stream.Collectors;

public class SetSpawnCmd extends Cmd {

    public SetSpawnCmd(SkyWarsReloaded plugin) {
        super(plugin, "skywarsmap", "setspawn", "skywars.command.map.setspawn", true, "<map> <type>", "Set a spawnpoint.");
    }

    @Override
    public boolean run(SWCommandSender sender, String[] args) {
        if (args.length == 0) {
            sender.sendMessage(plugin.getUtils().colorize("&cPlease enter a game template name."));
            return false; // todo not sure if I should return false/true here to show the usage too or not.
        } else if (args.length == 1) {
            sender.sendMessage(plugin.getUtils().colorize(String.format("&cPlease enter a spawn type. Options: &7%s&f, &7%s&f, &7%s", "player", "lobby", "spectate")));
        }

        final String templateName = args[0];
        GameTemplate template = plugin.getGameManager().getGameTemplateByName(templateName);
        if (template == null) {
            sender.sendMessage(plugin.getUtils().colorize("&cThere is no game template with that name."));
            return true;
        }
        SpawnType type = SpawnType.fromString(args[1]);
        if (type == null) {
            sender.sendMessage(plugin.getUtils().colorize(String.format("&cPlease enter a valid spawn type. Options: &7%s&f, &7%s&f, &7%s", "player", "lobby", "spectate")));
            return true;
        }

        // todo set spawn and send message
        return true;
    }
}
