package net.gcnt.skywarsreloaded.bukkit.game.kits;

import net.gcnt.skywarsreloaded.SkyWarsReloaded;
import net.gcnt.skywarsreloaded.bukkit.utils.BukkitEffect;
import net.gcnt.skywarsreloaded.bukkit.utils.BukkitItem;
import net.gcnt.skywarsreloaded.bukkit.wrapper.player.BukkitSWPlayer;
import net.gcnt.skywarsreloaded.game.kits.AbstractSWKit;
import net.gcnt.skywarsreloaded.wrapper.entity.SWPlayer;
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
        updateItems();
    }

    public void updateItems() {
        this.helmet = getHelmet() == null ? null : ((BukkitItem) getHelmet()).getBukkitItem();
        this.chestplate = getChestplate() == null ? null : ((BukkitItem) getChestplate()).getBukkitItem();
        this.leggings = getLeggings() == null ? null : ((BukkitItem) getLeggings()).getBukkitItem();
        this.boots = getBoots() == null ? null : ((BukkitItem) getBoots()).getBukkitItem();
        this.offHand = getOffHand() == null ? null : ((BukkitItem) getOffHand()).getBukkitItem();

        this.inventory = new HashMap<>();
        getContents().forEach((slot, item) -> inventory.put(slot, item == null ? null : ((BukkitItem) item).getBukkitItem()));

        this.effects = new ArrayList<>();
        getEffects().forEach(s -> {
            try {
                effects.add(new BukkitEffect(plugin, s));
            } catch (Exception ignored) {
            }
        });
    }

    @Override
    public void giveToPlayer(SWPlayer swp) {
        final Player p = ((BukkitSWPlayer) swp).getPlayer();
        final PlayerInventory playerInv = p.getInventory();

        // clearing inventory and effects.
        playerInv.setArmorContents(new ItemStack[4]);
        playerInv.clear();
        for (PotionEffectType value : PotionEffectType.values()) {
            if (value != null) p.removePotionEffect(value);
        }

        playerInv.setHelmet(this.helmet);
        playerInv.setChestplate(this.chestplate);
        playerInv.setLeggings(this.leggings);
        playerInv.setBoots(this.boots);
        this.inventory.forEach(playerInv::setItem);

        if (plugin.getUtils().getServerVersion() >= 9) {
            playerInv.setItemInOffHand(this.offHand);
        }

        effects.forEach(bukkitEffect -> {
            if (bukkitEffect != null) bukkitEffect.applyToPlayer(swp);
        });
    }
}
