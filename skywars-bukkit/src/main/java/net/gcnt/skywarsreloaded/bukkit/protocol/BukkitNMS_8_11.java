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
import org.bukkit.inventory.ItemStack;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class BukkitNMS_8_11 implements NMS {

    protected final SkyWarsReloaded plugin;
    protected final String serverVersion;
    protected final int version;

    protected Class<?> chatBaseComponent;
    protected Class<?> packetPlayOutChat;
    @SuppressWarnings("rawtypes")
    protected Class<Enum> chatMessageType;
    protected Class<?> blockPosition;
    protected Class<?> worldServer;
    protected Class<?> craftWorld;
    protected Class<?> tileEntityChest;
    protected Class<?> block;

    protected Method chatSerializer;
    protected Method getHandle;
    protected Method sendPacket;
    protected Field playerConnection;

    public BukkitNMS_8_11(SkyWarsReloaded plugin, String serverPackage) {
        this.plugin = plugin;
        this.serverVersion = serverPackage.substring(serverPackage.lastIndexOf('.') + 1);
        this.version = plugin.getUtils().getServerVersion();

        try {
            this.init();
        } catch (ClassNotFoundException | NoSuchFieldException | NoSuchMethodException e) {
            e.printStackTrace();
        }
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    public void init() throws ClassNotFoundException, NoSuchFieldException, NoSuchMethodException {
        // 1.8.8 - 1.15.2
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

    @Override
    public void setBlock(SWCoord loc, Item item) {
        if (loc.world() == null || !(loc.world() instanceof BukkitSWWorld) || !(item instanceof BukkitItem)) return;
        World world = ((BukkitSWWorld) loc.world()).getBukkitWorld();
        ItemStack itemStack = ((BukkitItem) item).getBukkitItem();

        Block bukkitBlock = world.getBlockAt(loc.x(), loc.y(), loc.z());
        bukkitBlock.setType(itemStack.getType());
        try {
            block.getMethod("setData", byte.class).invoke(bukkitBlock, item.getDamage());
        } catch (Exception ignored) {}
    }

    @Override
    @SuppressWarnings("deprecation")
    public void setGameRule(SWWorld world, SWGameRule rule, Object value) {
        World bukkitWorld = Bukkit.getWorld(world.getName());
        if (bukkitWorld == null) return;
        bukkitWorld.setGameRuleValue(rule.getMinecraftId(), String.valueOf(value));
    }

    @Override
    public void sendActionbar(SWPlayer player, String message) {
        if (!(player instanceof BukkitSWPlayer)) return;
        BukkitSWPlayer bukkitSWPlayer = (BukkitSWPlayer) player;

        try {
            Object baseComponent = chatSerializer.invoke(null, "{\"text\": \"" + message + "\"}");
            Object packet = packetPlayOutChat.getDeclaredConstructor(chatBaseComponent, byte.class).newInstance(baseComponent, (byte) 2);

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
            Object packet = packetPlayOutChat.getDeclaredConstructor(chatBaseComponent, byte.class)
                    .newInstance(baseComponent, (byte) 2);

            Object nmsPlayer = getHandle.invoke(bukkitSWPlayer.getPlayer());
            Object connection = playerConnection.get(nmsPlayer);
            sendPacket.invoke(connection, packet);
        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException | InstantiationException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void setChestOpen(SWCoord loc, boolean open) {
        if (loc.world() == null || !(loc.world() instanceof BukkitSWWorld)) return;
        World world = ((BukkitSWWorld) loc.world()).getBukkitWorld();

        try {
            Object position = blockPosition.getDeclaredConstructor(int.class, int.class, int.class)
                    .newInstance(loc.x(), loc.y(), loc.z());
            Object serverWorld = craftWorld.getMethod("getHandle").invoke(craftWorld.cast(world));
            Object chestBlock = worldServer.getMethod("getTileEntity", blockPosition).invoke(serverWorld, position);
            Object tileChest = tileEntityChest.cast(chestBlock);

            Object blockChest = tileEntityChest.getMethod(version == 8 ? "w" : "getBlock").invoke(tileChest);
            Object tileBlock = blockChest.getClass().getMethod("getBlockData").invoke(blockChest);
            Object blockThing = tileBlock.getClass().getMethod("getBlock").invoke(tileBlock);
            worldServer.getMethod("playBlockAction", blockPosition, block, int.class, int.class)
                    .invoke(serverWorld, position, blockThing, 1, open ? 1 : 0);
        } catch (Exception error) {
            error.printStackTrace();
        }
    }
}
