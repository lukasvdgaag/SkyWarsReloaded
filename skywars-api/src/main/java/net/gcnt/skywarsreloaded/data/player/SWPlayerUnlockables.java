package net.gcnt.skywarsreloaded.data.player;

import net.gcnt.skywarsreloaded.unlockable.Unlockable;
import net.gcnt.skywarsreloaded.wrapper.entity.SWPlayer;

import java.util.List;

public interface SWPlayerUnlockables {

    void addUnlockable(Unlockable unlockable);

    void removeUnlockable(Unlockable unlockable);

    List<Unlockable> getUnlocked(SWPlayer player);

    boolean isUnlocked(Unlockable unlockable);

}
