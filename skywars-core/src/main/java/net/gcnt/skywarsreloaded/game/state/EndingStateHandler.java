package net.gcnt.skywarsreloaded.game.state;

import net.gcnt.skywarsreloaded.SkyWarsReloaded;
import net.gcnt.skywarsreloaded.game.GamePlayer;
import net.gcnt.skywarsreloaded.game.gameinstance.LocalGameInstance;

public class EndingStateHandler extends CoreGameStateHandler {

    public EndingStateHandler(SkyWarsReloaded plugin, LocalGameInstance gameWorld) {
        super(plugin, gameWorld);
    }

    @Override
    public void tickSecond() {
        if (gameInstance.getTimer() == 0) {
            plugin.getGameInstanceManager().deleteGameInstance(gameInstance);
            return;
        }

        gameInstance.setTimer(gameInstance.getTimer() - 1);

        for (GamePlayer player : gameInstance.getWaitingPlayers()) {
            player.getSWPlayer().setExp(gameInstance.getTimer(), 0);
        }

        plugin.getScoreboardManager().updateAllPlayers(gameInstance);
    }
}
