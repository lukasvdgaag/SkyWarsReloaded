package net.gcnt.skywarsreloaded.gui;

import net.gcnt.skywarsreloaded.SkyWarsReloaded;
import net.gcnt.skywarsreloaded.utils.Item;
import net.gcnt.skywarsreloaded.utils.gui.AbstractSWGui;
import net.gcnt.skywarsreloaded.utils.gui.SWConfirmationGui;
import net.gcnt.skywarsreloaded.utils.gui.SWGuiClickHandler;
import net.gcnt.skywarsreloaded.utils.properties.ItemProperties;
import net.gcnt.skywarsreloaded.wrapper.entity.SWPlayer;

public class ConfirmationGui extends AbstractSWGui implements SWConfirmationGui {

    public ConfirmationGui(SkyWarsReloaded plugin, String title, SWPlayer player) {
        super(plugin, title, 5, player);
    }

    public void addConfirmButton(String configMessageProperty, SWGuiClickHandler handler) {
        Item item = plugin.getConfig().getItem(ItemProperties.GENERAL_CONFIRM.toString())
                .withMessages(plugin.getMessages().getItem(configMessageProperty));

        addButton(1, item, handler);
    }

    public void addCancelButton(String configMessageProperty, SWGuiClickHandler handler) {
        Item item = plugin.getConfig().getItem(ItemProperties.GENERAL_DENY.toString())
                .withMessages(plugin.getMessages().getItem(configMessageProperty));

        addButton(3, item, handler);
    }

}
