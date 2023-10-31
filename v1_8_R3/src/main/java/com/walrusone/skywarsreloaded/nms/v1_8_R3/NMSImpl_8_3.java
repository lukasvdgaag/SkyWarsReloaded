package com.walrusone.skywarsreloaded.nms.v1_8_R3;

import net.minecraft.server.v1_8_R3.*;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftEntity;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

public class NMSImpl_8_3 {
    
    public void sendParticles(World world, String type, float x, float y, float z, float offsetX, float offsetY, float offsetZ, float data, int amount) {
        EnumParticle particle = EnumParticle.valueOf(type);
        PacketPlayOutWorldParticles particles = new PacketPlayOutWorldParticles(particle, true, x, y, z, offsetX, offsetY, offsetZ, data, amount, 1);
        for (Player player : world.getPlayers()) {
            org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer start = (org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer) player;
            EntityPlayer target = start.getHandle();
            PlayerConnection connect = target.playerConnection;
            connect.sendPacket(particles);
        }
    }

    public void sendTitle(Player player, int fadein, int stay, int fadeout, String title, String subtitle) {
        PlayerConnection pConn = ((org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer) player).getHandle().playerConnection;
        PacketPlayOutTitle pTitleInfo = new PacketPlayOutTitle(PacketPlayOutTitle.EnumTitleAction.TIMES, null, fadein, stay, fadeout);
        pConn.sendPacket(pTitleInfo);
        if (subtitle != null) {
            subtitle = subtitle.replaceAll("%player%", player.getDisplayName());
            subtitle = ChatColor.translateAlternateColorCodes('&', subtitle);
            IChatBaseComponent iComp = IChatBaseComponent.ChatSerializer.a("{\"text\": \"" + subtitle + "\"}");
            PacketPlayOutTitle pSubtitle = new PacketPlayOutTitle(PacketPlayOutTitle.EnumTitleAction.SUBTITLE, iComp);
            pConn.sendPacket(pSubtitle);
        }
        if (title != null) {
            title = title.replaceAll("%player%", player.getDisplayName());
            title = ChatColor.translateAlternateColorCodes('&', title);
            IChatBaseComponent iComp = IChatBaseComponent.ChatSerializer.a("{\"text\": \"" + title + "\"}");
            PacketPlayOutTitle pTitle = new PacketPlayOutTitle(PacketPlayOutTitle.EnumTitleAction.TITLE, iComp);
            pConn.sendPacket(pTitle);
        }
    }

    public void sendActionBar(Player p, String msg) {
        String s = ChatColor.translateAlternateColorCodes('&', msg);
        IChatBaseComponent icbc = IChatBaseComponent.ChatSerializer.a("{\"text\": \"" + s + "\"}");
        PacketPlayOutChat bar = new PacketPlayOutChat(icbc, (byte) 2);
        ((org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer) p).getHandle().playerConnection.sendPacket(bar);
    }

    public void playChestAction(Block block, boolean open) {
        Location location = block.getLocation();
        WorldServer world = ((CraftWorld) location.getWorld()).getHandle();
        BlockPosition position = new BlockPosition(location.getX(), location.getY(), location.getZ());
        TileEntity tileEntity = world.getTileEntity(position);
        world.playBlockAction(position, tileEntity.w(), 1, open ? 1 : 0);
    }

    public void setEntityTarget(Entity ent, Player player) {
        EntityCreature entity = (EntityCreature) ((CraftEntity) ent).getHandle();
        entity.setGoalTarget(((CraftPlayer) player).getHandle(), null, false);
    }

    public void sendJSON(Player sender, String json) {
        final IChatBaseComponent icbc = IChatBaseComponent.ChatSerializer.a(json);
        final PacketPlayOutChat chat = new PacketPlayOutChat(icbc);
        ((CraftPlayer) sender).getHandle().playerConnection.sendPacket(chat);
    }
}
