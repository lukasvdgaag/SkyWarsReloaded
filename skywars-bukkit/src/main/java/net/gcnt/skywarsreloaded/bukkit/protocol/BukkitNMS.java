package net.gcnt.skywarsreloaded.bukkit.protocol;

import net.gcnt.skywarsreloaded.SkyWarsReloaded;
import net.gcnt.skywarsreloaded.bukkit.utils.BukkitItem;
import net.gcnt.skywarsreloaded.bukkit.wrapper.player.BukkitSWPlayer;
import net.gcnt.skywarsreloaded.bukkit.wrapper.world.BukkitSWWorld;
import net.gcnt.skywarsreloaded.protocol.NMS;
import net.gcnt.skywarsreloaded.utils.Item;
import net.gcnt.skywarsreloaded.utils.SWCoord;
import net.gcnt.skywarsreloaded.wrapper.player.SWPlayer;
import net.gcnt.skywarsreloaded.wrapper.server.SWGameRule;
import net.gcnt.skywarsreloaded.wrapper.world.SWWorld;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.inventory.ItemStack;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

@SuppressWarnings({"unchecked", "rawtypes"})
public class BukkitNMS implements NMS {

    @SuppressWarnings({"FieldCanBeLocal", "unused"})
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
            } else if (version <= 15) {
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
            }
            /* else {
                Class<?> typeNMSPlayer = Class.forName("net.minecraft.server.level.EntityPlayer");
                this.playerConnection = typeNMSPlayer.getField("b");
                Class<?> typePlayerConnection = Class.forName("net.minecraft.server.network.PlayerConnection");
                this.sendPacket = typePlayerConnection.getMethod("sendPacket", Class.forName("net.minecraft.network.protocol.Packet"));

                this.block = Class.forName("net.minecraft.world.level.block.Block");
                this.tileEntityChest = Class.forName("net.minecraft.world.level.block.entity.TileEntityChest");
                this.worldServer = Class.forName("net.minecraft.server.level.WorldServer");
                this.blockPosition = Class.forName("net.minecraft.core.BlockPosition");
                this.chatMessageType = (Class<Enum>) Class.forName("net.minecraft.network.chat.ChatMessageType");
                this.packetPlayOutChat = Class.forName("net.minecraft.network.protocol.game.PacketPlayOutChat");
                this.chatBaseComponent = Class.forName("net.minecraft.network.chat.IChatBaseComponent");
                this.chatSerializer = chatBaseComponent.getDeclaredClasses()[0].getMethod("a", String.class);
            }*/
        } catch (ClassNotFoundException | NoSuchMethodException | NoSuchFieldException e) {
            e.printStackTrace();
        }
    }

    @Override
    @SuppressWarnings("deprecation")
    public void setGameRule(SWWorld world, SWGameRule rule, Object value) {
        World bukkitWorld = Bukkit.getWorld(world.getName());
        if (bukkitWorld == null) return;
        bukkitWorld.setGameRuleValue(rule.getMinecraftId(), value.toString());
    }

    @Override
    public void sendActionbar(SWPlayer player, String message) {
        if (!(player instanceof BukkitSWPlayer)) return;
        BukkitSWPlayer bukkitSWPlayer = (BukkitSWPlayer) player;

        try {
            Object packet;
            if (version >= 16) {
                ((BukkitSWPlayer) player).getPlayer().spigot().sendMessage(net.md_5.bungee.api.ChatMessageType.ACTION_BAR, player.getUuid(), new net.md_5.bungee.api.chat.TextComponent(message));
                return;
            } else if (version >= 12) {
                Object baseComponent = chatSerializer.invoke(null, "{\"text\": \"" + message + "\"}");
                packet = packetPlayOutChat.getDeclaredConstructor(chatBaseComponent, chatMessageType).newInstance(baseComponent, Enum.valueOf(chatMessageType, "CHAT"));
            } else {
                Object baseComponent = chatSerializer.invoke(null, "{\"text\": \"" + message + "\"}");
                packet = packetPlayOutChat.getDeclaredConstructor(chatBaseComponent, byte.class).newInstance(baseComponent, (byte) 2);
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

            Object packet;
            if (version >= 16) {
                ((BukkitSWPlayer) player).getPlayer().spigot().sendMessage(net.md_5.bungee.api.ChatMessageType.SYSTEM, new net.md_5.bungee.api.chat.TextComponent(message));
                return;
            } else if (version >= 12) {
                Object baseComponent = chatSerializer.invoke(null, message);
                packet = packetPlayOutChat.getDeclaredConstructor(chatBaseComponent, chatMessageType).newInstance(baseComponent, Enum.valueOf(chatMessageType, "SYSTEM"));
            } else {
                Object baseComponent = chatSerializer.invoke(null, message);
                packet = packetPlayOutChat.getDeclaredConstructor(chatBaseComponent, byte.class).newInstance(baseComponent, (byte) 2);
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
                block.getMethod("setData", byte.class).invoke(bukkitBlock, item.getDamage());
            } catch (Exception ignored) {
            }
        }
    }

    @Override
    public void setChestOpen(SWCoord loc, boolean open) {
        if (loc.world() == null || !(loc.world() instanceof BukkitSWWorld)) return;
        World world = ((BukkitSWWorld) loc.world()).getBukkitWorld();

        try {
            if (version >= 16) {
                Block block = world.getBlockAt(loc.x(), loc.y(), loc.z());
                Chest chest = (Chest) block.getState();
                if (open) chest.open();
                else chest.close();
            } else {
                Object position = blockPosition.getDeclaredConstructor(int.class, int.class, int.class).newInstance(loc.x(), loc.y(), loc.z());
                Object serverWorld = craftWorld.getMethod("getHandle").invoke(craftWorld.cast(world));
                Object chestBlock = worldServer.getMethod("getTileEntity", blockPosition).invoke(serverWorld, position);
                Object tileChest = tileEntityChest.cast(chestBlock);

                Object blockChest = tileEntityChest.getMethod(version == 8 ? "w" : "getBlock").invoke(tileChest);
                Object tileBlock = blockChest.getClass().getMethod("getBlockData").invoke(blockChest);
                Object blockThing = tileBlock.getClass().getMethod("getBlock").invoke(tileBlock);
                worldServer.getMethod("playBlockAction", blockPosition, block, int.class, int.class).invoke(serverWorld, position, blockThing, 1, open ? 1 : 0);
            }
        } catch (Exception error) {
            error.printStackTrace();
        }
    }

}
