package net.gcnt.skywarsreloaded.game.state;

import net.gcnt.skywarsreloaded.SkyWarsReloaded;
import net.gcnt.skywarsreloaded.game.GamePlayer;
import net.gcnt.skywarsreloaded.game.GameTeam;
import net.gcnt.skywarsreloaded.game.gameinstance.LocalGameInstance;

import java.util.List;

public class PlayingStateHandler extends CoreGameStateHandler {

    public PlayingStateHandler(SkyWarsReloaded plugin, LocalGameInstance localInstance) {
        super(plugin, localInstance);
    }

    @Override
    public void tickSecond() {
        List<GameTeam> aliveTeams = gameInstance.getAliveTeams();

        if (aliveTeams.size() == 1) {
            // only one team left OR timer has run out.
            // todo determine the winner and end the game here.
            gameInstance.endGame();
            plugin.getScoreboardManager().updateAllPlayers(gameInstance);
            return;
        } else if (aliveTeams.size() == 0) {
            // no more players left.
            // todo end the game here.
            gameInstance.endGame();
            plugin.getScoreboardManager().updateAllPlayers(gameInstance);
            return;
        }

        // game is regularly ticking - more than one team is alive.
        gameInstance.setTimer(gameInstance.getTimer() + 1);

        // todo remove this EXP setting.
        for (GamePlayer player : gameInstance.getWaitingPlayers()) {
            player.getSWPlayer().setExp(gameInstance.getTimer(), 0);
        }

        plugin.getScoreboardManager().updateAllPlayers(gameInstance);
    }
}
