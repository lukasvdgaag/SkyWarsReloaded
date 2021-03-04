package com.walrusone.skywarsreloaded.game;

import com.walrusone.skywarsreloaded.enums.Vote;
import com.walrusone.skywarsreloaded.menus.gameoptions.objects.CoordLoc;
import com.walrusone.skywarsreloaded.menus.gameoptions.objects.GameKit;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.UUID;

public class PlayerCard {

    private UUID uuid;
    private TeamCard tCard;

    private CoordLoc spawn;

    private GameKit kitVote;
    private Vote gameTime;
    private Vote weather;
    private Vote chestVote;
    private Vote modifier;
    private Vote health;

    public PlayerCard(TeamCard tCard, UUID uuid, CoordLoc spawn) {
        this.uuid = uuid;
        this.tCard = tCard;
        this.kitVote = null;
        this.gameTime = null;
        this.weather = null;
        this.chestVote = null;
        this.modifier = null;
        this.health = null;
        this.spawn = spawn;
    }

    public void reset() {
        this.kitVote = null;
        this.gameTime = null;
        this.weather = null;
        this.chestVote = null;
        this.modifier = null;
        this.uuid = null;
    }

    public Player getPlayer() {
        if (uuid == null) {
            return null;
        }
        return Bukkit.getPlayer(uuid);
    }

    public void setPlayer(Player player) {
        if (player != null) {
            this.uuid = player.getUniqueId();
            return;
        }
        this.uuid = null;
    }

    public CoordLoc getSpawn() {
        return this.spawn;
    }

    public void setSpawn(CoordLoc loc) {
        this.spawn = loc;
    }

    public GameKit getKitVote() {
        return this.kitVote;
    }

    void setKitVote(GameKit kitVote) {
        this.kitVote = kitVote;
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

    public UUID getUUID() {
        return uuid;
    }

    public TeamCard getTeamCard() {
        return tCard;
    }
}