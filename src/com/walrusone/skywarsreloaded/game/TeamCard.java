package com.walrusone.skywarsreloaded.game;

import com.walrusone.skywarsreloaded.SkyWarsReloaded;
import com.walrusone.skywarsreloaded.events.SkyWarsJoinEvent;
import com.walrusone.skywarsreloaded.managers.PlayerStat;
import com.walrusone.skywarsreloaded.menus.gameoptions.objects.CoordLoc;
import com.walrusone.skywarsreloaded.utilities.Util;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Team;

import java.util.ArrayList;
import java.util.UUID;

public class TeamCard {
    private ArrayList<PlayerCard> playerCards = new ArrayList();
    private ArrayList<UUID> dead = new ArrayList();
    private CoordLoc spawn;
    private GameMap gMap;
    private int place;
    private String prefix;
    private Team team;
    private byte bColor;
    private String name;
    private int position;

    TeamCard(int size, CoordLoc spawn, GameMap gameMap, String prefix, String color, int pos) {
        this.spawn = spawn;
        gMap = gameMap;
        place = 1;
        this.prefix = prefix;
        name = (prefix + color);
        String col = color.replaceAll("\\s", "").toLowerCase();
        bColor = Util.get().getByteFromColor(col);
        position = (pos - 1);
        for (int i = 0; i < size; i++) {
            playerCards.add(new PlayerCard(this, null, -1));
        }
    }

    void updateCard(int size) {
        if (size > playerCards.size()) {
            for (int i = playerCards.size(); i < size; i++) {
                playerCards.add(new PlayerCard(this, null, -1));
            }
        } else {
            while (size < playerCards.size()) {
                playerCards.remove(playerCards.size() - 1);
            }
        }
    }

    public int getSize() {
        return playerCards.size();
    }

    int getFullCount() {
        int x = 0;
        for (PlayerCard pCard : playerCards) {
            if (pCard.getUUID() == null) {
                x++;
            }
        }
        return x;
    }

    int getPlace() {
        return place;
    }

    public void setPlace(int x) {
        place = x;
    }

    public CoordLoc getSpawn() {
        return spawn;
    }

    public GameMap getGameMap() {
        return gMap;
    }

    public ArrayList<PlayerCard> getPlayerCards() {
        return playerCards;
    }

    TeamCard sendReservation(Player player, PlayerStat ps) {
        if ((player != null) && (ps != null) && (ps.isInitialized())) {
            for (PlayerCard pCard : playerCards) {
                if ((pCard.getUUID() == null) && (spawn != null)) {
                    pCard.setPlayer(player);
                    pCard.setPreElo(ps.getElo());
                    boolean glassReader = gMap.getCage().setGlassColor(gMap, this);
                    if (glassReader) {
                        return this;
                    }
                }
            }
        }
        return null;
    }

    boolean joinGame(Player player) {
        for (PlayerCard pCard : playerCards) {
            if (pCard.getUUID().equals(player.getUniqueId())) {
                team.addEntry(player.getName());
                gMap.getJoinQueue().add(pCard);
                Bukkit.getPluginManager().callEvent(new SkyWarsJoinEvent(player,gMap));
                if (SkyWarsReloaded.getCfg().kitVotingEnabled()) {
                    gMap.getKitVoteOption().updateKitVotes();
                }
                if (SkyWarsReloaded.getCfg().resetTimerOnJoin()) {
                    gMap.setTimer(SkyWarsReloaded.getCfg().getWaitTimer());
                }
                return true;
            }
        }
        return false;
    }

    boolean removePlayer(UUID uuid) {
        PlayerCard pCard = containsPlayer(uuid);
        if ((pCard != null) && (team != null)) {
            team.removeEntry(SkyWarsReloaded.get().getServer().getOfflinePlayer(uuid).getName());
            pCard.reset();
            return true;
        }
        return false;
    }

    void reset() {
        place = 1;
        for (PlayerCard pCard : playerCards) {
            pCard.reset();
        }
        dead.clear();
    }

    public ArrayList<UUID> getDead() {
        return dead;
    }

    PlayerCard containsPlayer(UUID uuid) {
        for (PlayerCard pCard : playerCards) {
            if ((uuid != null) &&
                    (pCard.getUUID() != null) &&
                    (pCard.getUUID().equals(uuid))) {
                return pCard;
            }
        }


        return null;
    }

    boolean isFull() {
        return getFullCount() == 0;
    }

    public int getPlayersSize() {
        int count = 0;
        for (PlayerCard pCard : playerCards) {
            if (pCard.getUUID() != null) {
                count++;
            }
        }
        return count;
    }

    public boolean isElmininated() {
        int num = getPlayersSize();
        return (num == 0) || (num == dead.size());
    }

    public String getPlayerNames() {
        StringBuilder name = new StringBuilder();
        for (PlayerCard pCard : playerCards) {
            if (pCard.getPlayer() != null) {
                name.append(pCard.getPlayer().getDisplayName());
                name.append(", ");
            }
        }
        if (name.length() > 2) {
            return name.substring(0, name.length() - 2);
        }
        return name.toString();
    }

    public String getTeamName() {
        return name;
    }

    String getPrefix() {
        return prefix;
    }

    public Team getTeam() {
        return team;
    }

    public void setTeam(Team team) {
        this.team = team;
    }

    public byte getByte() {
        return bColor;
    }

    public int getPosition() {
        return position;
    }
}
