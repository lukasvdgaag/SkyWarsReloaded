package com.walrusone.skywarsreloaded.nms.v1_12_R1;

import com.walrusone.skywarsreloaded.game.signs.SWRSign;
import com.walrusone.skywarsreloaded.nms.NMS;
import net.minecraft.server.v1_12_R1.*;
import net.minecraft.server.v1_12_R1.IChatBaseComponent.ChatSerializer;
import net.minecraft.server.v1_12_R1.PacketPlayOutTitle.EnumTitleAction;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.Skull;
import org.bukkit.craftbukkit.v1_12_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftFallingBlock;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftPlayer;
import org.bukkit.entity.Entity;
import org.bukkit.entity.FallingBlock;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityTargetEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.generator.ChunkGenerator;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.scoreboard.Scoreboard;

import java.util.List;
import java.util.Random;

public class NMSHandler implements NMS {
    public NMSHandler() {
    }

    @Override
    public SWRSign createSWRSign(String name, Location location) {
        return new SWRSign12(name, location);
    }

    public boolean removeFromScoreboardCollection(Scoreboard scoreboard) {
        return false;
    }

    public void respawnPlayer(Player player) {
        ((org.bukkit.craftbukkit.v1_12_R1.CraftServer) Bukkit.getServer()).getHandle().moveToWorld(((CraftPlayer) player).getHandle(), 0, false);
    }

    public void sendParticles(World world, String type, float x, float y, float z, float offsetX, float offsetY, float offsetZ, float data, int amount) {
        EnumParticle particle = EnumParticle.valueOf(type);
        net.minecraft.server.v1_12_R1.PacketPlayOutWorldParticles particles = new net.minecraft.server.v1_12_R1.PacketPlayOutWorldParticles(particle, true, x, y, z, offsetX, offsetY, offsetZ, data, amount, new int[]{1});
        for (Player player : world.getPlayers()) {
            CraftPlayer start = (CraftPlayer) player;
            EntityPlayer target = start.getHandle();
            PlayerConnection connect = target.playerConnection;
            connect.sendPacket(particles);
        }
    }

    public FireworkEffect getFireworkEffect(Color one, Color two, Color three, Color four, Color five, FireworkEffect.Type type) {
        return FireworkEffect.builder().flicker(false).withColor(new Color[]{one, two, three, four}).withFade(five).with(type).trail(true).build();
    }

    public void sendTitle(Player player, int fadein, int stay, int fadeout, String title, String subtitle) {
        PlayerConnection pConn = ((CraftPlayer) player).getHandle().playerConnection;
        PacketPlayOutTitle pTitleInfo = new PacketPlayOutTitle(EnumTitleAction.TIMES, null, fadein, stay, fadeout);
        pConn.sendPacket(pTitleInfo);
        if (subtitle != null) {
            subtitle = subtitle.replaceAll("%player%", player.getDisplayName());
            subtitle = ChatColor.translateAlternateColorCodes('&', subtitle);
            IChatBaseComponent iComp = ChatSerializer.a("{\"text\": \"" + subtitle + "\"}");
            PacketPlayOutTitle pSubtitle = new PacketPlayOutTitle(EnumTitleAction.SUBTITLE, iComp);
            pConn.sendPacket(pSubtitle);
        }
        if (title != null) {
            title = title.replaceAll("%player%", player.getDisplayName());
            title = ChatColor.translateAlternateColorCodes('&', title);
            IChatBaseComponent iComp = ChatSerializer.a("{\"text\": \"" + title + "\"}");
            PacketPlayOutTitle pTitle = new PacketPlayOutTitle(EnumTitleAction.TITLE, iComp);
            pConn.sendPacket(pTitle);
        }
    }

    public void sendActionBar(Player p, String msg) {
        String s = ChatColor.translateAlternateColorCodes('&', msg);
        IChatBaseComponent icbc = ChatSerializer.a("{\"text\": \"" + s + "\"}");
        PacketPlayOutChat bar = new PacketPlayOutChat(icbc, ChatMessageType.GAME_INFO);
        ((CraftPlayer) p).getHandle().playerConnection.sendPacket(bar);
    }

