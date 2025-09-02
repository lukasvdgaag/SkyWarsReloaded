package com.walrusone.skywarsreloaded.managers.holograms;

import com.walrusone.skywarsreloaded.SkyWarsReloaded;
import com.walrusone.skywarsreloaded.enums.LeaderType;
import eu.decentsoftware.holograms.api.DHAPI;
import eu.decentsoftware.holograms.api.holograms.Hologram;
import org.bukkit.Location;
import org.bukkit.inventory.ItemStack;

public class DecentHologramManager extends AbstractHologramsManager<Hologram> {

    public DecentHologramManager(SkyWarsReloaded plugin) {
        super(plugin);
    }

    @Override
    protected void clearHologramLines(Hologram hologram) {
        DHAPI.removeHologramPage(hologram, 0);
    }

    @Override
    protected void insertTextLine(Hologram hologram, int index, String text) {
        DHAPI.insertHologramLine(hologram, index, text);
    }

    @Override
    protected void insertItemLine(Hologram hologram, int index, ItemStack item) {
        DHAPI.insertHologramLine(hologram, index, item);
    }

    @Override
    protected boolean isDeleted(Hologram hologram) {
        return hologram.isEnabled();
    }

    @Override
    protected void deleteHologram(Hologram hologram) {
        hologram.delete();
    }

    @Override
    protected Location getHologramLocation(Hologram hologram) {
        return hologram.getLocation();
    }

    @Override
    protected Hologram createHologram(Location loc, LeaderType type, String formatKey) {
        final String hologramId = getHologramIdentifier(loc, type, formatKey);
        return DHAPI.createHologram(hologramId, loc);
    }

    private String getHologramIdentifier(Location loc, LeaderType type, String formatKey) {
        return "swr_" + type.toString().toLowerCase() + "_" + formatKey + "_" + loc.getBlockX() + "_" + loc.getBlockY() + "_" + loc.getBlockZ();
    }
}
