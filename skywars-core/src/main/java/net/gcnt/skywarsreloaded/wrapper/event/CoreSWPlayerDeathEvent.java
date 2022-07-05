package net.gcnt.skywarsreloaded.wrapper.event;

import net.gcnt.skywarsreloaded.utils.Item;
import net.gcnt.skywarsreloaded.wrapper.entity.SWPlayer;

import java.util.List;

public class CoreSWPlayerDeathEvent extends CoreSWPlayerEvent implements SWPlayerDeathEvent {

    private String deathMessage;
    private boolean keepInventory;
    private final List<Item> drops;

    public CoreSWPlayerDeathEvent(SWPlayer player, String deathMessage, boolean keepInventory, List<Item> drops) {
        super(player);
        this.deathMessage = deathMessage;
        this.keepInventory = keepInventory;
        this.drops = drops;
    }

    @Override
    public String getDeathMessage() {
        return deathMessage;
    }

    @Override
    public void setDeathMessage(String message) {
        this.deathMessage = message;
    }

    @Override
    public boolean isKeepInventory() {
        return keepInventory;
    }

    @Override
    public void setKeepInventory(boolean keepInventory) {
        this.keepInventory = keepInventory;
    }

    @Override
    public List<Item> getDrops() {
        return drops;
    }
}
