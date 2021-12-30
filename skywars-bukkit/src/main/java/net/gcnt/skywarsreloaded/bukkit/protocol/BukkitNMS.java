package net.gcnt.skywarsreloaded.bukkit.protocol;

import net.gcnt.skywarsreloaded.SkyWarsReloaded;
import net.gcnt.skywarsreloaded.bukkit.utils.BukkitItem;
import net.gcnt.skywarsreloaded.bukkit.wrapper.player.BukkitSWPlayer;
import net.gcnt.skywarsreloaded.bukkit.wrapper.world.BukkitSWWorld;
import net.gcnt.skywarsreloaded.protocol.NMS;
import net.gcnt.skywarsreloaded.utils.Item;
import net.gcnt.skywarsreloaded.utils.SWCoord;
import net.gcnt.skywarsreloaded.wrapper.player.SWPlayer;
import net.gcnt.skywarsreloaded.wrapper.world.SWWorld;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.inventory.ItemStack;

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
    private Class<?> blockPosition;
    private Class<?> worldServer;
    private Class<?> craftWorld;
    private Class<?> tileEntityChest;
    private Class<?> block;

    private Method chatSerializer;
    private Method getHandle;
    private Method sendPacket;
    private Field playerConnection;

    public BukkitNMS(SkyWarsReloaded plugin, String serverPackage) throws IllegalStateException {
        this.plugin = plugin;
        String serverVersion = serverPackage.substring(serverPackage.lastIndexOf('.') + 1);
        this.version = plugin.getUtils().getServerVersion();

        try {

            Class<?> craftPlayer = Class.forName("org.bukkit.craftbukkit." + serverVersion + ".entity.CraftPlayer");
            this.getHandle = craftPlayer.getMethod("getHandle");

            if (version < 8) {
                throw new IllegalStateException("Unsupported server version: " + serverVersion);
            } else if (version <= 16) {
                // 1.8.8 - 1.16.4
                //Classes
                Class<?> typeNMSPlayer = Class.forName("net.minecraft.server." + serverVersion + ".EntityPlayer");
                Class<?> typePlayerConnection = Class.forName("net.minecraft.server." + serverVersion + ".PlayerConnection");
                this.chatBaseComponent = Class.forName("net.minecraft.server." + serverVersion + ".IChatBaseComponent");
                this.packetPlayOutChat = Class.forName("net.minecraft.server." + serverVersion + ".PacketPlayOutChat");
                if (version >= 12) this.chatMessageType = (Class<Enum>) Class.forName("net.minecraft.server." + serverVersion + ".ChatMessageType");

                this.blockPosition = Class.forName("net.minecraft.server." + serverVersion + ".BlockPosition");
                this.craftWorld = Class.forName("org.bukkit.craftbukkit." + serverVersion + ".CraftWorld");
                this.worldServer = Class.forName("net.minecraft.server." + serverVersion + ".WorldServer");
                this.tileEntityChest = Class.forName("net.minecraft.server." + serverVersion + ".TileEntityChest");
                this.block = Class.forName("net.minecraft.server." + serverVersion + ".Block");

                // Fields
                this.playerConnection = typeNMSPlayer.getField("playerConnection");

                // Methods
                this.chatSerializer = chatBaseComponent.getDeclaredClasses()[0].getMethod("a", String.class);
                this.sendPacket = typePlayerConnection.getMethod("sendPacket", Class.forName("net.minecraft.server." + serverVersion + ".Packet"));
            } else {
                // 1.17+ (and default to this for any future version unless updated)
                // Classes
                Class<?> typeNMSPlayer = Class.forName("net.minecraft.server.level.EntityPlayer");
                Class<?> typePlayerConnection = Class.forName("net.minecraft.server.network.PlayerConnection");
                this.chatBaseComponent = Class.forName("net.minecraft.network.chat.IChatBaseComponent");
                this.packetPlayOutChat = Class.forName("net.minecraft.network.protocol.game.PacketPlayOutChat");
                this.chatMessageType = (Class<Enum>) Class.forName("net.minecraft.network.chat.ChatMessageType");

                this.blockPosition = Class.forName("net.minecraft.core.BlockPosition");
                this.craftWorld = Class.forName("org.bukkit.craftbukkit." + serverVersion + ".CraftWorld");
                this.worldServer = Class.forName("net.minecraft.server.level.WorldServer");
                this.tileEntityChest = Class.forName("net.minecraft.server.level.block.entity.TileEntityChest");
                this.block = Class.forName("net.minecraft.server.world.level.block.Block");

                // Fields
                this.playerConnection = typeNMSPlayer.getField("b");

                // Methods
                this.chatSerializer = chatBaseComponent.getDeclaredClasses()[0].getMethod("a", String.class);
                this.sendPacket = typePlayerConnection.getMethod("sendPacket", Class.forName("net.minecraft.network.protocol.Packet"));
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
        if (!(player instanceof BukkitSWPlayer)) return;
        BukkitSWPlayer bukkitSWPlayer = (BukkitSWPlayer) player;

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
        if (!(player instanceof BukkitSWPlayer)) return;
        BukkitSWPlayer bukkitSWPlayer = (BukkitSWPlayer) player;

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

    @Override
    public void setBlock(SWCoord loc, Item item) {
        if (loc.world() == null || !(loc.world() instanceof BukkitSWWorld) || !(item instanceof BukkitItem)) return;
        World world = ((BukkitSWWorld) loc.world()).getBukkitWorld();
        ItemStack itemStack = ((BukkitItem) item).getBukkitItem();

        Block bukkitBlock = world.getBlockAt(loc.x(), loc.y(), loc.z());
        bukkitBlock.setType(itemStack.getType());
        if (version < 13) {
            try {
                block.getMethod("setData", Byte.class).invoke(bukkitBlock, item.getDamage());
            } catch (Exception ignored) {
            }
        }
    }

    @Override
    public void setChestOpen(SWCoord loc) {
        if (loc.world() == null || !(loc.world() instanceof BukkitSWWorld)) return;
        World world = ((BukkitSWWorld) loc.world()).getBukkitWorld();

        try {
            if (version >= 16) {
                Block block = world.getBlockAt(loc.x(), loc.y(), loc.z());
                Chest chest = (Chest) block;
                chest.open();
            } else {
                Object position = blockPosition.getDeclaredConstructor(Integer.class, Integer.class, Integer.class).newInstance(loc.x(), loc.y(), loc.z());
                Object serverWorld = craftWorld.getMethod("getHandle").invoke(craftWorld.cast(world));
                Object tileChest = tileEntityChest.cast(worldServer.getMethod("getTileEntity", blockPosition).invoke(serverWorld, position));
                Object blockData = tileEntityChest.getMethod("getBlock").invoke(tileChest);
                Object tileBlock = blockData.getClass().getMethod("getBlock").invoke(blockData);
                worldServer.getMethod("playBlockAction", blockPosition, block, Integer.class, Integer.class).invoke(serverWorld, position, tileBlock, 1, 1);
            }
        } catch (Exception ignored) {
        }
    }

}
