package net.gcnt.skywarsreloaded.command.maps;

import net.gcnt.skywarsreloaded.SkyWarsReloaded;
import net.gcnt.skywarsreloaded.command.Cmd;
import net.gcnt.skywarsreloaded.game.GameTemplate;
import net.gcnt.skywarsreloaded.utils.properties.MessageProperties;
import net.gcnt.skywarsreloaded.wrapper.sender.SWCommandSender;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SetMapCreatorCmd extends Cmd {

    public SetMapCreatorCmd(SkyWarsReloaded plugin) {
        super(plugin, "skywarsmap", "creator", "skywars.command.map.creator", true, "<map> <creator>", "Set the map creator.", "maker");
    }

    @Override
    public boolean run(SWCommandSender sender, String[] args) {
        if (args.length == 0) {
            plugin.getMessages().getMessage(MessageProperties.MAPS_ENTER_NAME.toString()).send(sender);
            return true;
        } else if (args.length == 1) {
            plugin.getMessages().getMessage(MessageProperties.MAPS_ENTER_CREATOR.toString()).send(sender);
            return true;
        }

        final String templateName = args[0];
        GameTemplate template = plugin.getGameManager().getGameTemplateByName(templateName);
        if (template == null) {
            plugin.getMessages().getMessage(MessageProperties.MAPS_DOESNT_EXIST.toString()).send(sender);
            return true;
        }

        args = Arrays.copyOfRange(args, 1, args.length);
        String creator = String.join(" ", args);
        template.setCreator(creator);
        plugin.getMessages().getMessage(MessageProperties.MAPS_SET_CREATOR.toString())
                .replace("%template%", template.getName())
                .replace("%creator%", creator)
                .send(sender);

        template.checkToDoList(sender);
        // todo: save template to storage
        return true;
    }

    @Override
    public List<String> onTabCompletion(SWCommandSender sender, String[] args) {
        if (args.length == 1) {
            List<String> maps = new ArrayList<>();
            plugin.getGameManager().getGameTemplatesCopy().forEach(template -> maps.add(template.getName()));
            return maps;
        } else if (args.length == 2) {
            return new ArrayList<>();
        }
        return new ArrayList<>();
    }
}
