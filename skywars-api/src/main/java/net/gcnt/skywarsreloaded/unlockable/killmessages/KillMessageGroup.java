package net.gcnt.skywarsreloaded.unlockable.killmessages;

import net.gcnt.skywarsreloaded.enums.DeathReason;
import net.gcnt.skywarsreloaded.unlockable.Unlockable;
import net.gcnt.skywarsreloaded.wrapper.player.SWPlayer;

import java.util.HashMap;
import java.util.List;

public interface KillMessageGroup extends Unlockable {

    HashMap<DeathReason, List<String>> getMessages();

    void addMessage(DeathReason reason, String message);

    List<String> getMessages(DeathReason reason);

    String getRandomMessage(SWPlayer player, DeathReason reason);

}
