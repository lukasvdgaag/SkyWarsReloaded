package com.walrusone.skywarsreloaded.nms.v1_9_R2;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

public class NMSHandler extends com.walrusone.skywarsreloaded.nms.v1_9_R1.NMSHandler {

    public void sendTitle(Player player, int fadein, int stay, int fadeout, String title, String subtitle) {
        net.minecraft.server.v1_9_R2.PlayerConnection pConn = ((org.bukkit.craftbukkit.v1_9_R2.entity.CraftPlayer) player).getHandle().playerConnection;
        net.minecraft.server.v1_9_R2.PacketPlayOutTitle pTitleInfo = new net.minecraft.server.v1_9_R2.PacketPlayOutTitle(net.minecraft.server.v1_9_R2.PacketPlayOutTitle.EnumTitleAction.TIMES, null, fadein, stay, fadeout);
        pConn.sendPacket(pTitleInfo);
        if (subtitle != null) {
            subtitle = subtitle.replaceAll("%player%", player.getDisplayName());
            subtitle = ChatColor.translateAlternateColorCodes('&', subtitle);
            net.minecraft.server.v1_9_R2.IChatBaseComponent iComp = net.minecraft.server.v1_9_R2.IChatBaseComponent.ChatSerializer.a("{\"text\": \"" + subtitle + "\"}");
            net.minecraft.server.v1_9_R2.PacketPlayOutTitle pSubtitle = new net.minecraft.server.v1_9_R2.PacketPlayOutTitle(net.minecraft.server.v1_9_R2.PacketPlayOutTitle.EnumTitleAction.SUBTITLE, iComp);
            pConn.sendPacket(pSubtitle);
        }
        if (title != null) {
            title = title.replaceAll("%player%", player.getDisplayName());
            title = ChatColor.translateAlternateColorCodes('&', title);
            net.minecraft.server.v1_9_R2.IChatBaseComponent iComp = net.minecraft.server.v1_9_R2.IChatBaseComponent.ChatSerializer.a("{\"text\": \"" + title + "\"}");
            net.minecraft.server.v1_9_R2.PacketPlayOutTitle pTitle = new net.minecraft.server.v1_9_R2.PacketPlayOutTitle(net.minecraft.server.v1_9_R2.PacketPlayOutTitle.EnumTitleAction.TITLE, iComp);
            pConn.sendPacket(pTitle);
        }
    }

    public void playChestAction(Block block, boolean open) {
        Location location = block.getLocation();
        net.minecraft.server.v1_9_R2.WorldServer world = ((org.bukkit.craftbukkit.v1_9_R2.CraftWorld) location.getWorld()).getHandle();
        net.minecraft.server.v1_9_R2.BlockPosition position = new net.minecraft.server.v1_9_R2.BlockPosition(location.getX(), location.getY(), location.getZ());
        net.minecraft.server.v1_9_R2.TileEntityEnderChest ec = (net.minecraft.server.v1_9_R2.TileEntityEnderChest) world.getTileEntity(position);
        assert ec != null;
        world.playBlockAction(position, ec.getBlock(), 1, open ? 1 : 0);
    }

    public void setEntityTarget(Entity ent, Player player) {
        net.minecraft.server.v1_9_R2.EntityCreature entity = (net.minecraft.server.v1_9_R2.EntityCreature) ((org.bukkit.craftbukkit.v1_9_R2.entity.CraftEntity) ent).getHandle();
        entity.setGoalTarget(((org.bukkit.craftbukkit.v1_9_R2.entity.CraftPlayer) player).getHandle(), null, false);
    }

}
