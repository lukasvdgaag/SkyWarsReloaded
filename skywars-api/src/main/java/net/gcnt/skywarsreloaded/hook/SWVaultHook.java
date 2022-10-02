package net.gcnt.skywarsreloaded.hook;

import net.gcnt.skywarsreloaded.wrapper.entity.SWPlayer;

public interface SWVaultHook extends SWHook {

    double getBalance(SWPlayer player);

    boolean hasBalance(SWPlayer player, double amount);

    boolean withdraw(SWPlayer player, double amount);

    boolean deposit(SWPlayer player, double amount);

}
