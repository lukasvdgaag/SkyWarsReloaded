package com.walrusone.skywarsreloaded.menus.gameoptions;

import com.google.common.collect.Lists;
import com.walrusone.skywarsreloaded.SkyWarsReloaded;
import com.walrusone.skywarsreloaded.enums.MatchState;
import com.walrusone.skywarsreloaded.enums.ScoreVar;
import com.walrusone.skywarsreloaded.enums.Vote;
import com.walrusone.skywarsreloaded.events.SkyWarsVoteEvent;
import com.walrusone.skywarsreloaded.game.GameMap;
import com.walrusone.skywarsreloaded.game.PlayerCard;
import com.walrusone.skywarsreloaded.managers.MatchManager;
import com.walrusone.skywarsreloaded.utilities.Messaging;
import com.walrusone.skywarsreloaded.utilities.Util;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;

public class HealthOption extends GameOption {
    public HealthOption(GameMap gameMap, String key) {
        itemList = Lists.newArrayList("healthrandom", "healthfive", "healthten", "healthfifteen", "healthtwenty");
        voteList = Lists.newArrayList(Vote.HEALTHRANDOM, Vote.HEALTHFIVE, Vote.HEALTHTEN, Vote.HEALTHFIFTEEN, Vote.HEALTHTWENTY);
        createMenu(key, new Messaging.MessageFormatter().format("menu.health-voting-menu"));
        this.gameMap = gameMap;
    }

    protected void doSlotNine(Player player) {
        Vote cVote = Vote.HEALTHRANDOM;
        String type = new Messaging.MessageFormatter().format("items.health-random");
        finishEvent(player, cVote, type);
    }

    protected void doSlotEleven(Player player) {
        Vote cVote = Vote.HEALTHFIVE;
        String type = new Messaging.MessageFormatter().format("items.health-five");
        finishEvent(player, cVote, type);
    }

    protected void doSlotThriteen(Player player) {
        Vote cVote = Vote.HEALTHTEN;
        String type = new Messaging.MessageFormatter().format("items.health-ten");
        finishEvent(player, cVote, type);
    }

    protected void doSlotFifteen(Player player) {
        Vote cVote = Vote.HEALTHFIFTEEN;
        String type = new Messaging.MessageFormatter().format("items.health-fifteen");
        finishEvent(player, cVote, type);
    }

    protected void doSlotSeventeen(Player player) {
        Vote cVote = Vote.HEALTHTWENTY;
        String type = new Messaging.MessageFormatter().format("items.health-twenty");
        finishEvent(player, cVote, type);
    }

    private void finishEvent(Player player, Vote vote, String type) {
        if (vote != null) {
            setVote(player, vote);
            Bukkit.getPluginManager().callEvent(new SkyWarsVoteEvent(player, gameMap, vote));
            updateVotes();
            Util.get().playSound(player, player.getLocation(), SkyWarsReloaded.getCfg().getConfirmeSelctionSound(), 1.0F, 1.0F);
            if (gameMap.getMatchState().equals(MatchState.WAITINGSTART) || gameMap.getMatchState().equals(MatchState.WAITINGLOBBY)) {
                new VotingMenu(player);
            }

            int votes = getVotes(false).getOrDefault(vote, 0);

            MatchManager.get().message(gameMap, new Messaging.MessageFormatter()
                    .setVariable("player", player.getName())
                    .setVariable("health", type)
                    .setVariable("votes", votes+"").format("game.votehealth"));
        }
    }

    public void setCard(PlayerCard pCard, Vote vote) {
        pCard.setHealth(vote);
    }

    public Vote getVote(PlayerCard pCard) {
        return pCard.getVote("health");
    }

    public Vote getRandomVote() {
        return Vote.getRandom("health");
    }

    protected void updateScoreboard() {
        gameMap.setCurrentHealth(getVoteString(getVoted()));
        gameMap.getGameBoard().updateScoreboardVar(ScoreVar.HEALTHVOTE);
    }

    protected Vote getDefault() {
        return gameMap.getDefaultHealth();
    }

    public void completeOption() {
        Vote time = gameMap.getHealthOption().getVoted();
        int t = 10;
        if (time == Vote.HEALTHFIVE) {
            t = 5;
        } else if (time == Vote.HEALTHFIFTEEN) {
            t = 15;
        } else if (time == Vote.HEALTHTWENTY) {
            t = 20;
        }
        for (Player player : gameMap.getAlivePlayers()) {
            SkyWarsReloaded.getNMS().setMaxHealth(player, t * 2);
            player.setHealth(t * 2);
        }

        if (SkyWarsReloaded.getCfg().isHealthVoteEnabled()  && gameMap.getTimer() < 5) {
            MatchManager.get().message(gameMap, new Messaging.MessageFormatter().setVariable("type", time.name().toLowerCase().replace("health", "")).format("game.vote-announcements.health"));
        }
    }
}
