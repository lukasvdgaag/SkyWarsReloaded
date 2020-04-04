package com.walrusone.skywarsreloaded.utilities;

import com.walrusone.skywarsreloaded.SkyWarsReloaded;
import net.milkbowl.vault.chat.Chat;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.economy.EconomyResponse;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;

public class VaultUtils {
    private static VaultUtils instance;
    private Economy econ = null;
    private Chat chat = null;

    private VaultUtils() {
        if ((!setupEconomy()) && (!setupChat())) {
            SkyWarsReloaded.get().getLogger().info("ERROR: Vault Dependency was not found. Install Vault or turn off Economy in the Config!");
        }
    }

    public static VaultUtils get() {
        if (instance == null) {
            instance = new VaultUtils();
        }
        return instance;
    }

    private boolean setupEconomy() {
        if (Bukkit.getServer().getPluginManager().getPlugin("Vault") == null) {
            return false;
        }
        RegisteredServiceProvider<Economy> rsp = Bukkit.getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            return false;
        }
        econ = ((Economy) rsp.getProvider());
        return econ != null;
    }

    private boolean setupChat() {
        RegisteredServiceProvider<Chat> rsp = Bukkit.getServer().getServicesManager().getRegistration(Chat.class);
        chat = ((Chat) rsp.getProvider());
        return chat != null;
    }


    public boolean canBuy(Player player, double cost) {
        return (econ != null) && (econ.getBalance(player) >= cost);
    }

    public boolean payCost(Player player, double cost) {
        if (econ != null) {
            EconomyResponse rp = econ.withdrawPlayer(player, cost);
            return rp.transactionSuccess();
        }
        return false;
    }

    public double getBalance(Player player) {
        if (econ != null) {
            return econ.getBalance(player);
        }
        return 0.0D;
    }

    public void give(Player win, int i) {
        if (econ != null) {
            econ.depositPlayer(win, i);
        }
    }

    public Chat getChat() {
        return chat;
    }
}
