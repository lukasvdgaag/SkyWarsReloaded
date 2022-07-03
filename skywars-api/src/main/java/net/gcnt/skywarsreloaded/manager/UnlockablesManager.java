package net.gcnt.skywarsreloaded.manager;

import net.gcnt.skywarsreloaded.unlockable.killmessages.KillMessageGroup;

import java.util.List;

public interface UnlockablesManager {

    void load();

    void loadKillMessages();

    List<KillMessageGroup> getKillMessageGroups();

}
