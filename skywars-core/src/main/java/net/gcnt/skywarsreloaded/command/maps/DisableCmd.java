package net.gcnt.skywarsreloaded.command.maps;

import net.gcnt.skywarsreloaded.SkyWarsReloaded;
import net.gcnt.skywarsreloaded.command.Cmd;
import net.gcnt.skywarsreloaded.game.GameTemplate;
import net.gcnt.skywarsreloaded.wrapper.sender.SWCommandSender;

import java.util.ArrayList;
import java.util.List;

public class DisableCmd extends Cmd {

    public DisableCmd(SkyWarsReloaded plugin) {
        super(plugin, "skywarsmap", "disable", "skywars.command.map.disable", true, "<map>", "Disable a template.");
    }

    @Override
    public boolean run(SWCommandSender sender, String[] args) {
        if (args.length == 0) {
            sender.sendMessage(plugin.getUtils().colorize("&cPlease enter a game template name."));
            return false; // todo not sure if I should return false/true here to show the usage too or not.
        }

        final String templateName = args[0];
        GameTemplate template = plugin.getGameManager().getGameTemplateByName(templateName);
        if (template == null) {
            sender.sendMessage(plugin.getUtils().colorize("&cThere is no game template with that name."));
            return true;
        }
        if (!template.isEnabled()) {
            sender.sendMessage(plugin.getUtils().colorize("&cThis template is already disabled."));
            return true;
        }

        template.disable();
        sender.sendMessage(plugin.getUtils().colorize("&aThe template &e" + templateName + " &ahas successfully been &cdisabled&a!"));
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
