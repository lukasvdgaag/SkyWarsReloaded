package com.walrusone.skywarsreloaded.managers.holograms;

import com.walrusone.skywarsreloaded.enums.LeaderType;
import org.bukkit.Location;

import java.util.List;

/**
 * Manages the creation, updating, and removal of holograms for leaderboards.
 */
public interface HologramManager {

    void load();

    void createLeaderboardHologram(Location loc, LeaderType type, String formatKey);

    void updateLeaderboardHolograms(LeaderType type);

    boolean removeHologram(Location loc);

    List<String> getFormats(LeaderType type);

}
