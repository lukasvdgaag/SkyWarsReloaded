package net.gcnt.skywarsreloaded.command.kits;

import net.gcnt.skywarsreloaded.SkyWarsReloaded;
import net.gcnt.skywarsreloaded.command.Cmd;
import net.gcnt.skywarsreloaded.game.kits.SWKit;
import net.gcnt.skywarsreloaded.utils.Item;
import net.gcnt.skywarsreloaded.utils.properties.MessageProperties;
import net.gcnt.skywarsreloaded.wrapper.sender.SWCommandSender;
import net.gcnt.skywarsreloaded.wrapper.player.SWPlayer;

public class CreateKitCmd extends Cmd {

    public CreateKitCmd(SkyWarsReloaded plugin) {
        super(plugin, "skywarskit", "create", "skywars.command.kit.create", true, "<name>", "Create a new kit", "c");
    }

    @Override
    public boolean run(SWCommandSender sender, String[] args) {
        if (args.length == 0) {
            plugin.getMessages().getMessage(MessageProperties.KITS_ENTER_NAME.toString()).send(sender);
            return false;
        }
        SWPlayer swp = (SWPlayer) sender;
        Item item = swp.getItemInHand(false);
        if (item == null) {
            plugin.getMessages().getMessage(MessageProperties.ERROR_MUST_HOLD_ITEM.toString()).send(sender);
            return true;
        }

        final String kitName = args[0];
        SWKit kit = plugin.getKitManager().createKit(kitName, item);
        if (kit == null) {
            plugin.getMessages().getMessage(MessageProperties.KITS_ALREADY_EXISTS.toString()).replace("%kit%", kitName).send(sender);
            return true;
        }

        plugin.getMessages().getMessage(MessageProperties.KITS_CREATED.toString()).replace("%kit%", kitName).send(sender);
        return true;
    }
}
