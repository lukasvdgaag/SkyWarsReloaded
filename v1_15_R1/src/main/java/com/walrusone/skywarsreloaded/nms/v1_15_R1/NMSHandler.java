package com.walrusone.skywarsreloaded.nms.v1_15_R1;

import com.google.common.collect.Lists;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityTargetEvent;
import org.bukkit.scoreboard.Scoreboard;

import java.lang.reflect.Field;
import java.util.Collection;

public class NMSHandler extends com.walrusone.skywarsreloaded.nms.v1_14_R1.NMSHandler {

    private NMSImpl_15_1 nmsImpl;

    public void playChestAction(Block block, boolean open) {
        if (nmsImpl == null) nmsImpl = new NMSImpl_15_1();
        nmsImpl.playChestAction(block, open);
    }

    public void setEntityTarget(org.bukkit.entity.Entity ent, Player player) {
        if (nmsImpl == null) nmsImpl = new NMSImpl_15_1();
        nmsImpl.setEntityTarget(ent, player);
    }


    public int getVersion() {
        return 15;
    }

}
