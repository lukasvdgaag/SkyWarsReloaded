package net.gcnt.skywarsreloaded.manager;

import net.gcnt.skywarsreloaded.game.cages.Cage;

import java.util.List;

public interface CageManager {

    void loadAllCages();

    Cage getCageById(String id);

    List<Cage> getCagesByType(String type);

    List<Cage> getAllCages();

    void deleteCage(Cage cage);

}
