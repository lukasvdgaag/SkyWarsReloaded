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
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class ModifierOption extends GameOption {
    public ModifierOption(GameMap gameMap, String key) {
        itemList = Lists.newArrayList("modifierrandom", "modifierspeed", "modifierjump", "modifierstrength", "modifiernone");
        voteList = Lists.newArrayList(Vote.MODIFIERRANDOM, Vote.MODIFIERSPEED, Vote.MODIFIERJUMP, Vote.MODIFIERSTRENGTH, Vote.MODIFIERNONE);
        createMenu(key, new Messaging.MessageFormatter().format("menu.modifier-voting-menu"));
        this.gameMap = gameMap;
    }

    protected void doSlotNine(Player player) {
        Vote cVote = Vote.MODIFIERRANDOM;
        String type = new Messaging.MessageFormatter().format("items.modifier-random");
        finishEvent(player, cVote, type);
    }

    protected void doSlotEleven(Player player) {
        Vote cVote = Vote.MODIFIERSPEED;
        String type = new Messaging.MessageFormatter().format("items.modifier-speed");
        finishEvent(player, cVote, type);
    }

    protected void doSlotThriteen(Player player) {
        Vote cVote = Vote.MODIFIERJUMP;
        String type = new Messaging.MessageFormatter().format("items.modifier-jump");
        finishEvent(player, cVote, type);
    }

    protected void doSlotFifteen(Player player) {
        Vote cVote = Vote.MODIFIERSTRENGTH;
        String type = new Messaging.MessageFormatter().format("items.modifier-strength");
        finishEvent(player, cVote, type);
    }

    protected void doSlotSeventeen(Player player) {
        Vote cVote = Vote.MODIFIERNONE;
        String type = new Messaging.MessageFormatter().format("items.modifier-none");
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
                    .setVariable("mod", type)
                    .setVariable("votes", votes + "").format("game.votemodifier"));
        }
    }

    public void setCard(PlayerCard pCard, Vote vote) {
        pCard.setModifier(vote);
    }

    public Vote getVote(PlayerCard pCard) {
        return pCard.getVote("modifier");
    }

    public Vote getRandomVote() {
        return Vote.getRandom("modifier");
    }

    protected void updateScoreboard() {
        gameMap.setCurrentModifier(getVoteString(getVoted()));
        gameMap.getGameBoard().updateScoreboardVar(ScoreVar.MODIFIERVOTE);
    }

    protected Vote getDefault() {
        return gameMap.getDefaultModifier();
    }

    public void completeOption() {
        Vote modifier = gameMap.getModifierOption().getVoted();
        if (modifier == Vote.MODIFIERSPEED) {
            PotionEffect effect = new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, SkyWarsReloaded.getCfg().getSpeed(), true, false);
            gameMap.getAlivePlayers().forEach(player -> player.addPotionEffect(effect));
        } else if (modifier == Vote.MODIFIERJUMP) {
            PotionEffectType effectType = SkyWarsReloaded.getNMS().getPotionEffectTypeByName("jump_boost", "jump");

            PotionEffect effect = new PotionEffect(effectType, Integer.MAX_VALUE, SkyWarsReloaded.getCfg().getJump(), true, false);
            gameMap.getAlivePlayers().forEach(player -> player.addPotionEffect(effect));
        } else if (modifier == Vote.MODIFIERSTRENGTH) {
            PotionEffectType effectType = SkyWarsReloaded.getNMS().getPotionEffectTypeByName("strength", "increase_damage");

            PotionEffect effect = new PotionEffect(effectType, Integer.MAX_VALUE, SkyWarsReloaded.getCfg().getStrength(), true, false);
            gameMap.getAlivePlayers().forEach(player -> player.addPotionEffect(effect));
        }

        if (SkyWarsReloaded.getCfg().isModifierVoteEnabled()) {
            String subOptionName = modifier.name().toLowerCase().replace("modifier", "modifier-");
            String optionValue = SkyWarsReloaded.getMessaging().getMessage("items." + subOptionName);

            MatchManager.get().message(
                    gameMap,
                    new Messaging.MessageFormatter().setVariable(
                            "type",
                            optionValue
                    ).format("game.vote-announcements.modifier"));
        }
    }
}
