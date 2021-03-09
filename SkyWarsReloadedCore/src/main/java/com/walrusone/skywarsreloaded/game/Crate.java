package com.walrusone.skywarsreloaded.game;

import com.walrusone.skywarsreloaded.SkyWarsReloaded;
import com.walrusone.skywarsreloaded.utilities.Messaging;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.inventory.Inventory;

public class Crate {
    private Location loc;
    private Inventory inv = org.bukkit.Bukkit.createInventory(null, 27, new Messaging.MessageFormatter().format("event.crateInv"));
    private Entity ent;
    private boolean success = false;
    private double prevY;

    Crate(Location loc, int max) {
        this.loc = loc.clone();
        SkyWarsReloaded.getCM().fillCrate(inv, max);
        ent = SkyWarsReloaded.getNMS().spawnFallingBlock(loc.add(0.0D, 40.0D, 0.0D), Material.SAND, false);
        prevY = ent.getLocation().getY();
        checkSuccess();
    }


    private void checkSuccess() {
        new org.bukkit.scheduler.BukkitRunnable() {
            public void run() {
                if ((ent.getLocation().getBlockY() == prevY) && (!success)) {
                    ent.getWorld().getBlockAt(ent.getLocation()).setType(Material.ENDER_CHEST);
                    setLocation(ent.getWorld().getBlockAt(ent.getLocation()));
                    ent.remove();
                } else {
                    prevY = ent.getLocation().getY();
                    Crate.this.checkSuccess();
                }
            }
        }.runTaskLater(SkyWarsReloaded.get(), 10L);
    }

    public Location getLocation() {
        return loc;
    }

    public void setLocation(Block block) {
        loc = block.getLocation().clone();
        success = true;
    }

    public Inventory getInventory() {
        return inv;
    }

    public Entity getEntity() {
        return ent;
    }
}
