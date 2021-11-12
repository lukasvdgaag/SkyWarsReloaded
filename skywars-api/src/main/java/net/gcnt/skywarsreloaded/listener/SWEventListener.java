package net.gcnt.skywarsreloaded.listener;

import net.gcnt.skywarsreloaded.wrapper.SWOfflinePlayer;
import net.gcnt.skywarsreloaded.wrapper.SWPlayer;

public interface SWEventListener {

    void onAsyncPlayerPreLogin(SWOfflinePlayer player);

    void onPlayerJoin(SWPlayer player);

    void onPlayerQuit(SWPlayer player);

    void onPlayerInteract(SWPlayer player);

    void onPlayerBlockBreak(SWPlayer player);

    void onPlayerBlockPlace(SWPlayer player);

}
