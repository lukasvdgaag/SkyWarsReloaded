package net.gcnt.skywarsreloaded.command.kits;

import net.gcnt.skywarsreloaded.SkyWarsReloaded;
import net.gcnt.skywarsreloaded.command.Cmd;
import net.gcnt.skywarsreloaded.game.kits.SWKit;
import net.gcnt.skywarsreloaded.wrapper.SWCommandSender;
import net.gcnt.skywarsreloaded.wrapper.SWPlayer;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class PreviewKitCmd extends Cmd {

    public PreviewKitCmd(SkyWarsReloaded plugin) {
        super(plugin, "skywarskit", "preview", "skywars.command.kit.preview", true, "<kit>", "Preview the kit.");
    }

    @Override
    public boolean run(SWCommandSender sender, String[] args) {
        if (args.length == 0) {
            sender.sendMessage(plugin.getUtils().colorize("&cPlease enter a kit name."));
            return true;
        }

        final String kitName = args[0];
        SWKit kit = plugin.getKitManager().getKitByName(kitName);
        SWPlayer swp = (SWPlayer) sender;
        if (kit == null) {
            sender.sendMessage(plugin.getUtils().colorize("&cThere is no kit with that name."));
            return true;
        }

        kit.giveToPlayer(swp);

        sender.sendMessage(plugin.getUtils().colorize("&aThe kit &e" + kitName + " &ahas been previewed in your inventory!"));
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
