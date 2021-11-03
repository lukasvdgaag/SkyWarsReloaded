package net.gcnt.skywarsreloaded.bukkit.game;

import net.gcnt.skywarsreloaded.game.Game;
import net.gcnt.skywarsreloaded.utils.Coord;

import java.util.List;

public class BukkitGame implements Game {

    private final String name;
    private String creator;
    private Coord spectateSpawn;
    private Coord lobbySpawn;
    private List<Coord> chests;

    public BukkitGame(String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getCreator() {
        return creator;
    }

    @Override
    public int getTeamSize() {
        return 0;
    }

    @Override
    public String getDisplayName() {
        return null;
    }

    @Override
    public Coord getSpectateSpawn() {
        return null;
    }

    @Override
    public void setSpectateSpawn(Coord loc) {

    }

    @Override
    public Coord getWaitingLobbySpawn() {
        return null;
    }

    @Override
    public void setWaitingLobbySpawn(Coord loc) {

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
    public void addChest(Coord loc) {

    }

    @Override
    public void removeChest(Coord loc) {

    }

    @Override
    public List<Coord> getChests() {
        return null;
    }
}
