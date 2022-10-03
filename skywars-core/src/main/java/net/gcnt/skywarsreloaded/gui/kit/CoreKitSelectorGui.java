package net.gcnt.skywarsreloaded.gui.kit;

import net.gcnt.skywarsreloaded.SkyWarsReloaded;
import net.gcnt.skywarsreloaded.game.kits.SWKit;
import net.gcnt.skywarsreloaded.utils.Item;
import net.gcnt.skywarsreloaded.utils.gui.AbstractSWGui;
import net.gcnt.skywarsreloaded.utils.gui.SWConfirmationGui;
import net.gcnt.skywarsreloaded.utils.gui.SWGuiClickHandler;
import net.gcnt.skywarsreloaded.utils.properties.MessageProperties;
import net.gcnt.skywarsreloaded.wrapper.entity.SWPlayer;

import java.util.ArrayList;
import java.util.List;

public class CoreKitSelectorGui extends AbstractSWGui {

    public CoreKitSelectorGui(SkyWarsReloaded plugin, SWPlayer player) {
        super(plugin,
                plugin.getMessages().getString(MessageProperties.MENUS_KITS_TITLE.toString(), "Kit Selector"),
                54,
                player);

        this.loadItems();
    }

    public void loadItems() {
        addCloseButton(49);

        for (SWKit kit : plugin.getKitManager().getKits()) {
            if (kit.getSlot() == -1) continue;

            final boolean unlocked = kit.hasUnlocked(player);
            final boolean selected = kit.getId().equals(player.getPlayerData().getKit());

            Item item = unlocked ? kit.getIcon() : kit.getUnavailableIcon();
            Item messagesItem = selected ? plugin.getMessages().getItem(MessageProperties.ITEMS_KITS_SELECTED.toString()) :
                    unlocked ? plugin.getMessages().getItem(MessageProperties.ITEMS_KITS_UNLOCKED.toString()) :
                            plugin.getMessages().getItem(MessageProperties.ITEMS_KITS_LOCKED.toString());

            item.withMessages(messagesItem);

            item.setDisplayName(prepareKitLine(kit, item.getDisplayName(), unlocked, selected));

            List<String> newLore = new ArrayList<>();
            for (String line : item.getLore()) {
                if (line.equals("{lore}")) {
                    kit.getLore().forEach(string -> newLore.add(prepareKitLine(kit, string, unlocked, selected)));
                } else {
                    newLore.add(prepareKitLine(kit, line, unlocked, selected));
                }
            }
            item.setLore(newLore);

            addButton(kit.getSlot(), item, (gui, slot, clickType, isShift) -> handleKitClick(kit));
        }
    }

    public String prepareKitLine(SWKit kit, String s, boolean unlocked, boolean selected) {
        MessageProperties status = selected ? MessageProperties.ITEMS_KITS_STATUS_SELECTED :
                unlocked ? MessageProperties.ITEMS_KITS_STATUS_UNLOCKED : MessageProperties.ITEMS_KITS_STATUS_LOCKED;
        String statusMessage = plugin.getMessages().getString(status.toString(), "Click");

        return s.replace("{displayname}", kit.getDisplayName())
                .replace("{description}", kit.getDescription())
                .replace("{cost}", kit.getCost() + "")
                .replace("{status}", statusMessage);
    }

    public SWGuiClickHandler.ClickResult handleKitClick(SWKit kit) {
        if (kit.getId().equals(player.getPlayerData().getKit())) {
            // player is selecting the kit they currently have selected.
            return SWGuiClickHandler.ClickResult.CANCELLED;
        }

        if (kit.hasUnlocked(player)) {
            // player has unlocked the kit, so selecting it.
            player.getPlayerData().setKit(kit.getId());
            plugin.getMessages().getMessage(MessageProperties.KITS_SELECTED.toString())
                    .replace("{kit}", kit.getDisplayName())
                    .send(player);
            return SWGuiClickHandler.ClickResult.CANCELLED;
        } else {
            if (kit.isEligible(player)) {
                // todo add kit purchase logic
                showPurchaseConfirmationDialog(kit);
            } else {
                plugin.getMessages().getMessage(MessageProperties.KITS_CANNOT_AFFORD.toString())
                        .replace("{kit}", kit.getDisplayName())
                        .replace("{cost}", kit.getCost() + "")
                        .send(player);
            }

            // player does not have the kit unlocked, attempting to unlock it if it costs money and if they're eligible.
        }

        return SWGuiClickHandler.ClickResult.CANCELLED;
    }

    public void showPurchaseConfirmationDialog(SWKit kit) {
        final SWConfirmationGui confirmationGui = plugin.getGuiManager().createConfirmationGui(player, plugin.getMessages().getString(MessageProperties.MENUS_CONFIRM_PURCHASE_TITLE.toString()));
        confirmationGui.addConfirmButton(MessageProperties.ITEMS_KITS_CONFIRM_PURCHASE.toString(), (gui, slot, clickType, isShift) -> {
            if (!kit.isEligible(player)) {
                plugin.getMessages().getMessage(MessageProperties.KITS_CANNOT_AFFORD.toString())
                        .replace("{kit}", kit.getDisplayName())
                        .replace("{cost}", kit.getCost() + "")
                        .send(player);
                return SWGuiClickHandler.ClickResult.CANCELLED;
            } else {
                kit.unlock(player);
                player.getPlayerData().setKit(kit.getId());
                plugin.getMessages().getMessage(MessageProperties.KITS_PURCHASED.toString())
                        .replace("{kit}", kit.getDisplayName())
                        .replace("{cost}", kit.getCost() + "")
                        .send(player);
                confirmationGui.close();

                open();
            }

            return SWGuiClickHandler.ClickResult.CANCELLED;
        });

        confirmationGui.addCancelButton(MessageProperties.ITEMS_KITS_DENY_PURCHASE.toString(), (gui, slot, clickType, isShift) -> {
            confirmationGui.close();
            open();
            return SWGuiClickHandler.ClickResult.CANCELLED;
        });
    }


}
