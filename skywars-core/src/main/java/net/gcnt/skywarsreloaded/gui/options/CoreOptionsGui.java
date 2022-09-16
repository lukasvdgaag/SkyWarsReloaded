package net.gcnt.skywarsreloaded.gui.options;

import net.gcnt.skywarsreloaded.SkyWarsReloaded;
import net.gcnt.skywarsreloaded.utils.gui.AbstractSWGui;
import net.gcnt.skywarsreloaded.wrapper.entity.SWPlayer;

public class CoreOptionsGui extends AbstractSWGui {

    public CoreOptionsGui(SkyWarsReloaded plugin, SWPlayer player) {
        // todo: title
        super(plugin, "", 3, player);
    }
}