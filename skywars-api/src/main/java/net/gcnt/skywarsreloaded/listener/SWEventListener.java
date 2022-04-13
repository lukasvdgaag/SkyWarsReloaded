package net.gcnt.skywarsreloaded.listener;

import net.gcnt.skywarsreloaded.wrapper.event.*;

public interface SWEventListener {

    void onAsyncPlayerPreLogin(SWAsyncPlayerPreLoginEvent event);

    void onAsyncPlayerChat(SWAsyncPlayerChatEvent event);

    void onPlayerJoin(SWPlayerJoinEvent event);

    void onPlayerQuit(SWPlayerQuitEvent event);

    void onPlayerInteract(SWPlayerInteractEvent event);

    void onPlayerBlockBreak(SWBlockBreakEvent event);

    void onPlayerBlockPlace(SWBlockPlaceEvent event);

    void onChunkLoad(SWChunkLoadEvent swEvent);

    void onWorldInit(SWWorldInitEvent swEvent);

    void onPlayerMove(SWPlayerMoveEvent swEvent);

    void onPlayerFoodLevelChange(SWPlayerFoodLevelChangeEvent event);

}
