package net.gcnt.skywarsreloaded.listener.minecraft;

import net.gcnt.skywarsreloaded.SkyWarsReloaded;
import net.gcnt.skywarsreloaded.game.GameTemplate;
import net.gcnt.skywarsreloaded.game.gameinstance.GameInstance;
import net.gcnt.skywarsreloaded.listener.CoreSWEventListener;
import net.gcnt.skywarsreloaded.utils.results.SpawnRemoveResult;
import net.gcnt.skywarsreloaded.wrapper.event.CoreSWBlockBreakEvent;

public class CoreSWBlockBreakListener {

    private final SkyWarsReloaded plugin;

    public CoreSWBlockBreakListener(SkyWarsReloaded plugin) {
        this.plugin = plugin;
        plugin.getEventManager().registerListener(new CoreSWEventListener<>(CoreSWBlockBreakEvent.class, this::onBlockBreak));
    }

    public void onBlockBreak(CoreSWBlockBreakEvent event) {
        if (CoreSWEventListener.cancelWhenWaitingInGame(event)) return;

        GameInstance gameWorld = plugin.getGameInstanceManager().getGameInstanceByName(event.getCoord().getWorld().getName());
        if (gameWorld == null || !gameWorld.isEditing()) return;
        final GameTemplate template = gameWorld.getTemplate();

        if (event.getBlockTypeName().equalsIgnoreCase("CHEST") || event.getBlockTypeName().equalsIgnoreCase("TRAPPED_CHEST")) {
            // player is removing a chest.
            boolean res = template.removeChest(event.getCoord());
            if (res) {
                event.getPlayer().sendTitle(plugin.getUtils().colorize("&c&lCHEST REMOVED"), plugin.getUtils().colorize("&7Removed a chest from the template!"), 5, 30, 5);
                event.getPlayer().sendMessage(plugin.getUtils().colorize(String.format("&cRemoved a chest from game template &7%s&c. There's &7%d &cleft.", template.getDisplayName(), template.getChests().size())));
                template.checkToDoList(event.getPlayer());
            }
        } else if (event.getBlockTypeName().equalsIgnoreCase("BEACON")) {
            // player is removing a player spawnpoint.
            SpawnRemoveResult res = template.removeSpawn(event.getCoord());

            if (res.isSuccess()) {
                int currentSpawns = res.getRemainingSpawns();

                event.getPlayer().sendTitle(plugin.getUtils().colorize("&c&lSPAWN REMOVED"), plugin.getUtils().colorize("&7Removed a player spawn from the template!"), 5, 30, 5);
                String message;
                if (template.getTeamSize() == 1) {
                    message = String.format("&cRemoved player spawnpoint &7#%d &cfrom game template &b%s&a.", res.getTeam() + 1, template.getDisplayName());
                } else {
                    message = String.format("&cRemoved player spawnpoint &7#%d &cfrom team &7%d &cfor game template &7%s&c. &e%d &cspawns left to set for this team.", res.getIndex() + 1, res.getTeam() + 1, template.getDisplayName(), template.getTeamSize() - currentSpawns);
                }
                event.getPlayer().sendMessage(plugin.getUtils().colorize(message));
                template.checkToDoList(event.getPlayer());
            }
        }
    }

}
