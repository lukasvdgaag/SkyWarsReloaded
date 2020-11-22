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
import com.walrusone.skywarsreloaded.menus.gameoptions.objects.CoordLoc;
import com.walrusone.skywarsreloaded.utilities.Messaging;
import com.walrusone.skywarsreloaded.utilities.Util;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.Chest;
import org.bukkit.block.DoubleChest;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;

public class ChestOption extends GameOption {
    public ChestOption(GameMap gameMap, String key) {
        this.gameMap = gameMap;
        itemList = Lists.newArrayList("chestrandom", "chestbasic", "chestnormal", "chestop", "chestscavenger");
        voteList = Lists.newArrayList(Vote.CHESTRANDOM, Vote.CHESTBASIC, Vote.CHESTNORMAL, Vote.CHESTOP, Vote.CHESTSCAVENGER);
        createMenu(key, new Messaging.MessageFormatter().format("menu.chest-voting-menu"));
    }

    protected void doSlotNine(Player player) {
        Vote cVote = Vote.CHESTRANDOM;
        String type = new Messaging.MessageFormatter().format("items.chest-random");
        finishEvent(gameMap, player, cVote, type);
    }

    protected void doSlotEleven(Player player) {
        Vote cVote = Vote.CHESTBASIC;
        String type = new Messaging.MessageFormatter().format("items.chest-basic");
        finishEvent(gameMap, player, cVote, type);
    }

    protected void doSlotThriteen(Player player) {
        Vote cVote = Vote.CHESTNORMAL;
        String type = new Messaging.MessageFormatter().format("items.chest-normal");
        finishEvent(gameMap, player, cVote, type);
    }

    protected void doSlotFifteen(Player player) {
        Vote cVote = Vote.CHESTOP;
        String type = new Messaging.MessageFormatter().format("items.chest-op");
        finishEvent(gameMap, player, cVote, type);
    }

    protected void doSlotSeventeen(Player player) {
        Vote cVote = Vote.CHESTSCAVENGER;
        String type = new Messaging.MessageFormatter().format("items.chest-scavenger");
        finishEvent(gameMap, player, cVote, type);
    }

    private void finishEvent(GameMap gameMap, Player player, Vote vote, String type) {
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
                    .setVariable("chest", type)
                    .setVariable("votes", votes+"").format("game.votechest"));
        }
    }

    public void setCard(PlayerCard pCard, Vote vote) {
        pCard.setChestVote(vote);
    }

    public Vote getVote(PlayerCard pCard) {
        return pCard.getVote("chest");
    }

    public Vote getRandomVote() {
        return Vote.getRandom("chest");
    }

    protected void updateScoreboard() {
        gameMap.setCurrentChest(getVoteString(getVoted()));
        gameMap.getGameBoard().updateScoreboardVar(ScoreVar.CHESTVOTE);
    }

    protected Vote getDefault() {
        return gameMap.getDefaultChestType();
    }


    public void completeOption() {
        Vote cVote = gameMap.getChestOption().getVoted();
        populateChests(gameMap.getChests(), cVote, false);
        populateChests(gameMap.getCenterChests(), cVote, true);
        if (SkyWarsReloaded.getCfg().isChestVoteEnabled() && gameMap.getTimer() < 5) {
            MatchManager.get().message(gameMap, new Messaging.MessageFormatter().setVariable("type", cVote.name().toLowerCase().replace("chest", "")).format("game.vote-announcements.chests"));
        }
    }

    private void populateChests(ArrayList<CoordLoc> chests, Vote cVote, boolean center) {
        org.bukkit.World mapWorld = gameMap.getCurrentWorld();
        for (CoordLoc eChest : chests) {
            int x = eChest.getX();
            int y = eChest.getY();
            int z = eChest.getZ();
            Location loc = new Location(mapWorld, x, y, z);
            if ((loc.getBlock().getState() instanceof Chest)) {
                Chest chest = (Chest) loc.getBlock().getState();
                org.bukkit.inventory.InventoryHolder ih = chest.getInventory().getHolder();
                if ((ih instanceof DoubleChest)) {
                    DoubleChest dc = (DoubleChest) ih;
                    SkyWarsReloaded.getCM().populateChest(dc, cVote, center);
                } else {
                    SkyWarsReloaded.getCM().populateChest(chest, cVote, center);
                }
            }
        }
    }
}
