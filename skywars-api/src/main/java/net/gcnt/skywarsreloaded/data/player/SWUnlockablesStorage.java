package net.gcnt.skywarsreloaded.data.player;

import net.gcnt.skywarsreloaded.unlockable.Unlockable;
import net.gcnt.skywarsreloaded.wrapper.entity.SWPlayer;

public interface SWUnlockablesStorage {

    void addUnlockable(SWPlayer player, Unlockable unlockable);

    void removeUnlockable(SWPlayer player, Unlockable unlockable);

    boolean hasUnlockable(SWPlayer player, Unlockable unlockable);

}
