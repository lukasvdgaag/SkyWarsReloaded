package net.gcnt.skywarsreloaded.command.kits;

import net.gcnt.skywarsreloaded.SkyWarsReloaded;
import net.gcnt.skywarsreloaded.command.Cmd;
import net.gcnt.skywarsreloaded.game.kits.SWKit;
import net.gcnt.skywarsreloaded.utils.Item;
import net.gcnt.skywarsreloaded.wrapper.SWCommandSender;
import net.gcnt.skywarsreloaded.wrapper.SWPlayer;

import java.util.HashMap;

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

        swp.clearInventory();

        kit.getContents().forEach(swp::setSlot);
        swp.setHelmet(kit.getHelmet());
        swp.setChestplate(kit.getChestplate());
        swp.setLeggings(kit.getLeggings());
        swp.setBoots(kit.getBoots());

        sender.sendMessage(plugin.getUtils().colorize("&aThe kit &e" + kitName + " &ahas been previewed in your inventory!"));
        return true;
    }
}
