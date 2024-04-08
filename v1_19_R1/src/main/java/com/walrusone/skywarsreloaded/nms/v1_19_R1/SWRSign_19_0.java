package com.walrusone.skywarsreloaded.nms.v1_19_R1;

import com.walrusone.skywarsreloaded.nms.v1_8_R3.SWRSign83;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.type.WallSign;

public class SWRSign_19_0 extends SWRSign83 {

    public SWRSign_19_0(String name, Location location) {
        super(name, location);
    }

    @Override
    public Block getAttachedBlock(Block b) {
        BlockData blockData = b.getState().getBlockData();

        BlockFace face = BlockFace.DOWN;
        if (blockData instanceof WallSign) {
            face = ((WallSign) blockData).getFacing().getOppositeFace();
        }

        return b.getRelative(face);
    }

}
