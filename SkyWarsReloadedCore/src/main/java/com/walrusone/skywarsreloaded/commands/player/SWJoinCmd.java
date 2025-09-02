package com.walrusone.skywarsreloaded.commands.player;

import com.walrusone.skywarsreloaded.SkyWarsReloaded;
import com.walrusone.skywarsreloaded.enums.GameType;
import com.walrusone.skywarsreloaded.managers.MatchManager;
import com.walrusone.skywarsreloaded.utilities.Messaging;
import com.walrusone.skywarsreloaded.utilities.SWRServer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SWJoinCmd extends com.walrusone.skywarsreloaded.commands.BaseCmd {
    public SWJoinCmd(SkyWarsReloaded plugin, String t) {
        super(plugin);
        type = t;
        forcePlayer = true;
        cmdName = "join";
        alias = new String[]{"j"};
        argLength = 1;
    }

    public boolean run(CommandSender sender, Player player, String[] args) {
        if (SkyWarsReloaded.getCfg().bungeeMode()) {
            SWRServer server = SWRServer.getAvailableServer();
            if (server != null) {
                server.setPlayerCount(server.getPlayerCount() + 1);
                server.updateSigns();
                SkyWarsReloaded.get().sendBungeeMsg(player, "Connect", server.getServerName());
            }
        }
        else {
            GameType type = GameType.ALL;
            if (args.length > 1) {
                if ((args[1].equalsIgnoreCase("single")) || (args[1].equalsIgnoreCase("solo"))) {
                    type = GameType.SINGLE;
                } else if (args[1].equalsIgnoreCase("team")) {
                    type = GameType.TEAM;
                }
            }
            boolean joined = MatchManager.get().joinGame(player, type) != null;
            int count = 0;
            while ((count < 4) && (!joined)) {
                joined = MatchManager.get().joinGame(player, type) != null;
                count++;
            }
            if (!joined) {
                player.sendMessage(new Messaging.MessageFormatter().format("error.could-not-join"));
            }
        }
        return true;
    }
}
