package net.gcnt.skywarsreloaded.game.state;

import net.gcnt.skywarsreloaded.SkyWarsReloaded;
import net.gcnt.skywarsreloaded.game.GameInstance;
import net.gcnt.skywarsreloaded.game.GamePlayer;
import net.gcnt.skywarsreloaded.game.GameTeam;

import java.util.List;

public class PlayingStateHandler extends CoreGameStateHandler {

    public PlayingStateHandler(SkyWarsReloaded plugin, GameInstance gameWorld) {
        super(plugin, gameWorld);
    }

    @Override
    public void tickSecond() {
        List<GameTeam> aliveTeams = gameWorld.getAliveTeams();

        if (aliveTeams.size() == 1) {
            // only one team left OR timer has run out.
            // todo determine the winner and end the game here.
            gameWorld.endGame();
            plugin.getScoreboardManager().updateAllPlayers(gameWorld);
            return;
        } else if (aliveTeams.size() == 0) {
            // no more players left.
            // todo end the game here.
            gameWorld.endGame();
            plugin.getScoreboardManager().updateAllPlayers(gameWorld);
            return;
        }

        // game is regularly ticking - more than one team is alive.
        gameWorld.setTimer(gameWorld.getTimer() + 1);

        // todo remove this EXP setting.
        for (GamePlayer player : gameWorld.getWaitingPlayers()) {
            player.getSWPlayer().setExp(gameWorld.getTimer(), 0);
        }

        plugin.getScoreboardManager().updateAllPlayers(gameWorld);
    }
}
