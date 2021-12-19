package net.gcnt.skywarsreloaded.command.general;

import net.gcnt.skywarsreloaded.SkyWarsReloaded;
import net.gcnt.skywarsreloaded.command.Cmd;
import net.gcnt.skywarsreloaded.game.GameWorld;
import net.gcnt.skywarsreloaded.utils.SWCoord;
import net.gcnt.skywarsreloaded.utils.properties.MessageProperties;
import net.gcnt.skywarsreloaded.wrapper.player.SWPlayer;
import net.gcnt.skywarsreloaded.wrapper.sender.SWCommandSender;

import java.util.ArrayList;
import java.util.List;

public class LobbySpawnCmd extends Cmd {

    public LobbySpawnCmd(SkyWarsReloaded plugin) {
        super(plugin, "skywars", "setlobby", "skywars.command.setlobby", true, "", "Set the lobby spawn.", "setspawn");
    }

    @Override
    public boolean run(SWCommandSender sender, String[] args) {
        SWPlayer player = (SWPlayer) sender;
        SWCoord loc = player.getLocation();

        if (loc.world() == null) {
            plugin.getMessages().getMessage(MessageProperties.ERROR_WORLD_NOT_RECOGNIZED.toString()).send(sender);
            return true;
        }
        for (GameWorld gw : plugin.getGameManager().getGameWorlds()) {
            if (gw.getWorldName().equals(loc.world().getName())) {
                plugin.getMessages().getMessage(MessageProperties.ERROR_CANNOT_SET_LOBBYSPAWN_IN_GAMEWORLD.toString()).send(sender);
                return true;
            }
        }

        plugin.getDataConfig().set("lobby", loc.toString());
        plugin.getDataConfig().save();
        plugin.getMessages().getMessage(MessageProperties.CHAT_LOBBY_SPAWN_SET.toString()).send(sender);
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
