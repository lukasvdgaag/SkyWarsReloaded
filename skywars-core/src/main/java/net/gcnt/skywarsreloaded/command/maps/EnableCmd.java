package net.gcnt.skywarsreloaded.command.maps;

import net.gcnt.skywarsreloaded.SkyWarsReloaded;
import net.gcnt.skywarsreloaded.command.Cmd;
import net.gcnt.skywarsreloaded.game.GameTemplate;
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
            sender.sendMessage(plugin.getUtils().colorize("&cPlease enter a game template name."));
            return true;
        }

        final String templateName = args[0];
        GameTemplate template = plugin.getGameManager().getGameTemplateByName(templateName);
        if (template == null) {
            sender.sendMessage(plugin.getUtils().colorize("&cThere is no game template with that name."));
            return true;
        }
        if (template.isEnabled()) {
            sender.sendMessage(plugin.getUtils().colorize("&cThis template is already enabled."));
            return true;
        }

        if (!template.checkToDoList(sender)) {
            sender.sendMessage(plugin.getUtils().colorize("&cCould not enable template &e%s&c because it was set up incorrectly. See the message above for more information.".formatted(template.getName())));
            return true;
        }

        template.enable();
        sender.sendMessage(plugin.getUtils().colorize("&aThe template &e%s &ahas successfully been &denabled&a!".formatted(template.getName())));
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
