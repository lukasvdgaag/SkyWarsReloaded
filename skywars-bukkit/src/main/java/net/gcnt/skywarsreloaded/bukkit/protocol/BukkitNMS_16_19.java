package net.gcnt.skywarsreloaded.bukkit.protocol;

import net.gcnt.skywarsreloaded.bukkit.BukkitSkyWarsReloaded;
import net.gcnt.skywarsreloaded.bukkit.wrapper.player.BukkitSWPlayer;
import net.gcnt.skywarsreloaded.bukkit.wrapper.world.BukkitSWWorld;
import net.gcnt.skywarsreloaded.utils.SWCoord;
import net.gcnt.skywarsreloaded.wrapper.entity.SWPlayer;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.entity.Player;

public class BukkitNMS_16_19 extends BukkitNMS_14_15 {

    public BukkitNMS_16_19(BukkitSkyWarsReloaded plugin, String serverPackage) {
        super(plugin, serverPackage);
    }

    @Override
    public void initReflection() {
        // Don't do reflection for latest version of the API
    }

    @Override
    public void sendActionbar(SWPlayer player, String message) {
        if (!(player instanceof BukkitSWPlayer)) return;
        BukkitSWPlayer bukkitSWPlayer = (BukkitSWPlayer) player;

        Player bukkitPlayer = bukkitSWPlayer.getPlayer();
        if (bukkitPlayer == null) return;
        bukkitPlayer.spigot().sendMessage(
                net.md_5.bungee.api.ChatMessageType.ACTION_BAR,
                player.getUuid(),
                new net.md_5.bungee.api.chat.TextComponent(message)
        );
    }

    @Override
    public void sendJSONMessage(SWPlayer player, String message) {
        if (!(player instanceof BukkitSWPlayer)) return;
        BukkitSWPlayer bukkitSWPlayer = (BukkitSWPlayer) player;

        Player bukkitPlayer = bukkitSWPlayer.getPlayer();
        if (bukkitPlayer == null) return;
        bukkitPlayer.spigot().sendMessage(
                net.md_5.bungee.api.ChatMessageType.SYSTEM,
                new net.md_5.bungee.api.chat.TextComponent(message));

    }

    @Override
    public void setChestOpen(SWCoord loc, boolean open) {
        if (loc.getWorld() == null || !(loc.getWorld() instanceof BukkitSWWorld)) return;
        World world = ((BukkitSWWorld) loc.getWorld()).getBukkitWorld();

        Block block = world.getBlockAt(loc.x(), loc.y(), loc.z());
        Chest chest = (Chest) block.getState();
        if (open) chest.open();
        else chest.close();
    }

    @Override
    public String getVoidGeneratorSettings() {
        return "{ \"type\": \"minecraft:flat\", \"settings\": { \"biome\": \"minecraft:void\", \"lakes\": false, \"features\": false, \"layers\": [{ \"block\": \"minecraft:air\", \"height\": 1 }] } }";
    }
}
