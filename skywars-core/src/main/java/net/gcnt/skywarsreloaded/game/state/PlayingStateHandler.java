package net.gcnt.skywarsreloaded.game.state;

import net.gcnt.skywarsreloaded.SkyWarsReloaded;
import net.gcnt.skywarsreloaded.game.GamePlayer;
import net.gcnt.skywarsreloaded.game.GameTeam;
import net.gcnt.skywarsreloaded.game.GameWorld;

import java.util.List;

public class PlayingStateHandler extends CoreGameStateHandler {

    public PlayingStateHandler(SkyWarsReloaded plugin, GameWorld gameWorld) {
        super(plugin, gameWorld);
    }

    @Override
    public void tickSecond() {
        List<GameTeam> aliveTeams = gameWorld.getAliveTeams();

        if (aliveTeams.size() == 1) {
            // only one team left OR timer has run out.
            // todo determine the winner and end the game here.
            gameWorld.endGame();
            plugin.getScoreboardManager().updateGame(gameWorld);
            return;
        } else if (aliveTeams.size() == 0) {
            // no more players left.
            // todo end the game here.
            gameWorld.endGame();
            plugin.getScoreboardManager().updateGame(gameWorld);
            return;
        }

        // game is regularly ticking - more than one team is alive.
        gameWorld.setTimer(gameWorld.getTimer() + 1);

        // todo remove this EXP setting.
        for (GamePlayer player : gameWorld.getWaitingPlayers()) {
            player.getSWPlayer().setExp(gameWorld.getTimer(), 0);
        }

        plugin.getScoreboardManager().updateGame(gameWorld);
    }
}
