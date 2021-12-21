package net.gcnt.skywarsreloaded.bukkit.protocol;

import net.gcnt.skywarsreloaded.SkyWarsReloaded;
import net.gcnt.skywarsreloaded.bukkit.wrapper.player.BukkitSWPlayer;
import net.gcnt.skywarsreloaded.protocol.NMS;
import net.gcnt.skywarsreloaded.wrapper.player.SWPlayer;
import net.gcnt.skywarsreloaded.wrapper.world.SWWorld;
import org.bukkit.Bukkit;
import org.bukkit.World;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.UUID;

@SuppressWarnings({"unchecked", "rawtypes"})
public class BukkitNMS implements NMS {

    private final SkyWarsReloaded plugin;
    private final int version;

    private Class<?> chatBaseComponent;
    private Class<?> packetPlayOutChat;
    private Class<Enum> chatMessageType;

    private Method chatSerializer;
    private Method getHandle;
    private Method sendPacket;
    private Field playerConnection;

    public BukkitNMS(SkyWarsReloaded plugin, String serverPackage) {
        this.plugin = plugin;
        String serverVersion = serverPackage.substring(serverPackage.lastIndexOf('.') + 1);
        this.version = plugin.getUtils().getServerVersion();

        try {

            Class<?> craftPlayer = Class.forName("org.bukkit.craftbukkit." + serverVersion + ".entity.CraftPlayer");
            this.getHandle = craftPlayer.getMethod("getHandle");

            chatBaseComponent = Class.forName("net.minecraft.server." + serverVersion + ".IChatBaseComponent");
            chatSerializer = chatBaseComponent.getDeclaredClasses()[0].getMethod("a", String.class);

            if (version >= 17) {
                // 1.17+
                Class<?> typeNMSPlayer = Class.forName("net.minecraft.server.level.EntityPlayer");
                Class<?> typePlayerConnection = Class.forName("net.minecraft.server.network.PlayerConnection");
                this.playerConnection = typeNMSPlayer.getField("b");
                this.sendPacket = typePlayerConnection.getMethod("sendPacket", Class.forName("net.minecraft.network.protocol.Packet"));
                this.packetPlayOutChat = Class.forName("net.minecraft.network.protocol.game.PacketPlayOutChat");
                this.chatMessageType = (Class<Enum>) Class.forName("net.minecraft.network.chat.ChatMessageType");
            } else {
                // 1.8.8 - 1.16.4
                Class<?> typeNMSPlayer = Class.forName("net.minecraft.server." + serverVersion + ".EntityPlayer");
                Class<?> typePlayerConnection = Class.forName("net.minecraft.server." + serverVersion + ".PlayerConnection");
                this.playerConnection = typeNMSPlayer.getField("playerConnection");
                this.sendPacket = typePlayerConnection.getMethod("sendPacket", Class.forName("net.minecraft.server." + serverVersion + ".Packet"));
                this.packetPlayOutChat = Class.forName("net.minecraft.server." + serverVersion + ".PacketPlayOutChat");
                if (version >= 12) this.chatMessageType = (Class<Enum>) Class.forName("net.minecraft.server." + serverVersion + ".ChatMessageType");
            }
        } catch (ClassNotFoundException | NoSuchMethodException | NoSuchFieldException e) {
            e.printStackTrace();
        }
    }

    @Override
    @SuppressWarnings("deprecation")
    public void setGameRule(SWWorld world, String rule, String value) {
        World bukkitWorld = Bukkit.getWorld(world.getName());
        if (bukkitWorld == null) return;
        // todo make this use the non-deprecated method
        bukkitWorld.setGameRuleValue(rule, value);
    }

    @Override
    public void sendActionbar(SWPlayer player, String message) {
        if (!(player instanceof BukkitSWPlayer bukkitSWPlayer)) return;

        try {
            Object baseComponent = chatSerializer.invoke(null, "{\"text\": \"" + message + "\"}");

            Object packet;
            if (version < 12) {
                packet = packetPlayOutChat.getDeclaredConstructor(chatBaseComponent, byte.class).newInstance(baseComponent, (byte) 2);
            } else if (version < 16) {
                packet = packetPlayOutChat.getDeclaredConstructor(chatBaseComponent, chatMessageType)
                        .newInstance(baseComponent, Enum.valueOf(chatMessageType, "CHAT"));
            } else {
                packet = packetPlayOutChat.getDeclaredConstructor(chatBaseComponent, chatMessageType, UUID.class)
                        .newInstance(baseComponent, Enum.valueOf(chatMessageType, "CHAT"), player.getUuid());
            }


            Object nmsPlayer = getHandle.invoke(bukkitSWPlayer.getPlayer());
            Object connection = playerConnection.get(nmsPlayer);

            sendPacket.invoke(connection, packet);
        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException | InstantiationException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void sendJSONMessage(SWPlayer player, String message) {
        if (!(player instanceof BukkitSWPlayer bukkitSWPlayer)) return;

        try {
            Object baseComponent = chatSerializer.invoke(null, message);

            Object packet;
            if (version < 12) {
                packet = packetPlayOutChat.getDeclaredConstructor(chatBaseComponent, byte.class).newInstance(baseComponent, (byte) 2);
            } else if (version < 16) {
                packet = packetPlayOutChat.getDeclaredConstructor(chatBaseComponent, chatMessageType)
                        .newInstance(baseComponent, Enum.valueOf(chatMessageType, "GAME_INFO"));
            } else {
                packet = packetPlayOutChat.getDeclaredConstructor(chatBaseComponent, chatMessageType, UUID.class)
                        .newInstance(baseComponent, Enum.valueOf(chatMessageType, "GAME_INFO"), player.getUuid());
            }

            Object nmsPlayer = getHandle.invoke(bukkitSWPlayer.getPlayer());
            Object connection = playerConnection.get(nmsPlayer);

            sendPacket.invoke(connection, packet);
        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException | InstantiationException e) {
            e.printStackTrace();
        }
    }
}
