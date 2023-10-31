package com.walrusone.skywarsreloaded.nms.v1_16_R3;

import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Scoreboard;

public class NMSHandler extends com.walrusone.skywarsreloaded.nms.v1_16_R2.NMSHandler {

    private NMSImpl_16_3 nmsImpl;

    public void playChestAction(Block block, boolean open) {
        if (nmsImpl == null) nmsImpl = new NMSImpl_16_3();
        nmsImpl.playChestAction(block, open);
    }

    public void setEntityTarget(org.bukkit.entity.Entity ent, Player player) {
        if (nmsImpl == null) nmsImpl = new NMSImpl_16_3();
        nmsImpl.setEntityTarget(ent, player);
    }
    
}
