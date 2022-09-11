package net.gcnt.skywarsreloaded.game.state;

import net.gcnt.skywarsreloaded.SkyWarsReloaded;
import net.gcnt.skywarsreloaded.game.GamePlayer;
import net.gcnt.skywarsreloaded.game.GameTemplate;
import net.gcnt.skywarsreloaded.game.GameWorld;
import net.gcnt.skywarsreloaded.game.types.GameState;
import net.gcnt.skywarsreloaded.utils.properties.ConfigProperties;

public class WaitingStateHandler extends CoreGameStateHandler {

    private final GameState DEFAULT_WAITING_STATE;
    private final int gameFullTimerMaxLobby;
    private final int gameFullTimerMaxCages;

    public WaitingStateHandler(SkyWarsReloaded plugin, GameWorld gameWorld) {
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
                for (GamePlayer player : gameWorld.getWaitingPlayers()) {
                    player.getSWPlayer().sendMessage("§cNot enough players. Teleporting you back to the waiting lobby.");
                    player.getSWPlayer().teleport(gameWorld.getTemplate().getWaitingLobbySpawn());
                }
                break;

            case ANNOUNCE_DROP_TO_FULL_TIMER:
                gameWorld.setTimer(getWaitingFullTimer());
                final String message = "§eThe game is §6§lFULL§e! Starting in §6" + this.gameWorld.getTimer() + "§e seconds.";
                for (GamePlayer player : gameWorld.getWaitingPlayers()) {
                    player.getSWPlayer().sendMessage(message);
                }
                break;

            case COUNTDOWN:
                // when the timer reaches 10, officially start the countdown state.
                if (gameWorld.getState() == GameState.WAITING_CAGES && gameWorld.getTimer() == getWaitingFullTimer()) {
                    gameWorld.setState(GameState.COUNTDOWN);
                }

                gameWorld.setTimer(gameWorld.getTimer() - 1);

                for (GamePlayer player : gameWorld.getWaitingPlayers()) {
                    player.getSWPlayer().setExp(gameWorld.getTimer(), 0);
                }

                announceTimer();
                break;

            case START_GAME:
                // In lobby -> cages state
                if (gameWorld.getState() == GameState.WAITING_LOBBY) {
                    // todo teleport all players to their cages
                }
                // In cages -> playing state
                else if (gameWorld.getState() == GameState.COUNTDOWN) {
                    // todo release the cages
                    gameWorld.startGame();
                }

                plugin.getScoreboardManager().updateAllPlayers(gameWorld);
                break;

            default:
                System.out.println("Failed at life");
                break;
        }
        plugin.getScoreboardManager().updateAllPlayers(gameWorld);
    }

    public WaitingDecision calculateDecision() {
        final int playerCount = gameWorld.getWaitingPlayers().size();

        // Not ready to start yet
        if (playerCount < gameWorld.getTemplate().getMinPlayers()) {
            GameState prevState = gameWorld.getState();
            int prevTimer = gameWorld.getTimer();

            // Reset state
            gameWorld.setState(DEFAULT_WAITING_STATE);

            // Reset time
            this.resetWaitingTime();

            // Move players back to lobby
            if (gameWorld.getState() == GameState.WAITING_LOBBY && prevTimer != gameWorld.getTimer() && prevState != GameState.WAITING_LOBBY) {
                return WaitingDecision.RESET_TO_WAITING_LOBBY;
            }

            return WaitingDecision.NONE;
        }

        // game is full and the timer is higher than the waiting-full timer... shortening the timer
        else if (playerCount == gameWorld.getTemplate().getMaxPlayers()) {
            final int waitingFullTimer = getWaitingFullTimer();

            if (gameWorld.getTimer() > waitingFullTimer) {
                return WaitingDecision.ANNOUNCE_DROP_TO_FULL_TIMER;
            }
        }

        // Normal countdown
        else {
            // Countdown has finished, going to next state of the game.
            if (gameWorld.getTimer() == 0) {
                return WaitingDecision.START_GAME;
            }

            return WaitingDecision.COUNTDOWN;
        }

        return WaitingDecision.NONE;
    }

    private int getWaitingFullTimer() {
        return this.gameWorld.getState() == GameState.WAITING_LOBBY ?
                gameFullTimerMaxLobby :
                gameFullTimerMaxCages;
    }

    public void resetWaitingTime() {
        if (gameWorld.getState() == GameState.WAITING_LOBBY) {
            final int waitingLobbyTime = plugin.getConfig().getInt(ConfigProperties.GAME_TIMER_WAITING_LOBBY.toString());
            gameWorld.setTimer(waitingLobbyTime);

        } else if (gameWorld.getState() == GameState.WAITING_CAGES) {
            gameWorld.setTimer(plugin.getConfig().getInt(ConfigProperties.GAME_TIMER_WAITING_CAGES.toString()));
        }
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

    public enum WaitingDecision {
        NONE,
        START_GAME,
        RESET_TO_WAITING_LOBBY,
        ANNOUNCE_DROP_TO_FULL_TIMER,
        COUNTDOWN
    }
}
