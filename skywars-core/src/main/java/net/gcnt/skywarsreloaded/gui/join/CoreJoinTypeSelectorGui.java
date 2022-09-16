package net.gcnt.skywarsreloaded.gui.join;

import net.gcnt.skywarsreloaded.SkyWarsReloaded;
import net.gcnt.skywarsreloaded.utils.gui.AbstractSWGui;
import net.gcnt.skywarsreloaded.wrapper.entity.SWPlayer;

public class CoreJoinTypeSelectorGui extends AbstractSWGui {

    public CoreJoinTypeSelectorGui(SkyWarsReloaded plugin, SWPlayer player) {
        // todo: title
        super(plugin, "", 3, player);
    }
}