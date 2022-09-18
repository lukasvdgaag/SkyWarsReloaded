package net.gcnt.skywarsreloaded.game.state;

import net.gcnt.skywarsreloaded.SkyWarsReloaded;
import net.gcnt.skywarsreloaded.game.GamePlayer;
import net.gcnt.skywarsreloaded.game.GameTemplate;
import net.gcnt.skywarsreloaded.game.gameinstance.GameInstance;
import net.gcnt.skywarsreloaded.game.types.GameState;
import net.gcnt.skywarsreloaded.utils.properties.ConfigProperties;

public class WaitingStateHandler extends CoreGameStateHandler {

    private final GameState DEFAULT_WAITING_STATE;
    private final int gameFullTimerMaxLobby;
    private final int gameFullTimerMaxCages;

    public WaitingStateHandler(SkyWarsReloaded plugin, GameInstance gameWorld) {
        super(plugin, gameWorld);
        this.DEFAULT_WAITING_STATE = determineDefaultWaitingState(gameWorld.getTemplate());
        gameFullTimerMaxLobby = plugin.getConfig().getInt(ConfigProperties.GAME_TIMER_WAITING_LOBBY_FULL.toString());
        gameFullTimerMaxCages = plugin.getConfig().getInt(ConfigProperties.GAME_TIMER_WAITING_CAGES_FULL.toString());
    }

    public GameState determineDefaultWaitingState(GameTemplate template) {
        if (template.getTeamSize() > 1 || plugin.getConfig().getBoolean(ConfigProperties.GAME_SOLO_WAITING_LOBBY.toString()))
            return GameState.WAITING_LOBBY;
        else
            return GameState.WAITING_CAGES;
    }

    public GameState getDefaultWaitingState() {
        return DEFAULT_WAITING_STATE;
    }

    @Override
    public void tickSecond() {
        WaitingDecision decision = this.calculateDecision();
        this.handleDecision(decision);
    }

    public void handleDecision(WaitingDecision decision) {
        switch (decision) {
            case RESET_TO_WAITING_LOBBY:
                // timer was reset
                // teleporting all waiting players back to the waiting lobby
                for (GamePlayer player : gameInstance.getWaitingPlayers()) {
                    player.getSWPlayer().sendMessage("§cNot enough players. Teleporting you back to the waiting lobby.");
                    player.getSWPlayer().teleport(gameInstance.getTemplate().getWaitingLobbySpawn());
                }
                break;

            case ANNOUNCE_DROP_TO_FULL_TIMER:
                gameInstance.setTimer(getWaitingFullTimer());
                final String message = "§eThe game is §6§lFULL§e! Starting in §6" + this.gameInstance.getTimer() + "§e seconds.";
                for (GamePlayer player : gameInstance.getWaitingPlayers()) {
                    player.getSWPlayer().sendMessage(message);
                }
                break;

            case COUNTDOWN:
                // when the timer reaches 10, officially start the countdown state.
                if (gameInstance.getState() == GameState.WAITING_CAGES && gameInstance.getTimer() == getWaitingFullTimer()) {
                    gameInstance.setState(GameState.COUNTDOWN);
                }

                gameInstance.setTimer(gameInstance.getTimer() - 1);

                for (GamePlayer player : gameInstance.getWaitingPlayers()) {
                    player.getSWPlayer().setExp(gameInstance.getTimer(), 0);
                }

                announceTimer();
                break;

            case START_GAME:
                // In lobby -> cages state
                if (gameInstance.getState() == GameState.WAITING_LOBBY) {
                    // todo teleport all players to their cages
                }
                // In cages -> playing state
                else if (gameInstance.getState() == GameState.COUNTDOWN) {
                    // todo release the cages
                    gameInstance.startGame();
                }

                plugin.getScoreboardManager().updateAllPlayers(gameInstance);
                break;

            default:
                break;
        }
        plugin.getScoreboardManager().updateAllPlayers(gameInstance);
    }

