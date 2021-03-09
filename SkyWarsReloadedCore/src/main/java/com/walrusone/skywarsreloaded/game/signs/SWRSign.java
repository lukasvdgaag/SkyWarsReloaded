package com.walrusone.skywarsreloaded.game.signs;

import com.walrusone.skywarsreloaded.game.GameMap;
import org.bukkit.Location;
import org.bukkit.block.Block;

public interface SWRSign {

    void setMaterial(GameMap gMap, Block attachedBlock);

    void updateBlock(Block block, String item);

    Block getAttachedBlock(final Block b);

    void update();

    Location getLocation();

    String getName();

}
