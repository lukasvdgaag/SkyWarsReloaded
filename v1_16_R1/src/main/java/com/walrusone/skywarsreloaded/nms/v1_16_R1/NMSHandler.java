package com.walrusone.skywarsreloaded.nms.v1_16_R1;

import com.google.common.collect.Lists;
import com.walrusone.skywarsreloaded.nms.v1_15_R1.NMSImpl_15_1;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityTargetEvent;
import org.bukkit.scoreboard.Scoreboard;

import java.util.Collection;

public class NMSHandler extends com.walrusone.skywarsreloaded.nms.v1_15_R1.NMSHandler {

    private NMSImpl_16_1 nmsImpl;

    public void playChestAction(Block block, boolean open) {
        if (nmsImpl == null) nmsImpl = new NMSImpl_16_1();
        nmsImpl.playChestAction(block, open);
    }

    public void setEntityTarget(org.bukkit.entity.Entity ent, Player player) {
        if (nmsImpl == null) nmsImpl = new NMSImpl_16_1();
        nmsImpl.setEntityTarget(ent, player);
    }


    public int getVersion() {
        return 16;
    }


    protected String getColorFromByte(byte color) {
        switch (color) {
            case 0:
                return "WHITE";
            case 1:
                return "ORANGE";
            case 2:
                return "MAGENTA";
            case 3:
                return "LIGHT_BLUE";
            case 4:
                return "YELLOW";
            case 5:
                return "LIME";
            case 6:
                return "PINK";
            case 7:
                return "GRAY";
            case 8:
                return "LIGHT_GRAY";
            case 9:
                return "CYAN";
            case 10:
                return "PURPLE";
            case 11:
                return "BLUE";
            case 12:
                return "BROWN";
            case 13:
                return "GREEN";
            case 14:
                return "RED";
            case 16:
                return "BLACK";
        }
        return "WHITE";
    }


}
