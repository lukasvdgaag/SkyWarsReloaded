package net.gcnt.skywarsreloaded.gui.join;

import com.google.common.collect.Lists;
import net.gcnt.skywarsreloaded.SkyWarsReloaded;
import net.gcnt.skywarsreloaded.game.GamePlayer;
import net.gcnt.skywarsreloaded.game.GameTemplate;
import net.gcnt.skywarsreloaded.game.gameinstance.GameInstance;
import net.gcnt.skywarsreloaded.game.gameinstance.LocalGameInstance;
import net.gcnt.skywarsreloaded.utils.Item;
import net.gcnt.skywarsreloaded.utils.gui.AbstractSWGui;
import net.gcnt.skywarsreloaded.utils.gui.SWGui;
import net.gcnt.skywarsreloaded.utils.gui.SWGuiClickHandler;
import net.gcnt.skywarsreloaded.wrapper.entity.SWPlayer;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class CoreJoinGameGui extends AbstractSWGui {

    private int nextSlot = 0;

    public CoreJoinGameGui(SkyWarsReloaded plugin, SWPlayer player) {
        super(plugin, "Game Selector", 6, player);

        addCloseButton(49);
        createTemplateItems(plugin);
    }

    private void createTemplateItems(SkyWarsReloaded plugin) {
        this.nextSlot = 0;
        // Get all templates with game instances and sort them by teamsize from smallest to largest.
        plugin.getGameInstanceManager().getGameInstancesList().stream()
                .map(GameInstance::getTemplate).distinct()
                .sorted(getComparator())
                .forEach(this::addGameTemplateItem);
    }

    private Comparator<GameTemplate> getComparator() {
        return Comparator.comparingInt(GameTemplate::getTeamSize);
    }

    private void addGameTemplateItem(GameTemplate template) {
        Item item = plugin.getItemManager().createItem("DIAMOND_SWORD");

        List<? extends GameInstance> gameInstances = plugin.getGameInstanceManager().getGameInstancesByTemplate(template)
                .stream()
                .filter(GameInstance::canJoinGame)
                .sorted(Comparator.comparingInt(GameInstance::getPlayerCount))
                .collect(Collectors.toList());

        String joinColor = gameInstances.isEmpty() ? "§c" : "§a";
        String joinStatus = gameInstances.isEmpty() ? "§7No joinable games found." : "§3Click to join.";

        item.setDisplayName(joinColor + template.getDisplayName());
        item.setLore(Lists.newArrayList("", joinStatus));
        item.addAllItemFlags();

        this.addButton(nextSlot, item, (gui, slot, type, shift) -> {
            List<? extends GameInstance> instances = plugin.getGameInstanceManager().getGameInstancesByTemplate(template)
                    .stream()
                    .sorted(Comparator.comparingInt(GameInstance::getPlayerCount))
                    .collect(Collectors.toList());

            for (GameInstance instance : instances) {
                if (instance.canJoinGame()) {
                    if (instance instanceof LocalGameInstance) {
                        LocalGameInstance localGame = (LocalGameInstance) instance;
                        GamePlayer gp = localGame.preparePlayerJoin(player.getUuid(), false);

                        localGame.addPlayers(gp);
                    }
                }
            }

            return SWGuiClickHandler.ClickResult.CANCELLED;
        });
        nextSlot++;
    }

    private SWGuiClickHandler.ClickResult handleBackButton(SWGui gui, int slot, SWGuiClickHandler.ClickType clickType, boolean isShift) {
        gui.getPlayer().sendMessage("OOoooo! You clicked a button!");
        return SWGuiClickHandler.ClickResult.IGNORED;
    }
}
