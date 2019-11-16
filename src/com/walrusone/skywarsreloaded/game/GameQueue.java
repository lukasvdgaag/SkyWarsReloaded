package com.walrusone.skywarsreloaded.game;

import com.walrusone.skywarsreloaded.SkyWarsReloaded;
import com.walrusone.skywarsreloaded.managers.MatchManager;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.LinkedList;
import java.util.Queue;


public class GameQueue {
    private Queue<PlayerCard> queue = new LinkedList();
    private GameMap map;
    private boolean running = false;

    GameQueue(GameMap g) {
        map = g;
    }

    public void add(PlayerCard pCard) {
        queue.add(pCard);
        if (!running) {
            sendToGame();
        }
    }

    private void sendToGame() {
        if (!queue.isEmpty()) {
            running = true;
            if (SkyWarsReloaded.get().isEnabled()) {
                new BukkitRunnable() {
                    public void run() {
                        MatchManager.get().teleportToArena(map, (PlayerCard) queue.poll());
                        GameQueue.this.sendToGame();
                    }
                }.runTaskLater(SkyWarsReloaded.get(), 2L);
            }
        } else {
            running = false;
        }
    }
}
