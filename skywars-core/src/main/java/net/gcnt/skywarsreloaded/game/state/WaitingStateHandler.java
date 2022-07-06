package net.gcnt.skywarsreloaded.game.state;

import net.gcnt.skywarsreloaded.SkyWarsReloaded;
import net.gcnt.skywarsreloaded.game.GamePlayer;
import net.gcnt.skywarsreloaded.game.GameTemplate;
import net.gcnt.skywarsreloaded.game.GameWorld;
import net.gcnt.skywarsreloaded.game.types.GameState;
import net.gcnt.skywarsreloaded.utils.properties.ConfigProperties;

public class WaitingStateHandler extends CoreGameStateHandler {

    public WaitingStateHandler(SkyWarsReloaded plugin, GameWorld gameWorld) {
        super(plugin, gameWorld);
    }

    public GameState getBeginningWaitingState(GameTemplate template) {
        if (template.getTeamSize() > 1 || plugin.getConfig().getBoolean(ConfigProperties.GAME_SOLO_WAITING_LOBBY.toString()))
            return GameState.WAITING_LOBBY;
        else
            return GameState.WAITING_CAGES;
    }

    @Override
    public void tickSecond() {
        final int playerCount = gameWorld.getWaitingPlayers().size();

        final int waitingFullTimer = gameWorld.getState() == GameState.WAITING_LOBBY ?
                plugin.getConfig().getInt(ConfigProperties.GAME_TIMER_WAITING_LOBBY_FULL.toString()) :
                plugin.getConfig().getInt(ConfigProperties.GAME_TIMER_WAITING_CAGES_FULL.toString());

        if (playerCount < gameWorld.getTemplate().getMinPlayers()) {
            GameState oldStatus = gameWorld.getState();
            gameWorld.setState(getBeginningWaitingState(gameWorld.getTemplate()));

            if (gameWorld.getState() == GameState.WAITING_LOBBY) {
                int oldTimer = gameWorld.getTimer();

                final int waitingLobbyTime = plugin.getConfig().getInt(ConfigProperties.GAME_TIMER_WAITING_LOBBY.toString());
                gameWorld.setTimer(waitingLobbyTime);

                if (oldTimer != waitingLobbyTime && oldStatus != GameState.WAITING_LOBBY) {
                    // timer was reset
                    // teleporting all waiting players back to the waiting lobby
                    for (GamePlayer player : gameWorld.getWaitingPlayers()) {
                        player.getSWPlayer().sendMessage("§cNot enough players. Teleporting you back to the waiting lobby.");
                        player.getSWPlayer().teleport(gameWorld.getTemplate().getWaitingLobbySpawn());
                    }
                }

            } else if (gameWorld.getState() == GameState.WAITING_CAGES) {
                gameWorld.setTimer(plugin.getConfig().getInt(ConfigProperties.GAME_TIMER_WAITING_CAGES.toString()));
            }
        } else if (playerCount == gameWorld.getTemplate().getMaxPlayers() && gameWorld.getTimer() > waitingFullTimer) {
            // game is full and the timer is higher than the waiting-full timer... shortening the timer
            gameWorld.setTimer(waitingFullTimer);

            final String message = "§eThe game is §6§lFULL§e! Starting in §6" + waitingFullTimer + "§e seconds.";
            for (GamePlayer player : gameWorld.getWaitingPlayers()) {
                player.getSWPlayer().sendMessage(message);
            }
        } else {
            if (gameWorld.getTimer() == 0) {
                if (gameWorld.getState() == GameState.WAITING_LOBBY) {
                    // todo teleport all players to their cages
                } else if (gameWorld.getState() == GameState.COUNTDOWN) {
                    // todo release the cages
                    startGame();
                }

                plugin.getScoreboardManager().updateGame(gameWorld);
                return;
            }

            // when the timer reaches 10, officially start the countdown state.
            if (gameWorld.getState() == GameState.WAITING_CAGES && gameWorld.getTimer() == waitingFullTimer) {
                gameWorld.setState(GameState.COUNTDOWN);
            }

            gameWorld.setTimer(gameWorld.getTimer() - 1);

            for (GamePlayer player : gameWorld.getWaitingPlayers()) {
                player.getSWPlayer().setExp(gameWorld.getTimer(), 0);
            }

            announceTimer();
        }

        plugin.getScoreboardManager().updateGame(gameWorld);
    }

    private void startGame() {
        gameWorld.setState(GameState.PLAYING);
        gameWorld.fillChests();
        gameWorld.removeCages();
        for (GamePlayer gp : gameWorld.getWaitingPlayers()) {
            gp.setAlive(true);
            gameWorld.preparePlayer(gp.getSWPlayer());
            gp.getSWPlayer().playSound(gp.getSWPlayer().getLocation(), "BLOCK_NOTE_BLOCK_PLING", 1, 2);
            gp.getSWPlayer().sendTitle("§a§lGOOD LUCK", "§eThe game has started!", 20, 50, 20);
            gp.getSWPlayer().sendMessage("§aThe game has started! §eGood luck!");
        }
        gameWorld.getScheduler().setGameStateHandler(new PlayingStateHandler(plugin, gameWorld));
    }

    private void announceTimer() {
        // todo make this configurable
        // At 60
        if (gameWorld.getTimer() == 60) {
            for (GamePlayer player : gameWorld.getWaitingPlayers()) {
                player.getSWPlayer().playSound(player.getSWPlayer().getLocation(), "BLOCK_NOTE_BLOCK_PLING", 0.7f, 1.0f);
                player.getSWPlayer().sendMessage("§eStarting in §a1 minute");
                player.getSWPlayer().sendTitle("§a§l1 minute", "§eStarting soon!", 20, 50, 20);
            }

            // Every 10s (other than 0)
        } else if (gameWorld.getTimer() > 0 && gameWorld.getTimer() % 10 == 0) {
            for (GamePlayer player : gameWorld.getWaitingPlayers()) {
                player.getSWPlayer().playSound(player.getSWPlayer().getLocation(), "BLOCK_NOTE_BLOCK_PLING", 0.7f, 1.0f);
                player.getSWPlayer().sendMessage(String.format("§eStarting in §6%d seconds", gameWorld.getTimer()));
                player.getSWPlayer().sendTitle(String.format("§6§l%d seconds", gameWorld.getTimer()), "§eStarting soon!", 20, 50, 20);
            }

            // Every number under or equal to 5 (other than 0)
        } else if (gameWorld.getTimer() > 0 && gameWorld.getTimer() <= 5) {
            final String formatted = String.format("%d second%s", gameWorld.getTimer(), gameWorld.getTimer() == 1 ? "" : "s");

            for (GamePlayer player : gameWorld.getWaitingPlayers()) {
                player.getSWPlayer().playSound(player.getSWPlayer().getLocation(), "UI_BUTTON_CLICK", 0.7f, 1.0f);
                player.getSWPlayer().sendMessage("§eStarting in §c" + formatted);
                player.getSWPlayer().sendTitle("§c§l" + formatted, "§eStarting soon!", 0, 25, 0);
            }
        }
    }


}
