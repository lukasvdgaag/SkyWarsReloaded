package net.gcnt.skywarsreloaded.command.kits;

import net.gcnt.skywarsreloaded.SkyWarsReloaded;
import net.gcnt.skywarsreloaded.command.Cmd;
import net.gcnt.skywarsreloaded.game.kits.SWKit;
import net.gcnt.skywarsreloaded.wrapper.SWCommandSender;

import java.util.Arrays;

public class SetKitSlotCmd extends Cmd {

    public SetKitSlotCmd(SkyWarsReloaded plugin) {
        super(plugin, "skywarskit", "slot", "skywars.command.kit.slot", false, "<kit> <slot>", "Set the kit's menu slot.");
    }

    @Override
    public boolean run(SWCommandSender sender, String[] args) {
        if (args.length == 0) {
            sender.sendMessage(plugin.getUtils().colorize("&cPlease enter a kit name."));
            return true;
        } else if (args.length == 1) {
            sender.sendMessage(plugin.getUtils().colorize("&cPlease enter the menu slot of the kit."));
            return true;
        }

        if (!plugin.getUtils().isInt(args[1])) {
            sender.sendMessage(plugin.getUtils().colorize("&cPlease enter a valid slot number (number)."));
            return false;
        }
        int slot = Integer.parseInt(args[1]);
        if (slot < 0) {
            sender.sendMessage(plugin.getUtils().colorize("&cPlease enter a valid slot number (0 or greater)"));
            return false;
        }

        final String kitName = args[0];
        SWKit kit = plugin.getKitManager().getKitByName(kitName);
        if (kit == null) {
            sender.sendMessage(plugin.getUtils().colorize("&cThere is no kit with that name."));
            return true;
        }

        kit.setSlot(slot);
        sender.sendMessage(plugin.getUtils().colorize("&aThe menu slot of the kit &e" + kitName + " &ahas been changed to &e" + slot + "&a!"));
        return true;
    }
}
