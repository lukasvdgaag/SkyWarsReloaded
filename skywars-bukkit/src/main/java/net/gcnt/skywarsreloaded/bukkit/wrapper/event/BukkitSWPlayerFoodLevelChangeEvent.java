package net.gcnt.skywarsreloaded.bukkit.wrapper.event;

import net.gcnt.skywarsreloaded.utils.Item;
import net.gcnt.skywarsreloaded.wrapper.event.CoreSWPlayerFoodLevelChangeEvent;
import net.gcnt.skywarsreloaded.wrapper.player.SWPlayer;
import org.bukkit.event.entity.FoodLevelChangeEvent;

public class BukkitSWPlayerFoodLevelChangeEvent extends CoreSWPlayerFoodLevelChangeEvent {

    private final FoodLevelChangeEvent event;

    public BukkitSWPlayerFoodLevelChangeEvent(FoodLevelChangeEvent event, SWPlayer player, int foodLevel, Item item) {
        super(player, foodLevel, item);
        this.event = event;
    }

    public FoodLevelChangeEvent getEvent() {
        return event;
    }

    @Override
    public void setFoodLevel(int foodLevel) {
        event.setFoodLevel(foodLevel);
    }
}
