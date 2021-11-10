package net.gcnt.skywarsreloaded.bukkit.game.kits;

import net.gcnt.skywarsreloaded.SkyWarsReloaded;
import net.gcnt.skywarsreloaded.game.GamePlayer;
import net.gcnt.skywarsreloaded.game.kits.AbstractSWKit;

public class BukkitSWKit extends AbstractSWKit {

    public BukkitSWKit(SkyWarsReloaded plugin, String id) {
        super(plugin, id);
    }

    @Override
    public void givePlayer(GamePlayer sp) {
        // todo give kit to bukkit player.
    }
}
