package net.gcnt.skywarsreloaded.game.state;

import net.gcnt.skywarsreloaded.SkyWarsReloaded;
import net.gcnt.skywarsreloaded.game.GamePlayer;
import net.gcnt.skywarsreloaded.game.gameinstance.GameInstance;

public class EndingStateHandler extends CoreGameStateHandler {

    public EndingStateHandler(SkyWarsReloaded plugin, GameInstance gameWorld) {
        super(plugin, gameWorld);
    }

    @Override
    public void tickSecond() {
        if (gameInstance.getTimer() == 0) {
            plugin.getGameInstanceManager().deleteGameWorld(gameInstance);
            return;
        }

        gameInstance.setTimer(gameInstance.getTimer() - 1);

        for (GamePlayer player : gameInstance.getWaitingPlayers()) {
            player.getSWPlayer().setExp(gameInstance.getTimer(), 0);
        }

        plugin.getScoreboardManager().updateAllPlayers(gameInstance);
    }
}
