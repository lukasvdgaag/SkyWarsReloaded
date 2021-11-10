package net.gcnt.skywarsreloaded.game.kits;

import net.gcnt.skywarsreloaded.game.GamePlayer;

import java.util.HashMap;
import java.util.List;

public interface SWKit {

    String getId();

    void loadData();

    void saveData();

    String getDisplayName();

    String getIcon();

    String getUnavailableIcon();

    int getSlot();

    String getDescription();

    List<String> getLore();

    String getPermission();

    Object getHelmet();

    Object getChestplate();

    Object getLeggings();

    Object getBoots();

    HashMap<Integer, Object> getContents();

    List<String> getEffects();

    KitRequirements getRequirements();

    void givePlayer(GamePlayer sp);

}