    public String getItemName(org.bukkit.inventory.ItemStack item) {
        return org.bukkit.craftbukkit.v1_12_R1.inventory.CraftItemStack.asNMSCopy(item).getName();
    }

    public void playGameSound(Location loc, String sound, float volume, float pitch, boolean customSound) {
        if (customSound) {
            loc.getWorld().playSound(loc, sound, volume, pitch);
        } else {
            loc.getWorld().playSound(loc, Sound.valueOf(sound), volume, pitch);
        }
    }

    public org.bukkit.inventory.ItemStack getMainHandItem(Player player) {
        return player.getInventory().getItemInMainHand();
    }

    public org.bukkit.inventory.ItemStack getOffHandItem(Player player) {
        return player.getInventory().getItemInOffHand();
    }

    public org.bukkit.inventory.ItemStack getItemStack(Material material, List<String> lore, String message) {
        org.bukkit.inventory.ItemStack addItem = new org.bukkit.inventory.ItemStack(material, 1);
        ItemMeta addItemMeta = addItem.getItemMeta();
        addItemMeta.setDisplayName(message);
        addItemMeta.setLore(lore);
        addItemMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        addItemMeta.addItemFlags(ItemFlag.HIDE_DESTROYS);
        addItemMeta.addItemFlags(ItemFlag.HIDE_POTION_EFFECTS);
        addItemMeta.addItemFlags(ItemFlag.HIDE_PLACED_ON);
        addItemMeta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);
        addItemMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        addItem.setItemMeta(addItemMeta);
        return addItem;
    }

    public org.bukkit.inventory.ItemStack getItemStack(org.bukkit.inventory.ItemStack item, List<String> lore, String message) {
        org.bukkit.inventory.ItemStack addItem = item.clone();
        ItemMeta addItemMeta = addItem.getItemMeta();
        addItemMeta.setDisplayName(message);
        addItemMeta.setLore(lore);
        addItemMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        addItemMeta.addItemFlags(ItemFlag.HIDE_DESTROYS);
        addItemMeta.addItemFlags(ItemFlag.HIDE_POTION_EFFECTS);
        addItemMeta.addItemFlags(ItemFlag.HIDE_PLACED_ON);
        addItemMeta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);
        addItemMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        addItem.setItemMeta(addItemMeta);
        return addItem;
    }

    public boolean isValueParticle(String string) {
        try {
            Particle.valueOf(string);
        } catch (IllegalArgumentException e) {
            return false;
        }
        return true;
    }

    public void updateSkull(Skull skull, java.util.UUID uuid) {
        skull.setOwningPlayer(Bukkit.getOfflinePlayer(uuid));
    }

    public void setMaxHealth(Player player, int health) {
        player.getAttribute(org.bukkit.attribute.Attribute.GENERIC_MAX_HEALTH).setBaseValue(health);
    }

    public void spawnDragon(World world, Location loc) {
        WorldServer w = ((CraftWorld) world).getHandle();
        EntityEnderDragon dragon = new EntityEnderDragon(w);
        dragon.getDragonControllerManager().setControllerPhase(net.minecraft.server.v1_12_R1.DragonControllerPhase.c);
        dragon.setLocation(loc.getX(), loc.getY(), loc.getZ(), w.random.nextFloat() * 360.0F, 0.0F);
        w.addEntity(dragon);
    }

    public Entity spawnFallingBlock(Location loc, Material mat, boolean damage) {
        FallingBlock block = loc.getWorld().spawnFallingBlock(loc, new org.bukkit.material.MaterialData(mat));
        block.setDropItem(false);
        EntityFallingBlock fb = ((CraftFallingBlock) block).getHandle();
        fb.a(damage);
        return block;
    }

    public void playEnderChestAction(Block block, boolean open) {
        Location location = block.getLocation();
        WorldServer world = ((CraftWorld) location.getWorld()).getHandle();
        net.minecraft.server.v1_12_R1.BlockPosition position = new net.minecraft.server.v1_12_R1.BlockPosition(location.getX(), location.getY(), location.getZ());
        TileEntityEnderChest ec = (TileEntityEnderChest) world.getTileEntity(position);
        if (ec != null) {
            world.playBlockAction(position, ec.getBlock(), 1, open ? 1 : 0);
        }
    }

    public void setEntityTarget(Entity ent, Player player) {
        EntityCreature entity = (EntityCreature) ((org.bukkit.craftbukkit.v1_12_R1.entity.CraftEntity) ent).getHandle();
        entity.setGoalTarget(((CraftPlayer) player).getHandle(), EntityTargetEvent.TargetReason.CLOSEST_PLAYER, true);
    }

    public void updateSkull(SkullMeta meta1, Player player) {
        meta1.setOwner(player.getName());
    }

    public ChunkGenerator getChunkGenerator() {
        return new ChunkGenerator() {
            public final ChunkData generateChunkData(World world, Random random, int x, int z, BiomeGrid chunkGererator) {
                ChunkData chunkData = createChunkData(world);
                for (int i = 0; i < 16; i++) {
                    for (int j = 0; j < 16; j++) {
                        chunkGererator.setBiome(i, j, org.bukkit.block.Biome.valueOf("VOID"));
                    }
                }
                return chunkData;
            }
        };
    }

    public boolean checkMaterial(FallingBlock fb, Material mat) {
        return fb.getMaterial().equals(mat);
    }

    public org.bukkit.scoreboard.Objective getNewObjective(Scoreboard scoreboard, String criteria, String DisplayName) {
        return scoreboard.registerNewObjective(DisplayName, criteria);
    }

    public void setGameRule(World world, String rule, String bool) {
        world.setGameRuleValue(rule, bool);
    }

    public boolean headCheck(Block h1) {
        return h1.getType() == Material.matchMaterial("SKULL");
    }

    public org.bukkit.inventory.ItemStack getBlankPlayerHead() {
        return new org.bukkit.inventory.ItemStack(Material.matchMaterial("SKULL_ITEM"), 1, (short) 3);
    }

    public int getVersion() {
        return 12;
    }

    public org.bukkit.inventory.ItemStack getMaterial(String item) {
        if (item.equalsIgnoreCase("SKULL_ITEM")) {
            return new org.bukkit.inventory.ItemStack(Material.matchMaterial("SKULL_ITEM"), 1, (short) 1);
        }
        return new org.bukkit.inventory.ItemStack(Material.valueOf(item), 1);
    }


    public org.bukkit.inventory.ItemStack getColorItem(String mat, byte color) {
        if (mat.equalsIgnoreCase("wool"))
            return new org.bukkit.inventory.ItemStack(Material.matchMaterial("WOOL"), 1,  color);
        if (mat.equalsIgnoreCase("glass"))
            return new org.bukkit.inventory.ItemStack(Material.matchMaterial("STAINED_GLASS"), 1,  color);
        if (mat.equalsIgnoreCase("banner")) {
            return new org.bukkit.inventory.ItemStack(Material.matchMaterial("BANNER"), 1,  color);
        }
        return new org.bukkit.inventory.ItemStack(Material.matchMaterial("STAINED_GLASS"), 1,  color);
    }


    public void setBlockWithColor(World world, int x, int y, int z, Material mat, byte cByte) {
        world.getBlockAt(x, y, z).setType(mat);
        world.getBlockAt(x, y, z).setData(cByte);
    }


    public void deleteCache() {
    }


    public Block getHitBlock(ProjectileHitEvent event) {
        return event.getHitBlock();
    }

    @Override
    public void sendJSON(Player sender, String json) {
        final IChatBaseComponent icbc = IChatBaseComponent.ChatSerializer.a(json);
        final PacketPlayOutChat chat = new PacketPlayOutChat(icbc);
        ((CraftPlayer) sender).getHandle().playerConnection.sendPacket(chat);
    }
}
