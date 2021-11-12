package net.gcnt.skywarsreloaded.command.kits;

import net.gcnt.skywarsreloaded.SkyWarsReloaded;
import net.gcnt.skywarsreloaded.command.Cmd;
import net.gcnt.skywarsreloaded.game.kits.SWKit;
import net.gcnt.skywarsreloaded.utils.Item;
import net.gcnt.skywarsreloaded.wrapper.SWCommandSender;
import net.gcnt.skywarsreloaded.wrapper.SWPlayer;

import java.util.HashMap;

public class SetKitContentsCmd extends Cmd {

    public SetKitContentsCmd(SkyWarsReloaded plugin) {
        super(plugin, "skywarskit", "contents", "skywars.command.kit.contents", true, "<kit>", "Set the kit's contents.");
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

        HashMap<Integer, Item> items = new HashMap<>();
        final Item[] inventory = swp.getInventory();
        for (int i = 0; i < inventory.length; i++) {
            if (inventory[i] != null) items.put(i, inventory[i]);
        }

        kit.setContents(items);
        kit.setHelmet(swp.getHelmet());
        kit.setChestplate(swp.getChestplate());
        kit.setLeggings(swp.getLeggings());
        kit.setBoots(swp.getBoots());
        if (plugin.getUtils().getServerVersion() >= 9) {
            kit.setOffHand(swp.getItemInHand(true));
        }

        kit.saveData();
        sender.sendMessage(plugin.getUtils().colorize("&aThe contents of the kit &e" + kitName + " &ahave been changed to &ethe items in your inventory&a!"));
        return true;
    }
}
