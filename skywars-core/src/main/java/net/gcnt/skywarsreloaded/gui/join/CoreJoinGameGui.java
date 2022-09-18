package net.gcnt.skywarsreloaded.gui.join;

import net.gcnt.skywarsreloaded.SkyWarsReloaded;
import net.gcnt.skywarsreloaded.game.GameTemplate;
import net.gcnt.skywarsreloaded.game.gameinstance.GameInstance;
import net.gcnt.skywarsreloaded.utils.Item;
import net.gcnt.skywarsreloaded.utils.gui.AbstractSWGui;
import net.gcnt.skywarsreloaded.utils.gui.SWGui;
import net.gcnt.skywarsreloaded.utils.gui.SWGuiClickHandler;
import net.gcnt.skywarsreloaded.wrapper.entity.SWPlayer;

import java.util.Comparator;

public class CoreJoinGameGui extends AbstractSWGui {

    public CoreJoinGameGui(SkyWarsReloaded plugin, SWPlayer player) {
        // todo: title
        super(plugin, "TEMP §a§lTEST TITLE §c§lFOR TESTING UWU", 6, player);

        // Back button
        final Item backItem = plugin.getItemManager().createItem("BARRIER");
        this.addButton(45, backItem, this::handleBackButton);

        createTemplateItems(plugin);

        // Refresh button
        final Item refreshItem = plugin.getItemManager().createItem("SUNFLOWER");
        this.addButton(48, refreshItem, this::handleBackButton);

        // Join random
        final Item randomItem = plugin.getItemManager().createItem("SUNFLOWER");
        this.addButton(50, randomItem, this::handleBackButton);
    }

    private void createTemplateItems(SkyWarsReloaded plugin) {
        // Get all templates with game instances and sort them by teamsize from smallest to largest.
        plugin.getGameManager().getGameWorlds().stream()
                .map(GameInstance::getTemplate).distinct()
                .sorted(getComparator())
                .forEach(this::addGameTemplateItem);
    }

    private Comparator<GameTemplate> getComparator() {
        return Comparator.comparingInt(GameTemplate::getTeamSize);
    }

    private void addGameTemplateItem(GameTemplate template) {

    }

    private SWGuiClickHandler.ClickResult handleBackButton(SWGui gui, int slot, SWGuiClickHandler.ClickType clickType, boolean isShift) {
        gui.getPlayer().sendMessage("OOoooo! You clicked a button!");
        return SWGuiClickHandler.ClickResult.IGNORED;
    }
}
