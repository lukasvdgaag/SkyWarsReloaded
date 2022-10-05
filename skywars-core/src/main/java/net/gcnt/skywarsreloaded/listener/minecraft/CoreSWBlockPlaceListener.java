package net.gcnt.skywarsreloaded.listener.minecraft;

import net.gcnt.skywarsreloaded.SkyWarsReloaded;
import net.gcnt.skywarsreloaded.game.GameTemplate;
import net.gcnt.skywarsreloaded.game.gameinstance.LocalGameInstance;
import net.gcnt.skywarsreloaded.game.types.ChestType;
import net.gcnt.skywarsreloaded.listener.CoreSWEventListener;
import net.gcnt.skywarsreloaded.wrapper.event.CoreSWBlockPlaceEvent;

public class CoreSWBlockPlaceListener {

    private final SkyWarsReloaded plugin;

    public CoreSWBlockPlaceListener(SkyWarsReloaded plugin) {
        this.plugin = plugin;
        plugin.getEventManager().registerListener(new CoreSWEventListener<>(CoreSWBlockPlaceEvent.class, this::onPlace));
    }

    public void onPlace(CoreSWBlockPlaceEvent event) {
        if (CoreSWEventListener.cancelWhenWaitingInGame(event) || plugin.getGameInstanceManager().isManagerRemote()) return;

        // player is placing a chest
        if (event.getBlockTypeName().equalsIgnoreCase("CHEST") ||
                event.getBlockTypeName().equalsIgnoreCase("TRAPPED_CHEST")) {
            LocalGameInstance gameWorld = (LocalGameInstance) plugin.getGameInstanceManager().getGameInstanceByName(event.getCoord().getWorld().getName());
            if (gameWorld == null || !gameWorld.isEditing()) return;

            final GameTemplate template = gameWorld.getTemplate();
            final ChestType chestType = gameWorld.getSelectedEditingChestTypes().getOrDefault(event.getPlayer().getUuid(), ChestType.ISLAND);
            boolean res = template.addChest(event.getCoord().asBlock(), chestType);
            if (res) {
                event.getPlayer().sendTitle(plugin.getUtils().colorize("&a&lCHEST ADDED"),
                        plugin.getUtils().colorize("&7Added a new chest to the template!"), 5, 30, 5);
                event.getPlayer().sendMessage(plugin.getUtils().colorize(
                        String.format("&aAdded a new &e%s &achest (&b#%d&a) to game template &b%s&a.",
                                chestType.getId().toLowerCase(),
                                template.getChests().size(), template.getDisplayName())));
                template.checkToDoList(event.getPlayer());
            }
        }
    }

}
