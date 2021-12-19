package net.gcnt.skywarsreloaded.command.kits;

import net.gcnt.skywarsreloaded.SkyWarsReloaded;
import net.gcnt.skywarsreloaded.command.Cmd;
import net.gcnt.skywarsreloaded.game.kits.SWKit;
import net.gcnt.skywarsreloaded.utils.properties.MessageProperties;
import net.gcnt.skywarsreloaded.wrapper.sender.SWCommandSender;

import java.util.ArrayList;
import java.util.List;

public class SetKitSlotCmd extends Cmd {

    public SetKitSlotCmd(SkyWarsReloaded plugin) {
        super(plugin, "skywarskit", "slot", "skywars.command.kit.slot", false, "<kit> <slot>", "Set the kit's menu slot.", "s");
    }

    @Override
    public boolean run(SWCommandSender sender, String[] args) {
        if (args.length == 0) {
            plugin.getMessages().getMessage(MessageProperties.KITS_ENTER_NAME.toString()).send(sender);
            return true;
        } else if (args.length == 1) {
            plugin.getMessages().getMessage(MessageProperties.KITS_ENTER_SLOT.toString()).send(sender);
            return true;
        }

        if (!plugin.getUtils().isInt(args[1])) {
            plugin.getMessages().getMessage(MessageProperties.KITS_ENTER_SLOT_NUMBER.toString()).send(sender);
            return false;
        }
        int slot = Integer.parseInt(args[1]);
        if (slot < 0) {
            plugin.getMessages().getMessage(MessageProperties.KITS_ENTER_SLOT_GREATER.toString()).send(sender);
            return false;
        }

        final String kitName = args[0];
        SWKit kit = plugin.getKitManager().getKitByName(kitName);
        if (kit == null) {
            plugin.getMessages().getMessage(MessageProperties.KITS_DOESNT_EXIST.toString()).replace("%kit%", kitName).send(sender);
            return true;
        }

        kit.setSlot(slot);
        plugin.getMessages().getMessage(MessageProperties.KITS_SET_SLOT.toString())
                .replace("%kit%", kitName)
                .replace("%slot%", slot + "")
                .send(sender);
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
