package com.walrusone.skywarsreloaded.nms.v1_20_R1;

import org.bukkit.NamespacedKey;
import org.bukkit.Registry;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.potion.PotionEffectType;

import java.util.Locale;

public class NMSHandler extends com.walrusone.skywarsreloaded.nms.v1_19_R1.NMSHandler {

    public int getVersion() {
        return 20;
    }

    @Override
    public PotionEffectType getPotionEffectTypeByName(String... name) {
        for (String n : name) {
            try {
                PotionEffectType type = Registry.EFFECT.get(NamespacedKey.fromString(n.toLowerCase(Locale.ROOT)));
                if (type != null) {
                    return type;
                }
            } catch (Exception e) {
                // ignore
            }
        }
        return null;
    }

    @Override
    public Enchantment getEnchantmentByName(String... name) {
        for (String n : name) {
            try {
                Enchantment enchantment = Registry.ENCHANTMENT.get(NamespacedKey.fromString(n.toLowerCase(Locale.ROOT)));
                if (enchantment != null) {
                    return enchantment;
                }
            } catch (Exception e) {
                // ignore
            }
        }
        return null;
    }
}
