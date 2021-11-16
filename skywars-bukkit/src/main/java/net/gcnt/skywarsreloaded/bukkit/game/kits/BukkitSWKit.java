package net.gcnt.skywarsreloaded.bukkit.game.kits;

import net.gcnt.skywarsreloaded.SkyWarsReloaded;
import net.gcnt.skywarsreloaded.bukkit.utils.BukkitEffect;
import net.gcnt.skywarsreloaded.bukkit.utils.BukkitItem;
import net.gcnt.skywarsreloaded.bukkit.wrapper.player.BukkitSWPlayer;
import net.gcnt.skywarsreloaded.game.kits.AbstractSWKit;
import net.gcnt.skywarsreloaded.wrapper.player.SWPlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class BukkitSWKit extends AbstractSWKit {

    private ItemStack helmet;
    private ItemStack chestplate;
    private ItemStack leggings;
    private ItemStack boots;
    private ItemStack offHand;
    private HashMap<Integer, ItemStack> inventory;

    private List<BukkitEffect> effects;

    public BukkitSWKit(SkyWarsReloaded plugin, String id) {
        super(plugin, id);
    }

    @Override
    public synchronized void loadData() {
        super.loadData();
        this.helmet = getHelmet() == null ? null : ((BukkitItem) getHelmet()).getBukkitItem();
        this.chestplate = getChestplate() == null ? null : ((BukkitItem) getChestplate()).getBukkitItem();
        this.leggings = getLeggings() == null ? null : ((BukkitItem) getLeggings()).getBukkitItem();
        this.boots = getBoots() == null ? null : ((BukkitItem) getBoots()).getBukkitItem();
        this.offHand = getOffHand() == null ? null : ((BukkitItem) getOffHand()).getBukkitItem();

        this.inventory = new HashMap<>();
        getContents().forEach((slot, item) -> inventory.put(slot, item == null ? null : ((BukkitItem) item).getBukkitItem()));

        this.effects = new ArrayList<>();
        getEffects().forEach(s -> effects.add(new BukkitEffect(plugin, s)));
    }

    @Override
    public void giveToPlayer(SWPlayer swp) {
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
        if (plugin.getUtils().getServerVersion() >= 9) {
            inventory.setItemInOffHand(this.offHand);
        }

        effects.forEach(bukkitEffect -> {
            if (bukkitEffect != null) bukkitEffect.applyToPlayer(swp);
        });
    }
}
