package net.gcnt.skywarsreloaded.command.maps;

import net.gcnt.skywarsreloaded.SkyWarsReloaded;
import net.gcnt.skywarsreloaded.command.Cmd;
import net.gcnt.skywarsreloaded.game.GameTemplate;
import net.gcnt.skywarsreloaded.utils.properties.MessageProperties;
import net.gcnt.skywarsreloaded.wrapper.sender.SWCommandSender;

import java.util.ArrayList;
import java.util.List;

public class EnableCmd extends Cmd {

    public EnableCmd(SkyWarsReloaded plugin) {
        super(plugin, "skywarsmap", "enable", "skywars.command.map.enable", true, "<map>", "Enable a template.", "en");
    }

    @Override
    public boolean run(SWCommandSender sender, String[] args) {
        if (args.length == 0) {
            plugin.getMessages().getMessage(MessageProperties.MAPS_ENTER_NAME.toString()).send(sender);
            return true;
        }

        final String templateName = args[0];
        GameTemplate template = plugin.getGameManager().getGameTemplateByName(templateName);
        if (template == null) {
            plugin.getMessages().getMessage(MessageProperties.MAPS_DOESNT_EXIST.toString()).send(sender);
            return true;
        }
        if (template.isEnabled()) {
            plugin.getMessages().getMessage(MessageProperties.MAPS_ALREADY_ENABLED.toString()).replace("%template%", template.getName()).send(sender);
            return true;
        }

        if (!template.checkToDoList(sender)) {
            plugin.getMessages().getMessage(MessageProperties.MAPS_CANNOT_ENABLE_INCOMPLETE_SETUP.toString()).replace("%template%", template.getName()).send(sender);
            return true;
        }

        template.enable();
        template.saveData();
        plugin.getMessages().getMessage(MessageProperties.MAPS_ENABLED.toString()).replace("%template%", template.getName()).send(sender);
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
