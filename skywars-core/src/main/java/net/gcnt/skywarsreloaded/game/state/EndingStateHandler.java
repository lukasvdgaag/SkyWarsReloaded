package net.gcnt.skywarsreloaded.game.state;

import net.gcnt.skywarsreloaded.SkyWarsReloaded;
import net.gcnt.skywarsreloaded.game.GamePlayer;
import net.gcnt.skywarsreloaded.game.GameWorld;
import net.gcnt.skywarsreloaded.game.types.GameState;
import net.gcnt.skywarsreloaded.utils.properties.ConfigProperties;

public class EndingStateHandler extends CoreGameStateHandler {

    public EndingStateHandler(SkyWarsReloaded plugin, GameWorld gameWorld) {
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

        plugin.getScoreboardManager().updateGame(gameWorld);
    }
}
