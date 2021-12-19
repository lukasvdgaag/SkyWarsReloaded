package net.gcnt.skywarsreloaded.command.kits;

import net.gcnt.skywarsreloaded.SkyWarsReloaded;
import net.gcnt.skywarsreloaded.command.Cmd;
import net.gcnt.skywarsreloaded.game.kits.SWKit;
import net.gcnt.skywarsreloaded.utils.properties.MessageProperties;
import net.gcnt.skywarsreloaded.wrapper.sender.SWCommandSender;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SetKitDescriptionCmd extends Cmd {

    public SetKitDescriptionCmd(SkyWarsReloaded plugin) {
        super(plugin, "skywarskit", "description", "skywars.command.kit.description", false, "<kit> <displayname>", "Set the kit's description.", "desc");
    }

    @Override
    public boolean run(SWCommandSender sender, String[] args) {
        if (args.length == 0) {
            plugin.getMessages().getMessage(MessageProperties.KITS_ENTER_NAME.toString()).send(sender);
            return true;
        } else if (args.length == 1) {
            plugin.getMessages().getMessage(MessageProperties.KITS_ENTER_DESCRIPTION.toString()).send(sender);
            return true;
        }

        final String kitName = args[0];
        SWKit kit = plugin.getKitManager().getKitByName(kitName);
        if (kit == null) {
            plugin.getMessages().getMessage(MessageProperties.KITS_DOESNT_EXIST.toString()).replace("%kit%", kitName).send(sender);
            return true;
        }

        args = Arrays.copyOfRange(args, 1, args.length);
        String description = String.join(" ", args);
        kit.setDescription(description);
        kit.saveData();

        plugin.getMessages().getMessage(MessageProperties.KITS_SET_DESCRIPTION.toString()).replace("%kit%", kitName).replace("%description%", description).send(sender);
        return true;
    }

    @Override
    public List<String> onTabCompletion(SWCommandSender sender, String[] args) {
        if (args.length == 1) {
            List<String> kits = new ArrayList<>();
            plugin.getKitManager().getKits().forEach(kit -> kits.add(kit.getId()));
            return kits;
        }
        return new ArrayList<>();
    }

}
