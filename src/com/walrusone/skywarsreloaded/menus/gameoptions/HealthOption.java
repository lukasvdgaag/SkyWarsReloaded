package com.walrusone.skywarsreloaded.menus.gameoptions;

import com.walrusone.skywarsreloaded.SkyWarsReloaded;
import com.walrusone.skywarsreloaded.enums.MatchState;
import com.walrusone.skywarsreloaded.enums.ScoreVar;
import com.walrusone.skywarsreloaded.enums.Vote;
import com.walrusone.skywarsreloaded.events.SkyWarsVoteEvent;
import com.walrusone.skywarsreloaded.game.GameBoard;
import com.walrusone.skywarsreloaded.game.GameMap;
import com.walrusone.skywarsreloaded.game.PlayerCard;
import com.walrusone.skywarsreloaded.managers.MatchManager;
import com.walrusone.skywarsreloaded.utilities.Messaging;
import com.walrusone.skywarsreloaded.utilities.Messaging.MessageFormatter;
import com.walrusone.skywarsreloaded.utilities.Util;
import java.util.ArrayList;
import java.util.Arrays;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class HealthOption extends GameOption
{
  public HealthOption(GameMap gameMap, String key)
  {
    itemList = new ArrayList(Arrays.asList(new String[] { "healthrandom", "healthfive", "healthten", "healthfifteen", "healthtwenty" }));
    voteList = new ArrayList(Arrays.asList(new Vote[] { Vote.HEALTHRANDOM, Vote.HEALTHFIVE, Vote.HEALTHTEN, Vote.HEALTHFIFTEEN, Vote.HEALTHTWENTY }));
    createMenu(key, new Messaging.MessageFormatter().format("menu.health-voting-menu"));
    this.gameMap = gameMap;
  }
  
  protected void doSlotNine(Player player)
  {
    Vote cVote = Vote.HEALTHRANDOM;
    String type = new Messaging.MessageFormatter().format("items.health-random");
    finishEvent(player, cVote, type);
  }
  
  protected void doSlotEleven(Player player)
  {
    Vote cVote = Vote.HEALTHFIVE;
    String type = new Messaging.MessageFormatter().format("items.health-five");
    finishEvent(player, cVote, type);
  }
  
  protected void doSlotThriteen(Player player)
  {
    Vote cVote = Vote.HEALTHTEN;
    String type = new Messaging.MessageFormatter().format("items.health-ten");
    finishEvent(player, cVote, type);
  }
  
  protected void doSlotFifteen(Player player)
  {
    Vote cVote = Vote.HEALTHFIFTEEN;
    String type = new Messaging.MessageFormatter().format("items.health-fifteen");
    finishEvent(player, cVote, type);
  }
  
  protected void doSlotSeventeen(Player player)
  {
    Vote cVote = Vote.HEALTHTWENTY;
    String type = new Messaging.MessageFormatter().format("items.health-twenty");
    finishEvent(player, cVote, type);
  }
  
  private void finishEvent(Player player, Vote vote, String type) {
    if (vote != null) {
      setVote(player, vote);
        Bukkit.getPluginManager().callEvent(new SkyWarsVoteEvent(player,gameMap,vote));
        updateVotes();
      Util.get().playSound(player, player.getLocation(), SkyWarsReloaded.getCfg().getConfirmeSelctionSound(), 1.0F, 1.0F);
      if (gameMap.getMatchState().equals(MatchState.WAITINGSTART)) {
        new VotingMenu(player);
      }
      MatchManager.get().message(gameMap, new Messaging.MessageFormatter()
        .setVariable("player", player.getName())
        .setVariable("health", type).format("game.votehealth"));
    }
  }
  
  public void setCard(PlayerCard pCard, Vote vote)
  {
    pCard.setHealth(vote);
  }
  
  public Vote getVote(PlayerCard pCard)
  {
    return pCard.getVote("health");
  }
  
  public Vote getRandomVote()
  {
    return Vote.getRandom("health");
  }
  
  protected void updateScoreboard()
  {
    gameMap.setCurrentHealth(getVoteString(getVoted()));
    gameMap.getGameBoard().updateScoreboardVar(ScoreVar.HEALTHVOTE);
  }
  
  protected Vote getDefault()
  {
    return Vote.HEALTHTEN;
  }
  
  public void completeOption()
  {
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
  }
}
