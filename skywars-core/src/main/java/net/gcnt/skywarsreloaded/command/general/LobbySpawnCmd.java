package net.gcnt.skywarsreloaded.command.general;

import net.gcnt.skywarsreloaded.SkyWarsReloaded;
import net.gcnt.skywarsreloaded.command.Cmd;
import net.gcnt.skywarsreloaded.enums.SWServerType;
import net.gcnt.skywarsreloaded.game.gameinstance.LocalGameInstance;
import net.gcnt.skywarsreloaded.manager.gameinstance.LocalGameInstanceManager;
import net.gcnt.skywarsreloaded.utils.SWCoord;
import net.gcnt.skywarsreloaded.utils.properties.ConfigProperties;
import net.gcnt.skywarsreloaded.utils.properties.MessageProperties;
import net.gcnt.skywarsreloaded.utils.properties.RuntimeDataProperties;
import net.gcnt.skywarsreloaded.wrapper.entity.SWPlayer;
import net.gcnt.skywarsreloaded.wrapper.sender.SWCommandSender;

import java.util.ArrayList;
import java.util.List;

public class LobbySpawnCmd extends Cmd {

    public LobbySpawnCmd(SkyWarsReloaded plugin) {
        super(plugin, "skywars", "setlobby", "skywars.command.setlobby", true, "", "Set the lobby spawn.", "setspawn");
    }

    @Override
    public boolean run(SWCommandSender sender, String[] args) {
        if ((plugin.getConfig().getBoolean(ConfigProperties.PROXY_ENABLED.toString()) &&
                plugin.getConfig().getEnum(SWServerType.class, ConfigProperties.PROXY_SERVER_TYPE.toString()) == SWServerType.GAME)) {
            // server is on proxy mode and is set to be a game server, disallow this command.
            plugin.getMessages().getMessage(MessageProperties.ERROR_ACTION_NOT_FOR_GAME_SERVERS.toString()).send(sender);
            return true;
        }

        SWPlayer player = (SWPlayer) sender;
        SWCoord loc = player.getLocation();

        if (loc.getWorld() == null) {
            plugin.getMessages().getMessage(MessageProperties.ERROR_WORLD_NOT_RECOGNIZED.toString()).send(sender);
            return true;
        }

        if (!plugin.getGameInstanceManager().isManagerRemote()) {
            LocalGameInstanceManager gameInstanceManager = (LocalGameInstanceManager) plugin.getGameInstanceManager();
            for (LocalGameInstance gw : gameInstanceManager.getGameInstancesList()) {
                if (gw.getWorldName().equals(loc.getWorld().getName())) {
                    plugin.getMessages().getMessage(MessageProperties.ERROR_CANNOT_SET_LOBBYSPAWN_IN_GAMEWORLD.toString()).send(sender);
                    return true;
                }
            }
        }

        plugin.getDataConfig().set(RuntimeDataProperties.LOBBY_SPAWN.toString(), loc.toString());
        plugin.getDataConfig().save();
        plugin.getMessages().getMessage(MessageProperties.CHAT_LOBBY_SPAWN_SET.toString()).send(sender);
        return true;
    }

    @Override
    public List<String> onTabCompletion(SWCommandSender sender, String[] args) {
        if (args.length == 1) {
            List<String> maps = new ArrayList<>();
            plugin.getGameInstanceManager().getGameTemplatesCopy().forEach(template -> maps.add(template.getName()));
            return maps;
        }
        return new ArrayList<>();
    }
}
