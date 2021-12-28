package net.gcnt.skywarsreloaded.command.maps;

import net.gcnt.skywarsreloaded.SkyWarsReloaded;
import net.gcnt.skywarsreloaded.command.Cmd;
import net.gcnt.skywarsreloaded.game.GameTemplate;
import net.gcnt.skywarsreloaded.game.GameWorld;
import net.gcnt.skywarsreloaded.utils.properties.MessageProperties;
import net.gcnt.skywarsreloaded.wrapper.player.SWPlayer;
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

        GameTemplate template;
        if (args.length == 0) {
            GameWorld world = plugin.getGameManager().getGameWorldByName(player.getLocation().world().getName());
            if (world == null || !world.isEditing() || world.getTemplate() == null) {
                plugin.getMessages().getMessage(MessageProperties.ERROR_NO_TEMPLATE_WORLD_FOUND.toString()).send(sender);
                return true;
            }
            template = world.getTemplate();
        } else {
            final String templateName = args[0];
            sender.sendMessage(templateName);
            template = plugin.getGameManager().getGameTemplateByName(templateName);
            if (template == null) {
                plugin.getMessages().getMessage(MessageProperties.MAPS_DOESNT_EXIST.toString()).send(sender);
                return true;
            }
        }

        List<GameWorld> worlds = plugin.getGameManager().getGameWorlds(template);
        CompletableFuture<Boolean> savingFuture = null;
        for (GameWorld world : worlds) {
            if (world.isEditing()) {
                // world creating and saving
                savingFuture = plugin.getWorldLoader().save(world);
                break;
            }
        }

        if (savingFuture == null) {
            this.sendMapSaveFail(template, sender);
            return true;
        }

        savingFuture.thenAccept(successful -> {
            if (successful) this.sendWorldSaved(template, sender);
            else this.sendMapSaveFail(template, sender);

            template.saveData();
            this.sendMapSaved(template, sender);

            template.checkToDoList(sender);
        });

        return true;
    }

    @Override
    public List<String> onTabCompletion(SWCommandSender sender, String[] args) {
        if (args.length == 1) {
            List<String> maps = new ArrayList<>();
            plugin.getGameManager().getGameTemplates().forEach(template -> maps.add(template.getName()));
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
