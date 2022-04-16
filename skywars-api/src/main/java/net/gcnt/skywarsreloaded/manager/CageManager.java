package net.gcnt.skywarsreloaded.manager;

import com.sk89q.worldedit.EditSession;
import net.gcnt.skywarsreloaded.game.TeamCage;
import net.gcnt.skywarsreloaded.game.TeamSpawn;
import net.gcnt.skywarsreloaded.game.cages.Cage;
import net.gcnt.skywarsreloaded.utils.SWCoord;

import java.util.List;

public interface CageManager {

    void loadAllCages();

    Cage getCageById(String id);

    List<Cage> getCagesByType(String type);

    List<Cage> getAllCages();

    EditSession placeCage(Cage cage, SWCoord location);

}
