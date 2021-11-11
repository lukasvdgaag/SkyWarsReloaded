package net.gcnt.skywarsreloaded.bukkit.game.kits;

import net.gcnt.skywarsreloaded.SkyWarsReloaded;
import net.gcnt.skywarsreloaded.bukkit.utils.BukkitEffect;
import net.gcnt.skywarsreloaded.bukkit.utils.BukkitItem;
import net.gcnt.skywarsreloaded.bukkit.wrapper.BukkitSWPlayer;
import net.gcnt.skywarsreloaded.game.GamePlayer;
import net.gcnt.skywarsreloaded.game.kits.AbstractSWKit;
import net.gcnt.skywarsreloaded.wrapper.SWPlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class BukkitSWKit extends AbstractSWKit {

    private final ItemStack helmet;
    private final ItemStack chestplate;
    private final ItemStack leggings;
    private final ItemStack boots;
    private final HashMap<Integer, ItemStack> inventory;

    private final List<BukkitEffect> effects;

    public BukkitSWKit(SkyWarsReloaded plugin, String id) {
        super(plugin, id);
        this.helmet = getHelmet() == null ? null : ((BukkitItem) getHelmet()).getBukkitItem();
        this.chestplate = getChestplate() == null ? null : ((BukkitItem) getChestplate()).getBukkitItem();
        this.leggings = getLeggings() == null ? null : ((BukkitItem) getLeggings()).getBukkitItem();
        this.boots = getBoots() == null ? null : ((BukkitItem) getBoots()).getBukkitItem();

        this.inventory = new HashMap<>();
        getContents().forEach((slot, item) -> inventory.put(slot, item == null ? null : ((BukkitItem) item).getBukkitItem()));

        this.effects = new ArrayList<>();
        getEffects().forEach(s -> effects.add(new BukkitEffect(plugin, s)));
    }

    @Override
    public void givePlayer(GamePlayer sp) {
        final SWPlayer swp = sp.getSWPlayer();
        final Player p = ((BukkitSWPlayer) swp).getPlayer();
        final PlayerInventory inventory = p.getInventory();

        // clearing inventory and effects.
        inventory.setArmorContents(new ItemStack[4]);
        inventory.clear();
        for (PotionEffectType value : PotionEffectType.values()) {
            p.removePotionEffect(value);
        }

        inventory.setHelmet(this.helmet);
        inventory.setChestplate(this.chestplate);
        inventory.setLeggings(this.leggings);
        inventory.setBoots(this.boots);
        this.inventory.forEach(inventory::setItem);

        effects.forEach(bukkitEffect -> {
            if (bukkitEffect != null) bukkitEffect.applyToPlayer(swp);
        });
    }
}
