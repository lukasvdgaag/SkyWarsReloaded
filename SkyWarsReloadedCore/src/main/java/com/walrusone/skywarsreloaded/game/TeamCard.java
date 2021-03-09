package com.walrusone.skywarsreloaded.game;

import com.walrusone.skywarsreloaded.SkyWarsReloaded;
import com.walrusone.skywarsreloaded.enums.MatchState;
import com.walrusone.skywarsreloaded.events.SkyWarsJoinEvent;
import com.walrusone.skywarsreloaded.game.cages.schematics.SchematicCage;
import com.walrusone.skywarsreloaded.managers.PlayerStat;
import com.walrusone.skywarsreloaded.menus.gameoptions.objects.CoordLoc;
import com.walrusone.skywarsreloaded.utilities.Util;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.StringJoiner;
import java.util.UUID;

public class TeamCard {
    private ArrayList<PlayerCard> playerCards = new ArrayList();
    private ArrayList<UUID> dead = new ArrayList();
    private List<CoordLoc> spawns;
    private GameMap gMap;
    private int place;
    private String prefix;
    //private Team team;
    private byte bColor;
    private String name;
    private int position;

    TeamCard(int size, GameMap gameMap, String prefix, String color, int pos, List<CoordLoc> teamSpawns) {
        this.spawns = teamSpawns;
        gMap = gameMap;
        place = 1;
        this.prefix = prefix;
        name = (prefix + color);
        String col = color.replaceAll("\\s", "").toLowerCase();
        bColor = Util.get().getByteFromColor(col);
        position = pos;
        for (int i = 0; i < size; i++) {
            CoordLoc loc;
            if (teamSpawns == null) loc = null;
            else {
                if (SkyWarsReloaded.getCfg().isUseSeparateCages()) {
                    loc = teamSpawns.size() >= i + 1 ? teamSpawns.get(i) : null;
                } else {
                    loc = teamSpawns.size() >= 1 ? teamSpawns.get(0) : null;
                }
            }
            playerCards.add(new PlayerCard(this, null, loc));
        }
    }

    void updateCard(int size) {
        if (size > playerCards.size()) {
            for (int i = playerCards.size(); i < size; i++) {
                playerCards.add(new PlayerCard(this, null, spawns.get(i)));
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

    public int getFullCount() {
        int x = 0;
        for (PlayerCard pCard : playerCards) {
            if (pCard.getUUID() == null) {
                x++;
            }
        }
        return x;
    }

    public int getPlace() {
        return place;
    }

    public void setPlace(int x) {
        place = x;
    }

    public List<CoordLoc> getSpawns() {
        return spawns;
    }

    public GameMap getGameMap() {
        return gMap;
    }

    public ArrayList<PlayerCard> getPlayerCards() {
        return playerCards;
    }

    public TeamCard sendReservation(Player player, PlayerStat ps) {
        if (player != null && ps != null && ps.isInitialized()) { // player is valid & player's meta info is available
            for (PlayerCard pCard : playerCards) {
                if ((pCard.getUUID() == null) && (pCard.getSpawn() != null)) {
                    pCard.setPlayer(player);

                    if (SkyWarsReloaded.getCfg().debugEnabled()) SkyWarsReloaded.get().getLogger().info("#teamCard: pCard in reservation " + pCard.getUUID());
                    if (pCard.getTeamCard().getGameMap().getMatchState() == MatchState.WAITINGSTART) {
                        spawnCage(pCard, ps);
                    }
                    return this; // return team card if successfully added to team
                }
            }
        }
        return null;
    }

    public void spawnCage(PlayerCard pCard, PlayerStat ps) {
        String cageName = ps.getGlassColor();
        // todo check this is beta
        if (cageName != null && cageName.startsWith("custom-")) {
            if (Bukkit.getPluginManager().isPluginEnabled("WorldEdit")) {
                //cage.removeSpawnHousing(this, reserved,false);
                Bukkit.getScheduler().runTaskLater(SkyWarsReloaded.get(), () -> {
                    boolean b = new SchematicCage().createSpawnPlatform(gMap, pCard.getPlayer());
                    if (!b) {
                        gMap.getCage().setGlassColor(gMap, this);
                    }
                }, 10L);
            }
        } else {
            boolean glassReader = gMap.getCage().setGlassColor(gMap, this);
        }
    }

    boolean joinGame(Player player) {
        for (PlayerCard pCard : playerCards) {
            if (pCard.getUUID().equals(player.getUniqueId())) {
                gMap.getJoinQueue().add(pCard);
                Bukkit.getPluginManager().callEvent(new SkyWarsJoinEvent(player, gMap));
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

    public boolean removePlayer(UUID uuid) {
        PlayerCard pCard = containsPlayer(uuid);
        if ((pCard != null) /*&& (team != null)*/) {
            //team.removeEntry(SkyWarsReloaded.get().getServer().getOfflinePlayer(uuid).getName());
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
            if ((pCard.getUUID() != null) &&
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
        StringJoiner name = new StringJoiner(", ");
        for (PlayerCard pCard : playerCards) {
            if (pCard.getPlayer() != null) {
                name.add(pCard.getPlayer().getDisplayName());
            }
        }
        return name.toString();
    }

    public String getTeamName() {
        return name;
    }

    String getPrefix() {
        return prefix;
    }

    /*public Team getTeam() {
        return team;
    }

    public void setTeam(Team team) {
        this.team = team;
    }*/

    public byte getByte() {
        return bColor;
    }

    public int getPosition() {
        return position;
    }
}
