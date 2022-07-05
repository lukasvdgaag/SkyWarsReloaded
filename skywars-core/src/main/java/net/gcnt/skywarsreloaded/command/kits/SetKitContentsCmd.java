package net.gcnt.skywarsreloaded.command.kits;

import net.gcnt.skywarsreloaded.SkyWarsReloaded;
import net.gcnt.skywarsreloaded.command.Cmd;
import net.gcnt.skywarsreloaded.game.kits.SWKit;
import net.gcnt.skywarsreloaded.utils.Item;
import net.gcnt.skywarsreloaded.utils.properties.MessageProperties;
import net.gcnt.skywarsreloaded.wrapper.sender.SWCommandSender;
import net.gcnt.skywarsreloaded.wrapper.entity.SWPlayer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class SetKitContentsCmd extends Cmd {

    public SetKitContentsCmd(SkyWarsReloaded plugin) {
        super(plugin, "skywarskit", "contents", "skywars.command.kit.contents", true, "<kit>", "Set the kit's contents.", "content", "items");
    }

    @Override
    public boolean run(SWCommandSender sender, String[] args) {
        if (args.length == 0) {
            plugin.getMessages().getMessage(MessageProperties.KITS_ENTER_NAME.toString()).send(sender);
            return true;
        }

        final String kitName = args[0];
        SWKit kit = plugin.getKitManager().getKitByName(kitName);
        SWPlayer swp = (SWPlayer) sender;
        if (kit == null) {
            plugin.getMessages().getMessage(MessageProperties.KITS_DOESNT_EXIST.toString()).replace("%kit%", kitName).send(sender);
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
        kit.updateItems();
        plugin.getMessages().getMessage(MessageProperties.KITS_SET_CONTENTS.toString()).replace("%kit%", kitName).send(sender);
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
