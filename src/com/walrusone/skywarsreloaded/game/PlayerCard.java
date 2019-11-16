package com.walrusone.skywarsreloaded.game;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import com.walrusone.skywarsreloaded.enums.Vote;
import com.walrusone.skywarsreloaded.menus.gameoptions.objects.GameKit;

public class PlayerCard {

    private UUID uuid;
    private TeamCard tCard;
    private int preElo;
    private int postElo;
    private int eloChange;

    private GameKit kitVote;
    private Vote gameTime;
    private Vote weather;
    private Vote chestVote;
    private Vote modifier;
    private Vote health;

    public PlayerCard(TeamCard tCard, UUID uuid, int BeginningElo) {
        this.uuid = uuid;
        this.preElo = BeginningElo;
        this.tCard = tCard;
        this.kitVote = null;
        this.gameTime = null;
        this.weather = null;
        this.chestVote = null;
        this.modifier = null;
        this.health = null;
    }

    public void reset() {
        this.preElo = -1;
        this.postElo = 0;
        this.eloChange = 0;
        this.kitVote = null;
        this.gameTime = null;
        this.weather = null;
        this.chestVote = null;
        this.modifier = null;
        this.uuid = null;
    }

    public void setPlayer (Player player) {
        this.uuid = player.getUniqueId();
    }

    public Player getPlayer() {
        if (uuid == null) {
            return null;
        }
        return Bukkit.getPlayer(uuid);
    }

    void setPreElo(int x) {
        this.preElo = x;
    }

    int getPreElo() {
        return this.preElo;
    }

    void setKitVote(GameKit kitVote) {
        this.kitVote = kitVote;
    }

    public GameKit getKitVote() {
        return this.kitVote;
    }

    public void setGameTime(Vote gameTime) {
        this.gameTime = gameTime;
    }

    public void setHealth(Vote health) {
        this.health = health;
    }

    public Vote getVote(String type) {
        if (type.equalsIgnoreCase("time")) {
            return this.gameTime;
        } else if (type.equalsIgnoreCase("chest")) {
            return this.chestVote;
        } else if (type.equalsIgnoreCase("weather")) {
            return this.weather;
        } else if (type.equalsIgnoreCase("modifier")) {
            return this.modifier;
        } else if (type.equalsIgnoreCase("health")) {
            return this.health;
        }
        return null;
    }

    public void setWeather(Vote weather) {
        this.weather = weather;
    }

    public void setChestVote(Vote chestVote) {
        this.chestVote = chestVote;
    }

    public void setModifier(Vote modifier) {
        this.modifier = modifier;
    }

    public int getPostElo() {
        return this.postElo;
    }

    public int getEloChange() {
        return this.eloChange;
    }

    public void calculateELO() {
        int n = tCard.getGameMap().getPlayerCount();
        float K = 32 / (float)(n - 1);

        int curPlace = tCard.getPlace();
        int curELO   = preElo;

        for (TeamCard tCard: tCard.getGameMap().getTeamCards()) {
            PlayerCard opponent = tCard.getPlayerCards().get(0);
            if (opponent.getUUID() != null && !opponent.getUUID().equals(uuid)) {
                int opponentPlace = opponent.tCard.getPlace();
                int opponentELO   = opponent.getPreElo();

                float S;
                if (curPlace < opponentPlace)
                    S = 1.0F;
                else if (curPlace == opponentPlace)
                    S = 0.5F;
                else
                    S = 0.0F;

                float EA = 1 / (1.0f + (float)Math.pow(10.0f, (opponentELO - curELO) / 400.0f));

                this.eloChange += Math.round(K * (S - EA));
            }
        }

        this.postElo = this.preElo + this.eloChange;
    }

    public UUID getUUID() {
        return uuid;
    }

    public TeamCard getTeamCard() {
        return tCard;
    }
}