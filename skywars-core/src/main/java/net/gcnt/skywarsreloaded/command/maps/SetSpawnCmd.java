package net.gcnt.skywarsreloaded.command.maps;

import net.gcnt.skywarsreloaded.SkyWarsReloaded;
import net.gcnt.skywarsreloaded.command.Cmd;
import net.gcnt.skywarsreloaded.game.GameTemplate;
import net.gcnt.skywarsreloaded.game.types.SpawnType;
import net.gcnt.skywarsreloaded.utils.SWCoord;
import net.gcnt.skywarsreloaded.wrapper.player.SWPlayer;
import net.gcnt.skywarsreloaded.wrapper.sender.SWCommandSender;

import java.util.ArrayList;
import java.util.List;

public class SetSpawnCmd extends Cmd {

    public SetSpawnCmd(SkyWarsReloaded plugin) {
        super(plugin, "skywarsmap", "setspawn", "skywars.command.map.setspawn", true, "<map> <type>", "Set a spawnpoint.");
    }

    @Override
    public boolean run(SWCommandSender sender, String[] args) {
        if (args.length == 0) {
            sender.sendMessage(plugin.getUtils().colorize("&cPlease enter a game template name."));
            return true;
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
        // todo: check if arg is number, translate to machine index -1, add error messages.

        SWPlayer player = (SWPlayer) sender;
        final SWCoord location = player.getLocation();

        switch (type) {
            case LOBBY -> {
                template.setWaitingLobbySpawn(location);
                sender.sendMessage(plugin.getUtils().colorize(String.format("&aThe waiting spawn of the template &b%s &ahas been set to your current location.", template.getDisplayName())));
            }
            case SPECTATE -> {
                template.setSpectateSpawn(location);
                sender.sendMessage(plugin.getUtils().colorize(String.format("&aThe spectator spawn of the template &b%s &ahas been set to your current location.", template.getDisplayName())));
            }
            case PLAYER -> {
                // todo PLAYER spawn (see above)
            }
        }

        return true;
    }

    // /swm setspawn player
    // /swm setspawn player <team>

    @Override
    public List<String> onTabCompletion(SWCommandSender sender, String[] args) {
        if (args.length == 1) {
            List<String> maps = new ArrayList<>();
            plugin.getGameManager().getGameTemplates().forEach(template -> maps.add(template.getName()));
            return maps;
        } else if (args.length == 2) {
            List<String> options = new ArrayList<>();
            for (SpawnType type : SpawnType.values()) {
                options.add(type.name().toLowerCase());
            }
            return options;
        }
        return new ArrayList<>();
    }
}
