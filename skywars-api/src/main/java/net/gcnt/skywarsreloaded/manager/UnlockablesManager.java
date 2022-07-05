package net.gcnt.skywarsreloaded.manager;

import net.gcnt.skywarsreloaded.unlockable.killmessages.KillMessageGroup;

import java.util.List;

public interface UnlockablesManager {

    void load();

    void loadKillMessages();

    void loadDefaultKillMessage();

    List<KillMessageGroup> getKillMessageGroups();

    KillMessageGroup getKillMessageGroup(String identifier);

}
