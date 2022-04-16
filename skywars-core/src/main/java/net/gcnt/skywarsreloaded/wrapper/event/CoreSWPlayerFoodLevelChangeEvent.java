package net.gcnt.skywarsreloaded.wrapper.event;

import net.gcnt.skywarsreloaded.utils.Item;
import net.gcnt.skywarsreloaded.wrapper.player.SWPlayer;

public class CoreSWPlayerFoodLevelChangeEvent extends CoreSWPlayerEvent implements SWPlayerFoodLevelChangeEvent {

    private boolean cancelled;
    private int foodLevel;
    private Item item;

    public CoreSWPlayerFoodLevelChangeEvent(SWPlayer player, int foodLevel, Item item) {
        super(player);
        this.foodLevel = foodLevel;
        this.item = item;
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }

    @Override
    public int getFoodLevel() {
        return foodLevel;
    }

    @Override
    public void setFoodLevel(int foodLevel) {
        this.foodLevel = foodLevel;
    }

    @Override
    public Item getItem() {
        return item;
    }
}
