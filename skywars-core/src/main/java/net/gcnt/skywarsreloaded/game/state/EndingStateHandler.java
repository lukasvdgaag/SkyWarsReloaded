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
        if (gameWorld.getTimer() == 0) {
            plugin.getGameManager().deleteGameWorld(gameWorld);
            return;
        }

        gameWorld.setTimer(gameWorld.getTimer() - 1);

        for (GamePlayer player : gameWorld.getWaitingPlayers()) {
            player.getSWPlayer().setExp(gameWorld.getTimer(), 0);
        }

        plugin.getScoreboardManager().updateAllPlayers(gameWorld);
    }
}
