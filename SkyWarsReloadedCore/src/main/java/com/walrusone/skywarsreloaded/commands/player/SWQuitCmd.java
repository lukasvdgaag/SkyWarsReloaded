package com.walrusone.skywarsreloaded.commands.player;

import com.walrusone.skywarsreloaded.SkyWarsReloaded;
import com.walrusone.skywarsreloaded.commands.BaseCmd;
import com.walrusone.skywarsreloaded.enums.PlayerRemoveReason;
import com.walrusone.skywarsreloaded.game.GameMap;
import com.walrusone.skywarsreloaded.managers.MatchManager;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SWQuitCmd extends BaseCmd {
    public SWQuitCmd(String t) {
        type = t;
        forcePlayer = true;
        cmdName = "quit";
        alias = new String[]{"q", "leave", "l"};
        argLength = 1;
    }

    public boolean run(CommandSender sender, Player player, String[] args) {
        GameMap map = MatchManager.get().getPlayerMap(player);
        if (map == null) {
            return true;
        }
        if (map.getTeamCard(player) == null && map.getSpectators().contains(player.getUniqueId())) {
            SkyWarsReloaded.get().getPlayerManager().removePlayer(
                    player, PlayerRemoveReason.PLAYER_QUIT_GAME, null, false);
        }
        else {
            SkyWarsReloaded.get().getPlayerManager().removePlayer(
                    player, PlayerRemoveReason.PLAYER_QUIT_GAME, null, true);
        }
        return true;
    }
}