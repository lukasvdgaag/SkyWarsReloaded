package com.walrusone.skywarsreloaded.managers.holograms;

import com.gmail.filoghost.holographicdisplays.api.Hologram;
import com.gmail.filoghost.holographicdisplays.api.HologramsAPI;
import com.walrusone.skywarsreloaded.SkyWarsReloaded;
import com.walrusone.skywarsreloaded.enums.LeaderType;
import org.bukkit.Location;
import org.bukkit.inventory.ItemStack;

public class HolographicHologramManager extends AbstractHologramsManager<Hologram> {

    public HolographicHologramManager(SkyWarsReloaded plugin) {
        super(plugin);
    }

    @Override
    protected Hologram createHologram(Location loc, LeaderType type, String formatKey) {
        return HologramsAPI.createHologram(plugin, loc);
    }

    @Override
    protected void clearHologramLines(Hologram hologram) {
        hologram.clearLines();
    }

    @Override
    protected void insertTextLine(Hologram hologram, int index, String text) {
        hologram.insertTextLine(index, text);
    }

    @Override
    protected void insertItemLine(Hologram hologram, int index, ItemStack item) {
        hologram.insertItemLine(index, item);
    }

    @Override
    protected boolean isDeleted(Hologram hologram) {
        return hologram.isDeleted();
    }

    @Override
    protected void deleteHologram(Hologram hologram) {
        hologram.delete();
    }

    @Override
    protected Location getHologramLocation(Hologram hologram) {
        return hologram.getLocation();
    }
}
