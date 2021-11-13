package net.gcnt.skywarsreloaded.command.kits;

import net.gcnt.skywarsreloaded.SkyWarsReloaded;
import net.gcnt.skywarsreloaded.command.Cmd;
import net.gcnt.skywarsreloaded.game.kits.SWKit;
import net.gcnt.skywarsreloaded.wrapper.SWCommandSender;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SetKitDescriptionCmd extends Cmd {

    public SetKitDescriptionCmd(SkyWarsReloaded plugin) {
        super(plugin, "skywarskit", "description", "skywars.command.kit.description", false, "<kit> <displayname>", "Set the kit's description.");
    }

    @Override
    public boolean run(SWCommandSender sender, String[] args) {
        if (args.length == 0) {
            sender.sendMessage(plugin.getUtils().colorize("&cPlease enter a kit name."));
            return true;
        } else if (args.length == 1) {
            sender.sendMessage(plugin.getUtils().colorize("&cPlease enter the description of the kit."));
            return true;
        }

        final String kitName = args[0];
        SWKit kit = plugin.getKitManager().getKitByName(kitName);
        if (kit == null) {
            sender.sendMessage(plugin.getUtils().colorize("&cThere is no kit with that name."));
            return true;
        }

        args = Arrays.copyOfRange(args, 1, args.length);
        String description = String.join(" ", args);
        kit.setDescription(description);
        kit.saveData();

        sender.sendMessage(plugin.getUtils().colorize("&aThe description of the kit &e" + kitName + " &ahas been changed to &e" + description + "&a!"));
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
