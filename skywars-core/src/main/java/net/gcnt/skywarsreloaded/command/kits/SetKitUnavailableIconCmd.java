package net.gcnt.skywarsreloaded.command.kits;

import net.gcnt.skywarsreloaded.SkyWarsReloaded;
import net.gcnt.skywarsreloaded.command.Cmd;
import net.gcnt.skywarsreloaded.game.kits.SWKit;
import net.gcnt.skywarsreloaded.utils.Item;
import net.gcnt.skywarsreloaded.wrapper.SWCommandSender;
import net.gcnt.skywarsreloaded.wrapper.SWPlayer;

public class SetKitUnavailableIconCmd extends Cmd {

    public SetKitUnavailableIconCmd(SkyWarsReloaded plugin) {
        super(plugin, "skywarskit", "unavailableicon", "skywars.command.kit.unavailable-icon", true, "<kit>", "Set the kit unavailable icon.");
    }

    @Override
    public boolean run(SWCommandSender sender, String[] args) {
        if (args.length == 0) {
            sender.sendMessage(plugin.getUtils().colorize("&cPlease enter a kit name."));
            return true;
        }

        SWPlayer swp = (SWPlayer) sender;
        Item item = swp.getItemInHand(false);
        if (item == null) {
            sender.sendMessage(plugin.getUtils().colorize("&cYou must hold an item in your hand to continue."));
            return true;
        }

        final String kitName = args[0];
        SWKit kit = plugin.getKitManager().getKitByName(kitName);
        if (kit == null) {
            sender.sendMessage(plugin.getUtils().colorize("&cThere is no kit with that name."));
            return true;
        }

        kit.setUnavailableIcon(item);
        kit.saveData();
        sender.sendMessage(plugin.getUtils().colorize("&aThe unavailable icon of the kit &e" + kitName + " &ahas been changed to &ethe item you're holding&a!"));
        return true;
    }
}
