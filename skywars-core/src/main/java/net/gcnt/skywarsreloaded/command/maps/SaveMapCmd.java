package net.gcnt.skywarsreloaded.command.maps;

import net.gcnt.skywarsreloaded.SkyWarsReloaded;
import net.gcnt.skywarsreloaded.command.Cmd;
import net.gcnt.skywarsreloaded.game.GameTemplate;
import net.gcnt.skywarsreloaded.game.gameinstance.LocalGameInstance;
import net.gcnt.skywarsreloaded.manager.gameinstance.LocalGameInstanceManager;
import net.gcnt.skywarsreloaded.utils.SWCoord;
import net.gcnt.skywarsreloaded.utils.properties.MessageProperties;
import net.gcnt.skywarsreloaded.utils.properties.RuntimeDataProperties;
import net.gcnt.skywarsreloaded.wrapper.entity.SWPlayer;
import net.gcnt.skywarsreloaded.wrapper.sender.SWCommandSender;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class SaveMapCmd extends Cmd {

    public SaveMapCmd(SkyWarsReloaded plugin) {
        super(plugin, "skywarsmap", "save", "skywars.command.map.creator", true, "[map]", "Save a map template.", "s");
    }

    @Override
    public boolean run(SWCommandSender sender, String[] args) {
        SWPlayer player = (SWPlayer) sender;
        if (plugin.getGameInstanceManager().isManagerRemote()) {
            plugin.getMessages().getMessage(MessageProperties.ERROR_ACTION_NOT_FOR_LOBBY_SERVERS.toString()).send(sender);
            return true;
        }

        GameTemplate template;
        LocalGameInstance foundInstance;
        CompletableFuture<Boolean> savingFuture = null;

        LocalGameInstanceManager gameManager = (LocalGameInstanceManager) plugin.getGameInstanceManager();
        if (args.length == 0) {
            LocalGameInstance gameInstance = gameManager.getGameInstanceByName(player.getLocation().getWorld().getName());
            if (gameInstance == null || !gameInstance.isEditing() || gameInstance.getTemplate() == null) {
                plugin.getMessages().getMessage(MessageProperties.ERROR_NO_TEMPLATE_WORLD_FOUND.toString()).send(sender);
                return true;
            }
            template = gameInstance.getTemplate();
            foundInstance = gameInstance;
        } else {
            final String templateName = args[0];
            sender.sendMessage(templateName); // todo: remove debug
            template = gameManager.getGameTemplateByName(templateName);
            foundInstance = null;
            if (template == null) {
                plugin.getMessages().getMessage(MessageProperties.MAPS_DOESNT_EXIST.toString()).send(sender);
                return true;
            }
        }

        if (foundInstance == null) {
            List<LocalGameInstance> runningInstances = gameManager.getGameInstancesByTemplate(template);
            for (LocalGameInstance instance : runningInstances) {
                if (instance.isEditing()) {
                    // instance creating and saving
                    foundInstance = instance;
                    plugin.getMessages().getMessage(MessageProperties.MAPS_SAVING_START.toString()).send(sender);
                    savingFuture = gameManager.saveInstanceToTemplate(instance);
                    break;
                }
            }
        }

        if (savingFuture == null) {
            this.sendMapSaveFail(template, sender);
            return true;
        }

        LocalGameInstance finalGameWorld = foundInstance;
        savingFuture.thenAccept(successful -> {
            try {
                if (successful) this.sendWorldSaved(template, sender);
                else this.sendMapSaveFail(template, sender);

                template.saveData();

                final SWCoord coord = plugin.getDataConfig().getCoord(RuntimeDataProperties.LOBBY_SPAWN.toString());
                for (SWPlayer swp : finalGameWorld.getWorld().getAllPlayers()) {
                    swp.teleport(coord);
                }
                gameManager.deleteGameInstance(finalGameWorld);

                this.sendMapSaved(template, sender);
                template.checkToDoList(sender);
            } catch (Exception e) {
                e.printStackTrace();
                this.sendMapSaveFail(template, sender);
            }
        });

        return true;
    }

    @Override
    public List<String> onTabCompletion(SWCommandSender sender, String[] args) {
        if (args.length == 1) {
            List<String> maps = new ArrayList<>();
            plugin.getGameInstanceManager().getGameTemplatesCopy().forEach(template -> maps.add(template.getName()));
            return maps;
        }
        return new ArrayList<>();
    }

    protected void sendWorldSaved(GameTemplate template, SWCommandSender sender) {
        plugin.getMessages().getMessage(MessageProperties.MAPS_WORLD_SAVED.toString())
                .replace("%template%", template.getName()).send(sender);
    }

    protected void sendMapSaved(GameTemplate template, SWCommandSender sender) {
        plugin.getMessages().getMessage(MessageProperties.MAPS_SAVED.toString())
                .replace("%template%", template.getName()).send(sender);
    }

    protected void sendMapSaveFail(GameTemplate template, SWCommandSender sender) {
        plugin.getMessages().getMessage(MessageProperties.MAPS_WORLD_SAVED_FAIL.toString())
                .replace("%template%", template.getName()).send(sender);
    }
}
