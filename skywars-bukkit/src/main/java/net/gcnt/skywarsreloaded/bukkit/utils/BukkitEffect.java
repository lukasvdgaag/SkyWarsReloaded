package net.gcnt.skywarsreloaded.bukkit.utils;

import net.gcnt.skywarsreloaded.SkyWarsReloaded;
import net.gcnt.skywarsreloaded.bukkit.wrapper.BukkitSWPlayer;
import net.gcnt.skywarsreloaded.utils.AbstractEffect;
import net.gcnt.skywarsreloaded.wrapper.SWPlayer;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class BukkitEffect extends AbstractEffect {

    private PotionEffect effect;

    public BukkitEffect(SkyWarsReloaded plugin, String input) {
        super(plugin, input);
        try {
            effect = new PotionEffect(PotionEffectType.getByName(getType()), getDuration(), getStrength(), true, showParticles());
        } catch (Exception e) {
            plugin.getLogger().error(String.format("Failed to load bukkit effect from string %s. Using the default: %d. (%s)", input, 1, e.getClass().getName() + ": " + e.getLocalizedMessage()));
        }
    }

    @Override
    public void applyToPlayer(SWPlayer player) {
        Player p = ((BukkitSWPlayer) player).getPlayer();
        p.addPotionEffect(effect);
    }
}
