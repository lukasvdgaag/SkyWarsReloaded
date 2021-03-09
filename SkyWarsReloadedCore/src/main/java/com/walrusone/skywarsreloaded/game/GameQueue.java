package com.walrusone.skywarsreloaded.game;

import com.walrusone.skywarsreloaded.SkyWarsReloaded;
import com.walrusone.skywarsreloaded.managers.MatchManager;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;


public class GameQueue {
    private List<PlayerCard> queue = new ArrayList<>();
    private GameMap map;
    private BukkitRunnable runnableQueue;

    GameQueue(GameMap g) {
        map = g;
    }

    public void add(PlayerCard pCard) {
        queue.add(pCard);
    }

    private void sendToGame() {
        if (SkyWarsReloaded.get().isEnabled()) {
            final PlayerCard pCard = queue.get(0);
            if (SkyWarsReloaded.getCfg().debugEnabled()) {
                if (pCard != null) {
                    SkyWarsReloaded.get().getLogger().info(
                            "#GameQueue:sendToGame: pCard uuid " + pCard.getUUID());
                }
            }
            MatchManager.get().teleportToArena(map, pCard);
            queue.remove(0);
        }
    }

    public void start() {
        queue.clear();
        runnableQueue = new BukkitRunnable() {
            @Override
            public void run() {
                if (queue.isEmpty()) return;
                sendToGame();
            }
        };
        runnableQueue.runTaskTimer(SkyWarsReloaded.get(), 0, 2);
    }

    public void kill() {
        if (runnableQueue != null)
            runnableQueue.cancel();
    }
}
