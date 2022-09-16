package net.gcnt.skywarsreloaded.gui.voting;

import net.gcnt.skywarsreloaded.SkyWarsReloaded;
import net.gcnt.skywarsreloaded.utils.gui.AbstractSWGui;
import net.gcnt.skywarsreloaded.wrapper.entity.SWPlayer;

public class CoreVotingGui extends AbstractSWGui {

    public CoreVotingGui(SkyWarsReloaded plugin, SWPlayer player) {
        // todo: title
        super(plugin, "", 3, player);
    }
}