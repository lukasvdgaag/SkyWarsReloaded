package net.gcnt.skywarsreloaded.command.kits;

import com.google.common.collect.Lists;
import net.gcnt.skywarsreloaded.SkyWarsReloaded;
import net.gcnt.skywarsreloaded.command.Cmd;
import net.gcnt.skywarsreloaded.game.kits.SWKit;
import net.gcnt.skywarsreloaded.utils.properties.MessageProperties;
import net.gcnt.skywarsreloaded.wrapper.sender.SWCommandSender;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SetKitLoreCmd extends Cmd {

    public SetKitLoreCmd(SkyWarsReloaded plugin) {
        super(plugin, "skywarskit", "lore", "skywars.command.kit.lore", false, "<kit> <add/remove/clear/preview> [value]", "Manage the kit's lore.", "l");
    }

    @Override
    public boolean run(SWCommandSender sender, String[] args) {
        if (args.length == 0) {
            plugin.getMessages().getMessage(MessageProperties.KITS_ENTER_NAME.toString()).send(sender);
            return true;
        } else if (args.length == 1) {
            plugin.getMessages().getMessage(MessageProperties.KITS_ENTER_LORE_ACTION.toString()).send(sender);
            return true;
        }

        String action = args[1].toLowerCase();
        if (action.equals("add") || action.equals("remove")) {
            if (args.length == 2) {
                // no value entered.
                plugin.getMessages().getMessage(MessageProperties.KITS_ENTER_LORE_LINE.toString()).send(sender);
                return true;
            } else if (action.equals("remove")) {
                // value entered, action is remove.
                if (!plugin.getUtils().isInt(args[2])) {
                    plugin.getMessages().getMessage(MessageProperties.KITS_ENTER_LORE_INDEX_NUMBER.toString()).send(sender);
                    return true;
                }
                int index = Integer.parseInt(args[2]);
                if (index < 1) {
                    plugin.getMessages().getMessage(MessageProperties.KITS_ENTER_LORE_INDEX_GREATER.toString()).send(sender);
                    return true;
                }
            }
        } else if (!action.equals("clear") && !action.equals("preview")) {
            plugin.getMessages().getMessage(MessageProperties.KITS_ENTER_LORE_ACTION.toString()).send(sender);
            return true;
        }

        final String kitName = args[0];
        SWKit kit = plugin.getKitManager().getKitByName(kitName);
        if (kit == null) {
            plugin.getMessages().getMessage(MessageProperties.KITS_DOESNT_EXIST.toString()).replace("%kit%", kitName).send(sender);
            return true;
        }

        final List<String> lore = kit.getLore();
        switch (action) {
            case "add":
                args = Arrays.copyOfRange(args, 2, args.length);
                String value = String.join(" ", args);
                lore.add(value);
                plugin.getMessages().getMessage(MessageProperties.KITS_ADDED_LORE_LINE.toString()).replace("%kit%", kitName).replace("%value%", value).send(sender);
                break;
            case "remove":
                int index = Integer.parseInt(args[2]);
                if (index > lore.size()) {
                    // out of bounds
                    plugin.getMessages().getMessage(MessageProperties.KITS_ENTER_LORE_INDEX_INVALID.toString()).replace("%kit%", kitName).replace("%max%", lore.size() + "").send(sender);
                    return true;
                }
                lore.remove(index - 1);
                plugin.getMessages().getMessage(MessageProperties.KITS_REMOVED_LORE_LINE.toString()).replace("%kit%", kitName).replace("%line%", index + "").send(sender);
                break;
            case "clear":
                kit.setLore(new ArrayList<>());
                plugin.getMessages().getMessage(MessageProperties.KITS_CLEARED_LORE.toString()).replace("%kit%", kitName).send(sender);
                break;
            default:
                // action is preview
                plugin.getMessages().getMessage(MessageProperties.KITS_PREVIEW_LORE_HEADER.toString()).replace("%kit%", kitName).send(sender);
                if (lore.isEmpty()) {
                    plugin.getMessages().getMessage(MessageProperties.KITS_PREVIEW_LORE_NO_LINES.toString()).replace("%kit%", kitName).send(sender);
                } else {
                    for (int i = 0; i < lore.size(); i++) {
                        plugin.getMessages().getMessage(MessageProperties.KITS_PREVIEW_LORE_LINE.toString()).replace("%kit%", kitName).replace("%line%", (i + 1) + "").replace("%value%", lore.get(i)).send(sender);
                    }
                }
                break;
        }
        kit.saveData();
        return true;
    }

    @Override
    public List<String> onTabCompletion(SWCommandSender sender, String[] args) {
        if (args.length == 1) {
            List<String> kits = new ArrayList<>();
            plugin.getKitManager().getKits().forEach(kit -> kits.add(kit.getId()));
            return kits;
        } else if (args.length == 2) {
            return Lists.newArrayList("add", "remove", "clear", "preview");
        } else if (args.length == 3) {
            if (!args[1].equalsIgnoreCase("remove")) return new ArrayList<>();
            SWKit kit = plugin.getKitManager().getKitByName(args[0]);
            if (kit == null) return new ArrayList<>();

            List<String> options = new ArrayList<>();
            for (int i = 0; i < kit.getLore().size(); i++) {
                options.add((i + 1) + "");
            }
            return options;
        }
        return new ArrayList<>();
    }
}
