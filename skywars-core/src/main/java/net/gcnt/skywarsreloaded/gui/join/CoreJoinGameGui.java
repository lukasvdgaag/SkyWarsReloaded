package net.gcnt.skywarsreloaded.gui.join;

import net.gcnt.skywarsreloaded.SkyWarsReloaded;
import net.gcnt.skywarsreloaded.utils.Item;
import net.gcnt.skywarsreloaded.utils.gui.AbstractSWGui;
import net.gcnt.skywarsreloaded.utils.gui.SWGui;
import net.gcnt.skywarsreloaded.utils.gui.SWGuiClickHandler;
import net.gcnt.skywarsreloaded.wrapper.entity.SWPlayer;

public class CoreJoinGameGui extends AbstractSWGui {

    public CoreJoinGameGui(SkyWarsReloaded plugin, SWPlayer player) {
        // todo: title
        super(plugin, "TEMP §a§lTEST TITLE §c§lFOR TESTING UWU", 6, player);

        // Back button
        final Item backItem = plugin.getItemManager().createItem("BARRIER");
        this.addButton(54, backItem, this::handleBackButton);
        // Refresh button
        final Item refreshItem = plugin.getItemManager().createItem("SUNFLOWER");
        this.addButton(58, refreshItem, this::handleBackButton);
    }

    private SWGuiClickHandler.ClickResult handleBackButton(SWGui gui, int slot, SWGuiClickHandler.ClickType clickType, boolean isShift) {
        gui.getPlayer().sendMessage("OOoooo! You clicked a button!");
        return SWGuiClickHandler.ClickResult.IGNORED;
    }
}
