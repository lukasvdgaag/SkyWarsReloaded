package net.gcnt.skywarsreloaded.command.kits;

import net.gcnt.skywarsreloaded.SkyWarsReloaded;
import net.gcnt.skywarsreloaded.command.Cmd;
import net.gcnt.skywarsreloaded.game.kits.SWKit;
import net.gcnt.skywarsreloaded.utils.Item;
import net.gcnt.skywarsreloaded.wrapper.SWCommandSender;
import net.gcnt.skywarsreloaded.wrapper.SWPlayer;

public class CreateKitCmd extends Cmd {

    public CreateKitCmd(SkyWarsReloaded plugin) {
        super(plugin, "skywarskit", "create", "skywars.command.kit.create", true, "<name>", "Create a new kit");
    }

    @Override
    public boolean run(SWCommandSender sender, String[] args) {
        if (args.length == 0) {
            sender.sendMessage(plugin.getUtils().colorize("&cPlease enter a kit name."));
            return false;
        }

        final String kitName = args[0];
        SWKit kit = plugin.getKitManager().createKit(kitName);
        if (kit == null) {
            sender.sendMessage(plugin.getUtils().colorize("&cThere already is a kit with that name."));
            return true;
        }

        SWPlayer swp = (SWPlayer) sender;
        Item item = swp.getItemInHand(false);
        if (item == null) {
            sender.sendMessage(plugin.getUtils().colorize("&cYou must hold an item in your hand to continue."));
            return true;
        }

        kit.setIcon(item);

        sender.sendMessage(plugin.getUtils().colorize("&aA new kit with the name &e" + kitName + "&a has successfully been created."));
        return true;
    }
}
