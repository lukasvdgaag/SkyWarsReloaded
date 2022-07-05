package net.gcnt.skywarsreloaded.unlockable.killmessages;

import net.gcnt.skywarsreloaded.enums.DeathReason;
import net.gcnt.skywarsreloaded.unlockable.CoreUnlockable;
import net.gcnt.skywarsreloaded.wrapper.player.SWPlayer;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class CoreKillMessageGroup extends CoreUnlockable implements KillMessageGroup {

    private final HashMap<DeathReason, List<String>> messages;

    public CoreKillMessageGroup(String id) {
        super(id);
        this.messages = new HashMap<>();
    }

    @Override
    public HashMap<DeathReason, List<String>> getMessages() {
        return messages;
    }

    @Override
    public void addMessage(DeathReason reason, String message) {
        if (!messages.containsKey(reason)) {
            messages.put(reason, new ArrayList<>());
        }
        messages.get(reason).add(message);
    }

    @Override
    public void setMessages(DeathReason reason, List<String> messages) {
        this.messages.put(reason, messages);
    }

    @Override
    public List<String> getMessages(DeathReason reason) {
        return messages.getOrDefault(reason, new ArrayList<>());
    }

    @Override
    public String getRandomMessage(DeathReason reason, @Nullable SWPlayer killer) {
        if (messages.containsKey(reason)) {
            return messages.get(reason).get(ThreadLocalRandom.current().nextInt(messages.get(reason).size()));
        }

        // falling back to the default message
        if (!messages.containsKey(DeathReason.DEFAULT)) {
            if (killer == null) return "§6%player% §edied.";
            else return "§6%player% §egot killed by §6%killer%§e.";
        }
        return getRandomMessage(DeathReason.DEFAULT, killer);
    }

    @Override
    public String getPermissionPrefix() {
        return "sw.killmessage.";
    }
}
