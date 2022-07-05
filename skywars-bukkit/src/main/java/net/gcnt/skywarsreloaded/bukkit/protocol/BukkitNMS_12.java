package net.gcnt.skywarsreloaded.bukkit.protocol;

import net.gcnt.skywarsreloaded.SkyWarsReloaded;
import net.gcnt.skywarsreloaded.bukkit.wrapper.player.BukkitSWPlayer;
import net.gcnt.skywarsreloaded.wrapper.entity.SWPlayer;

import java.lang.reflect.InvocationTargetException;

public class BukkitNMS_12 extends BukkitNMS_8_11 {

    public BukkitNMS_12(SkyWarsReloaded plugin, String serverPackage) {
        super(plugin, serverPackage);
    }

    @Override
    @SuppressWarnings("unchecked")
    public void sendActionbar(SWPlayer player, String message) {
        if (!(player instanceof BukkitSWPlayer)) return;
        BukkitSWPlayer bukkitSWPlayer = (BukkitSWPlayer) player;

        try {
            Object baseComponent = chatSerializer.invoke(null, "{\"text\": \"" + message + "\"}");
            Object packet = packetPlayOutChat.getDeclaredConstructor(chatBaseComponent, chatMessageType)
                    .newInstance(baseComponent, Enum.valueOf(chatMessageType, "CHAT"));

            Object nmsPlayer = getHandle.invoke(bukkitSWPlayer.getPlayer());
            Object connection = playerConnection.get(nmsPlayer);
            sendPacket.invoke(connection, packet);
        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException | InstantiationException e) {
            e.printStackTrace();
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public void sendJSONMessage(SWPlayer player, String message) {
        if (!(player instanceof BukkitSWPlayer)) return;
        BukkitSWPlayer bukkitSWPlayer = (BukkitSWPlayer) player;

        try {
            Object baseComponent = chatSerializer.invoke(null, message);
            Object packet = packetPlayOutChat.getDeclaredConstructor(chatBaseComponent, chatMessageType)
                    .newInstance(baseComponent, Enum.valueOf(chatMessageType, "SYSTEM"));

            Object nmsPlayer = getHandle.invoke(bukkitSWPlayer.getPlayer());
            Object connection = playerConnection.get(nmsPlayer);
            sendPacket.invoke(connection, packet);
        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException | InstantiationException e) {
            e.printStackTrace();
        }
    }
}
