package com.walrusone.skywarsreloaded.nms.v1_9_R2;

import com.walrusone.skywarsreloaded.nms.v1_9_R1.NMSImpl_9_1;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

public class NMSHandler extends com.walrusone.skywarsreloaded.nms.v1_9_R1.NMSHandler {

    private NMSImpl_9_2 nmsImpl;

    public void sendTitle(Player player, int fadein, int stay, int fadeout, String title, String subtitle) {
        if (nmsImpl == null) nmsImpl = new NMSImpl_9_2();
        nmsImpl.sendTitle(player, fadein, stay, fadeout, title, subtitle);
    }

    public void playChestAction(Block block, boolean open) {
        if (nmsImpl == null) nmsImpl = new NMSImpl_9_2();
        nmsImpl.playChestAction(block, open);
    }

    public void setEntityTarget(Entity ent, Player player) {
        if (nmsImpl == null) nmsImpl = new NMSImpl_9_2();
        nmsImpl.setEntityTarget(ent, player);
    }

}
