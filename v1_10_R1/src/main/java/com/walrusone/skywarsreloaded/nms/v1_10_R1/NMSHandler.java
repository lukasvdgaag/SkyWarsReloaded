package com.walrusone.skywarsreloaded.nms.v1_10_R1;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

public class NMSHandler extends com.walrusone.skywarsreloaded.nms.v1_9_R2.NMSHandler {

    private NMSImpl_10_1 nmsImpl;

    public void sendTitle(Player player, int fadein, int stay, int fadeout, String title, String subtitle) {
        if (nmsImpl == null) nmsImpl = new NMSImpl_10_1();
        nmsImpl.sendTitle(player, fadein, stay, fadeout, title, subtitle);
    }

    public void playChestAction(Block block, boolean open) {
        if (nmsImpl == null) nmsImpl = new NMSImpl_10_1();
        nmsImpl.playChestAction(block, open);
    }

    public void setEntityTarget(Entity ent, Player player) {
        if (nmsImpl == null) nmsImpl = new NMSImpl_10_1();
        nmsImpl.setEntityTarget(ent, player);
    }

    public int getVersion() {
        return 10;
    }

}
