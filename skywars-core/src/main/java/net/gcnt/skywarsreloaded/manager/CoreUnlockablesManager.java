package net.gcnt.skywarsreloaded.manager;

import net.gcnt.skywarsreloaded.SkyWarsReloaded;
import net.gcnt.skywarsreloaded.data.config.YAMLConfig;
import net.gcnt.skywarsreloaded.enums.DeathReason;
import net.gcnt.skywarsreloaded.unlockable.killmessages.CoreKillMessageGroup;
import net.gcnt.skywarsreloaded.unlockable.killmessages.KillMessageGroup;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CoreUnlockablesManager implements UnlockablesManager {

    private final SkyWarsReloaded plugin;

    private final HashMap<String, KillMessageGroup> killMessageGroups;

    public CoreUnlockablesManager(SkyWarsReloaded plugin) {
        this.plugin = plugin;
        this.killMessageGroups = new HashMap<>();

        load();
    }

    @Override
    public void load() {
        loadKillMessages();
    }

    @Override
    public void loadDefaultKillMessage() {
        if (getKillMessageGroup("default") != null) return;

        CoreKillMessageGroup group = new CoreKillMessageGroup("default");
        group.addMessage(DeathReason.DEFAULT, "§e%player%§7 died.");
        group.addMessage(DeathReason.DEFAULT_KILL, "§e%player%§7 was killed by §e%killer%§7.");
        killMessageGroups.put("default", group);
    }

    @Override
    public void loadKillMessages() {
        killMessageGroups.clear();

        final YAMLConfig config = plugin.getYAMLManager().loadConfig("killmessages", plugin.getDataFolder(), "killmessages.yml");
        config.getKeys("").forEach(key -> {
            // todo make sure that a default group is always loaded
            KillMessageGroup group = new CoreKillMessageGroup(key);
            config.loadUnlockableData(group, key);

            for (DeathReason reason : DeathReason.values()) {
                final String property = key + "." + reason.getProperty();
                if (config.contains(property)) {
                    final Object o = config.get(property);
                    if (o instanceof List) {
                        group.setMessages(reason, (List<String>) o);
                    } else {
                        group.addMessage(reason, o.toString());
                    }
                }
            }
            killMessageGroups.put(key, group);
        });

        loadDefaultKillMessage();
    }

    @Override
    public List<KillMessageGroup> getKillMessageGroups() {
        return new ArrayList<>(killMessageGroups.values());
    }

    @Override
    public KillMessageGroup getKillMessageGroup(String identifier) {
        if (killMessageGroups.containsKey(identifier)) {
            return killMessageGroups.get(identifier);
        }
        return killMessageGroups.getOrDefault("default", null);
    }
}
