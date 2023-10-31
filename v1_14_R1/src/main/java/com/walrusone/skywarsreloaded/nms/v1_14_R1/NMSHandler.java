package com.walrusone.skywarsreloaded.nms.v1_14_R1;

import com.google.common.collect.Lists;
import com.walrusone.skywarsreloaded.nms.v1_13_R2.NMSImpl_13_2;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityTargetEvent;
import org.bukkit.scoreboard.Scoreboard;

import java.util.Collection;

public class NMSHandler extends com.walrusone.skywarsreloaded.nms.v1_13_R2.NMSHandler {

    private NMSImpl_14_1 nmsImpl;

    public void playChestAction(Block block, boolean open) {
        if (nmsImpl == null) nmsImpl = new NMSImpl_14_1();
        nmsImpl.playChestAction(block, open);
    }

    public void setEntityTarget(org.bukkit.entity.Entity ent, Player player) {
        if (nmsImpl == null) nmsImpl = new NMSImpl_14_1();
        nmsImpl.setEntityTarget(ent, player);
    }

    public int getVersion() {
        return 14;
    }

}
