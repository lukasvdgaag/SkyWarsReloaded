package com.walrusone.skywarsreloaded.utilities;

import org.bukkit.entity.Player;

public class Tagged {
    private Player player;
    private Long time;

    public Tagged(Player player, Long time) {
        this.player = player;
        this.time = time;
    }

    public Player getPlayer() {
        return player;
    }

    public Long getTime() {
        return time;
    }
}
