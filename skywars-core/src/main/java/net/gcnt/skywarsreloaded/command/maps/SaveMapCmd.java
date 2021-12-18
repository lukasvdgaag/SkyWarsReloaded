package net.gcnt.skywarsreloaded.command.maps;

import net.gcnt.skywarsreloaded.SkyWarsReloaded;
import net.gcnt.skywarsreloaded.command.Cmd;
import net.gcnt.skywarsreloaded.game.GameTemplate;
import net.gcnt.skywarsreloaded.game.GameWorld;
import net.gcnt.skywarsreloaded.wrapper.sender.SWCommandSender;

import java.util.ArrayList;
import java.util.List;

public class SaveMapCmd extends Cmd {

    public SaveMapCmd(SkyWarsReloaded plugin) {
        super(plugin, "skywarsmap", "save", "skywars.command.map.creator", true, "<map>", "Save a map template.", "s");
    }

    @Override
    public boolean run(SWCommandSender sender, String[] args) {
        if (args.length == 0) {
            sender.sendMessage(plugin.getUtils().colorize("&cPlease enter a game template name."));
            return true;
        }

        final String templateName = args[0];
        GameTemplate template = plugin.getGameManager().getGameTemplateByName(templateName);
        if (template == null) {
            sender.sendMessage(plugin.getUtils().colorize("&cThere is no game template with that name."));
            return true;
        }

        List<GameWorld> worlds = plugin.getGameManager().getGameWorlds(template);
        for (GameWorld world : worlds) {
            if (world.isEditing()) {
                // schematic creating and saving
                boolean successful = plugin.getSchematicManager().saveGameWorldToSchematic(world, plugin.getUtils().getWorldEditWorld(world.getWorldName()));
                if (successful) {
                    sender.sendMessage(plugin.getUtils().colorize("&dWorld template schematic saved! &aWe have successfully saved the template world you were editing to a WorldEdit schematic."));
                } else {
                    sender.sendMessage(plugin.getUtils().colorize("&c&lError while saving world template schematic! &r&cWe failed to save the template world you were editing to a WorldEdit schematic. Please check the console for more information."));
                }
                break;
            }
        }

        // todo check for incorrect / uncompleted setup steps here before saving game template.

        template.saveData();
        sender.sendMessage(plugin.getUtils().colorize("&aThe data of template &e%s &ahas successfully been saved!".formatted(template.getName())));
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
}
