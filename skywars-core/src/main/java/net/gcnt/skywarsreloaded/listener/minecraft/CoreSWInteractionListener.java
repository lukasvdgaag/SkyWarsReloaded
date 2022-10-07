package net.gcnt.skywarsreloaded.listener.minecraft;

import net.gcnt.skywarsreloaded.SkyWarsReloaded;
import net.gcnt.skywarsreloaded.game.gameinstance.GameInstance;
import net.gcnt.skywarsreloaded.game.gameinstance.LocalGameInstance;
import net.gcnt.skywarsreloaded.listener.CoreSWEventListener;
import net.gcnt.skywarsreloaded.utils.Item;
import net.gcnt.skywarsreloaded.utils.gui.SWGui;
import net.gcnt.skywarsreloaded.utils.properties.ItemProperties;
import net.gcnt.skywarsreloaded.wrapper.entity.SWPlayer;
import net.gcnt.skywarsreloaded.wrapper.event.CoreSWPlayerInteractEvent;
import net.gcnt.skywarsreloaded.wrapper.event.SWPlayerInteractEvent;

public class CoreSWInteractionListener {

    private final SkyWarsReloaded plugin;

    public CoreSWInteractionListener(SkyWarsReloaded plugin) {
        this.plugin = plugin;
        plugin.getEventManager().registerListener(new CoreSWEventListener<>(CoreSWPlayerInteractEvent.class, this::onInteract));
    }

    public void onInteract(CoreSWPlayerInteractEvent event) {
        SWPlayer player = event.getPlayer();
        if (event.getClickedBlockType().toLowerCase().contains("chest")) {
            if (event.getAction() == SWPlayerInteractEvent.Action.LEFT_CLICK_BLOCK) {
                player.sendMessage("opening chest.");
                plugin.getNMSManager().getNMS().setChestOpen(event.getClickedBlockLocation(), true);
            }
        }

        GameInstance game = player.getGameWorld();
        if (game instanceof LocalGameInstance) {
            LocalGameInstance localGame = (LocalGameInstance) game;

            // player is player a local game.
            if (game.getState().isWaiting()) {
                Item item = player.getItemInHand(false);
                if (item == null) return;

                String kitSelectorMaterial = plugin.getItemManager().getItemFromConfig(ItemProperties.GAME_KIT_SELECTOR.toString()).getMaterial();
                String leaveMaterial = plugin.getItemManager().getItemFromConfig(ItemProperties.GAME_GAME_LEAVE.toString()).getMaterial();
                if (item.getMaterial().equals(kitSelectorMaterial)) {
                    SWGui kitGui = plugin.getGuiManager().createKitGui(player);
                    kitGui.open();
                } else if (item.getMaterial().equals(leaveMaterial)) {
                    localGame.removePlayer(localGame.getPlayer(player));
                }
            }
        }

    }

}
