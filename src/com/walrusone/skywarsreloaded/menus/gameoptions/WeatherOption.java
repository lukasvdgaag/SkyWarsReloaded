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
import org.bukkit.Chunk;
import org.bukkit.WeatherType;
import org.bukkit.World;
import org.bukkit.block.Biome;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;

public class WeatherOption extends GameOption {
    public WeatherOption(GameMap gameMap, String key) {
        itemList = Lists.newArrayList("weatherrandom", "weathersunny", "weatherrain", "weatherstorm", "weathersnow");
        voteList = Lists.newArrayList(Vote.WEATHERRANDOM, Vote.WEATHERSUN, Vote.WEATHERRAIN, Vote.WEATHERTHUNDER, Vote.WEATHERSNOW);
        createMenu(key, new Messaging.MessageFormatter().format("menu.weather-voting-menu"));
        this.gameMap = gameMap;
    }

    protected void doSlotNine(Player player) {
        Vote cVote = Vote.WEATHERRANDOM;
        String type = new Messaging.MessageFormatter().format("items.weather-random");
        finishEvent(player, cVote, type);
    }

    protected void doSlotEleven(Player player) {
        Vote cVote = Vote.WEATHERSUN;
        String type = new Messaging.MessageFormatter().format("items.weather-sunny");
        finishEvent(player, cVote, type);
    }

    protected void doSlotThriteen(Player player) {
        Vote cVote = Vote.WEATHERRAIN;
        String type = new Messaging.MessageFormatter().format("items.weather-rain");
        finishEvent(player, cVote, type);
    }

    protected void doSlotFifteen(Player player) {
        Vote cVote = Vote.WEATHERTHUNDER;
        String type = new Messaging.MessageFormatter().format("items.weather-storm");
        finishEvent(player, cVote, type);
    }

    protected void doSlotSeventeen(Player player) {
        Vote cVote = Vote.WEATHERSNOW;
        String type = new Messaging.MessageFormatter().format("items.weather-snow");
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
                    .setVariable("weather", type)
                    .setVariable("votes", votes+"")
                    .format("game.voteweather"));
        }
    }

    public void setCard(PlayerCard pCard, Vote vote) {
        pCard.setWeather(vote);
    }

    public Vote getVote(PlayerCard pCard) {
        return pCard.getVote("weather");
    }

    public Vote getRandomVote() {
        return Vote.getRandom("weather");
    }

    protected void updateScoreboard() {
        gameMap.setCurrentWeather(getVoteString(getVoted()));
        gameMap.getGameBoard().updateScoreboardVar(ScoreVar.WEATHERVOTE);
    }

    protected Vote getDefault() {
        return gameMap.getDefaultWeather();
    }


    public void completeOption() {
        Vote weather = gameMap.getWeatherOption().getVoted();
        WeatherType w = WeatherType.CLEAR;
        if (weather != Vote.WEATHERSUN) {
            w = WeatherType.DOWNFALL;
        }
        World world;
        int z;
        if (weather == Vote.WEATHERTHUNDER) {
            gameMap.setThunderStorm(true);
            gameMap.setNextStrike(Util.get().getRandomNum(3, 20));
            gameMap.setStrikeCounter(0);
        } else if (weather == Vote.WEATHERSNOW) {
            world = ((Player) gameMap.getAlivePlayers().get(0)).getWorld();
            for (int x = 65336; x < 200; x++) {
                for (z = 65336; z < 200; z++) {
                    if (SkyWarsReloaded.getNMS().getVersion() < 13) {
                        world.setBiome(x, z, Biome.valueOf("ICE_MOUNTAINS"));
                    } else {
                        world.setBiome(x, z, Biome.valueOf("SNOWY_TUNDRA"));
                    }
                }
            }
            java.util.List<Chunk> chunks = Util.get().getChunks(world);
            for (Chunk chunk : chunks) {
                world.refreshChunk(chunk.getX(), chunk.getZ());
            }
        }

        for (Player player : gameMap.getAllPlayers()) {
            player.setPlayerWeather(w);
        }

        if (SkyWarsReloaded.getCfg().isWeatherVoteEnabled()  && gameMap.getTimer() < 5) {
            MatchManager.get().message(gameMap, new Messaging.MessageFormatter().setVariable("type", weather.name().toLowerCase().replace("weather", "")).format("game.vote-announcements.weather"));
        }
    }
}