    public WaitingDecision calculateDecision() {
        final int playerCount = gameInstance.getWaitingPlayers().size();

        // Not ready to start yet
        if (playerCount < gameInstance.getTemplate().getMinPlayers()) {
            GameState prevState = gameInstance.getState();
            int prevTimer = gameInstance.getTimer();

            // Reset state
            gameInstance.setState(DEFAULT_WAITING_STATE);

            // Reset time
            this.resetWaitingTime();

            // Move players back to lobby
            if (gameInstance.getState() == GameState.WAITING_LOBBY && prevTimer != gameInstance.getTimer() && prevState != GameState.WAITING_LOBBY) {
                return WaitingDecision.RESET_TO_WAITING_LOBBY;
            }

            return WaitingDecision.NONE;
        }

        // game is full and the timer is higher than the waiting-full timer... shortening the timer
        else if (playerCount == gameInstance.getTemplate().getMaxPlayers()) {
            final int waitingFullTimer = getWaitingFullTimer();

            if (gameInstance.getTimer() > waitingFullTimer) {
                return WaitingDecision.ANNOUNCE_DROP_TO_FULL_TIMER;
            }
        }

        // Normal countdown
        else {
            // Countdown has finished, going to next state of the game.
            if (gameInstance.getTimer() == 0) {
                return WaitingDecision.START_GAME;
            }

            return WaitingDecision.COUNTDOWN;
        }

        return WaitingDecision.NONE;
    }

    private int getWaitingFullTimer() {
        return this.gameInstance.getState() == GameState.WAITING_LOBBY ?
                gameFullTimerMaxLobby :
                gameFullTimerMaxCages;
    }

    public void resetWaitingTime() {
        if (gameInstance.getState() == GameState.WAITING_LOBBY) {
            final int waitingLobbyTime = plugin.getConfig().getInt(ConfigProperties.GAME_TIMER_WAITING_LOBBY.toString());
            gameInstance.setTimer(waitingLobbyTime);

        } else if (gameInstance.getState() == GameState.WAITING_CAGES) {
            gameInstance.setTimer(plugin.getConfig().getInt(ConfigProperties.GAME_TIMER_WAITING_CAGES.toString()));
        }
    }

    private void announceTimer() {
        // todo make this configurable
        // At 60
        if (gameInstance.getTimer() == 60) {
            for (GamePlayer player : gameInstance.getWaitingPlayers()) {
                player.getSWPlayer().playSound(player.getSWPlayer().getLocation(), "BLOCK_NOTE_BLOCK_PLING", 0.7f, 1.0f);
                player.getSWPlayer().sendMessage("§eStarting in §a1 minute");
                player.getSWPlayer().sendTitle("§a§l1 minute", "§eStarting soon!", 20, 50, 20);
            }

            // Every 10s (other than 0)
        } else if (gameInstance.getTimer() > 0 && gameInstance.getTimer() % 10 == 0) {
            for (GamePlayer player : gameInstance.getWaitingPlayers()) {
                player.getSWPlayer().playSound(player.getSWPlayer().getLocation(), "BLOCK_NOTE_BLOCK_PLING", 0.7f, 1.0f);
                player.getSWPlayer().sendMessage(String.format("§eStarting in §6%d seconds", gameInstance.getTimer()));
                player.getSWPlayer().sendTitle(String.format("§6§l%d seconds", gameInstance.getTimer()), "§eStarting soon!", 20, 50, 20);
            }

            // Every number under or equal to 5 (other than 0)
        } else if (gameInstance.getTimer() > 0 && gameInstance.getTimer() <= 5) {
            final String formatted = String.format("%d second%s", gameInstance.getTimer(), gameInstance.getTimer() == 1 ? "" : "s");

            for (GamePlayer player : gameInstance.getWaitingPlayers()) {
                player.getSWPlayer().playSound(player.getSWPlayer().getLocation(), "UI_BUTTON_CLICK", 0.7f, 1.0f);
                player.getSWPlayer().sendMessage("§eStarting in §c" + formatted);
                player.getSWPlayer().sendTitle("§c§l" + formatted, "§eStarting soon!", 0, 25, 0);
            }
        }
    }

    public enum WaitingDecision {
        NONE,
        START_GAME,
        RESET_TO_WAITING_LOBBY,
        ANNOUNCE_DROP_TO_FULL_TIMER,
        COUNTDOWN
    }
}
