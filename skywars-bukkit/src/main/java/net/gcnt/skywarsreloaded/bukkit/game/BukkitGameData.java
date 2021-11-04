package net.gcnt.skywarsreloaded.bukkit.game;

import net.gcnt.skywarsreloaded.game.GameData;
import net.gcnt.skywarsreloaded.utils.Coord;

import java.util.List;

public class BukkitGameData implements GameData {

    private final String name;
    private String displayName;
    private String creator;
    private Coord spectateSpawn;
    private Coord lobbySpawn;
    private int teamsize;
    private List<Coord> chests;

    public BukkitGameData(String name) {
        this.name = name;
        // todo get info from mapdata file.
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public String getCreator() {
        return this.creator;
    }

    @Override
    public int getTeamSize() {
        return this.teamsize;
    }

    @Override
    public String getDisplayName() {
        return this.displayName;
    }

    @Override
    public Coord getSpectateSpawn() {
        return this.spectateSpawn;
    }

    @Override
    public void setSpectateSpawn(Coord loc) {
        this.spectateSpawn = loc;
    }

    @Override
    public Coord getWaitingLobbySpawn() {
        return this.lobbySpawn;
    }

    @Override
    public void setWaitingLobbySpawn(Coord loc) {
        this.lobbySpawn = loc;
    }

    @Override
    public void disable() {

    }

    @Override
    public void loadData() {

    }

    @Override
    public void saveData() {

    }

    @Override
    public synchronized boolean addChest(Coord loc) {
        for (Coord chest : this.chests) {
            if (chest.equals(loc)) return false;
        }
        this.chests.add(loc);
        return true;
    }

    @Override
    public synchronized boolean removeChest(Coord loc) {
        for (Coord chest : this.chests) {
            if (chest.equals(loc)) {
                this.chests.remove(loc);
                return true;
            }
        }
        return false;
    }

    @Override
    public List<Coord> getChests() {
        return this.chests;
    }
}
