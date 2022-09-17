package net.gcnt.skywarsreloaded.game.state;

import net.gcnt.skywarsreloaded.SkyWarsReloaded;
import net.gcnt.skywarsreloaded.game.GameInstance;
import net.gcnt.skywarsreloaded.game.GamePlayer;

public class EndingStateHandler extends CoreGameStateHandler {

    public EndingStateHandler(SkyWarsReloaded plugin, GameInstance gameWorld) {
        super(plugin, gameWorld);
    }

    @Override
    public void tickSecond() {
        if (gameInstance.getTimer() == 0) {
            plugin.getGameManager().deleteGameWorld(gameInstance);
            return;
        }

        gameInstance.setTimer(gameInstance.getTimer() - 1);

        for (GamePlayer player : gameInstance.getWaitingPlayers()) {
            player.getSWPlayer().setExp(gameInstance.getTimer(), 0);
        }

        plugin.getScoreboardManager().updateAllPlayers(gameInstance);
    }
}
