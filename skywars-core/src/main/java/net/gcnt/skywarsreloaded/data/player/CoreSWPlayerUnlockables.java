package net.gcnt.skywarsreloaded.data.player;

import net.gcnt.skywarsreloaded.SkyWarsReloaded;
import net.gcnt.skywarsreloaded.data.player.stats.SWPlayerUnlockables;
import net.gcnt.skywarsreloaded.unlockable.Unlockable;
import net.gcnt.skywarsreloaded.wrapper.entity.SWPlayer;

import java.util.ArrayList;
import java.util.List;

public class CoreSWPlayerUnlockables implements SWPlayerUnlockables {

    private final List<Unlockable> unlockables;
    private final SkyWarsReloaded plugin;
    private final SWPlayer player;

    public CoreSWPlayerUnlockables(SkyWarsReloaded plugin, SWPlayer player) {
        this.plugin = plugin;
        this.unlockables = new ArrayList<>();
        this.player = player;
    }

    @Override
    public void initUnlockable(String type, String id) {
        Unlockable unlockable = null;
        switch (type) {
            case "KIT":
                unlockable = plugin.getKitManager().getKitByName(id);
                break;
            case "CAGE":
                unlockable = plugin.getCageManager().getCageById(id);
                break;
        }

        if (unlockable != null) {
            unlockables.add(unlockable);
        }
    }

    @Override
    public void addUnlockable(Unlockable unlockable) {
        plugin.getPlayerStorage().getUnlockablesStorage().addUnlockable(player, unlockable);
        this.unlockables.add(unlockable);
    }

    @Override
    public void removeUnlockable(Unlockable unlockable) {
        plugin.getPlayerStorage().getUnlockablesStorage().removeUnlockable(player, unlockable);
        unlockables.remove(unlockable);
    }

    @Override
    public List<Unlockable> getUnlocked(SWPlayer player) {
        return unlockables;
    }

    @Override
    public boolean isUnlocked(Unlockable unlockable) {
        return unlockables.contains(unlockable);
    }

}
