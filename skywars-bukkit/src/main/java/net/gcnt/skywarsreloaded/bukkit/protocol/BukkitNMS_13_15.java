package net.gcnt.skywarsreloaded.bukkit.protocol;

import net.gcnt.skywarsreloaded.SkyWarsReloaded;
import net.gcnt.skywarsreloaded.bukkit.utils.BukkitItem;
import net.gcnt.skywarsreloaded.bukkit.wrapper.world.BukkitSWWorld;
import net.gcnt.skywarsreloaded.utils.Item;
import net.gcnt.skywarsreloaded.utils.SWCoord;
import net.gcnt.skywarsreloaded.wrapper.server.SWGameRule;
import net.gcnt.skywarsreloaded.wrapper.world.SWWorld;
import org.bukkit.Bukkit;
import org.bukkit.GameRule;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.inventory.ItemStack;

public class BukkitNMS_13_15 extends BukkitNMS_12 {

    public BukkitNMS_13_15(SkyWarsReloaded plugin, String serverPackage) {
        super(plugin, serverPackage);
    }

    @Override
    @SuppressWarnings("unchecked")
    public void setGameRule(SWWorld world, SWGameRule rule, Object value) {
        World bukkitWorld = Bukkit.getWorld(world.getName());
        if (bukkitWorld == null) return;
        if (rule.getValueType().equals(Integer.class)) {

            int intValue;
            if (value instanceof Integer) {
                intValue = (Integer) value;
            } else if (value instanceof String) {
                intValue = Integer.parseInt((String) value);
            } else {
                return;
            }

            GameRule<?> gameRule = GameRule.getByName(rule.getMinecraftId());
            if (gameRule == null) return;
            bukkitWorld.setGameRule((GameRule<Integer>) gameRule, intValue);
        } else if (rule.getValueType().equals(Boolean.class)) {

            boolean boolValue;
            if (value instanceof Boolean) {
                boolValue = (Boolean) value;
            } else if (value instanceof String) {
                boolValue = Boolean.parseBoolean((String) value);
            } else {
                return;
            }

            GameRule<?> gameRule = GameRule.getByName(rule.getMinecraftId());
            if (gameRule == null) return;
            bukkitWorld.setGameRule((GameRule<Boolean>) gameRule, boolValue);
        }
    }

    @Override
    public void setBlock(SWCoord loc, Item item) {
        if (loc.getWorld() == null || !(loc.getWorld() instanceof BukkitSWWorld) || !(item instanceof BukkitItem)) return;
        World world = ((BukkitSWWorld) loc.getWorld()).getBukkitWorld();
        ItemStack itemStack = ((BukkitItem) item).getBukkitItem();

        Block bukkitBlock = world.getBlockAt(loc.x(), loc.y(), loc.z());
        bukkitBlock.setType(itemStack.getType());
    }
}
