package net.gcnt.skywarsreloaded.command.kits;

import com.google.common.collect.Lists;
import net.gcnt.skywarsreloaded.SkyWarsReloaded;
import net.gcnt.skywarsreloaded.command.Cmd;
import net.gcnt.skywarsreloaded.game.kits.SWKit;
import net.gcnt.skywarsreloaded.wrapper.SWCommandSender;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SetKitLoreCmd extends Cmd {

    public SetKitLoreCmd(SkyWarsReloaded plugin) {
        super(plugin, "skywarskit", "lore", "skywars.command.kit.lore", false, "<kit> <add/remove/clear/preview> [value]", "Manage the kit's lore.");
    }

    @Override
    public boolean run(SWCommandSender sender, String[] args) {
        if (args.length == 0) {
            sender.sendMessage(plugin.getUtils().colorize("&cPlease enter a kit name."));
            return true;
        } else if (args.length == 1) {
            sender.sendMessage(plugin.getUtils().colorize(String.format("&cPlease enter the action you want to execute on the kit's lore. Options: &7%s&f, &7%s&f, &7%s&f, &7%s", "add", "remove", "clear", "preview")));
            return true;
        }

        String action = args[1].toLowerCase();
        if (action.equals("add") || action.equals("remove")) {
            if (args.length == 2) {
                // no value entered.
                sender.sendMessage(plugin.getUtils().colorize("&cPlease enter a lore line value."));
                return true;
            } else if (action.equals("remove")) {
                // value entered, action is remove.
                if (!plugin.getUtils().isInt(args[2])) {
                    sender.sendMessage(plugin.getUtils().colorize("&cPlease enter a valid lore line index (number)."));
                    return true;
                }
                int index = Integer.parseInt(args[2]);
                if (index < 1) {
                    sender.sendMessage(plugin.getUtils().colorize("&cPlease enter a valid lore line index (greater than 0)"));
                    return true;
                }
            }
        } else if (!action.equals("clear") && !action.equals("preview")) {
            sender.sendMessage(plugin.getUtils().colorize(String.format("&cPlease enter a valid lore edit action. Options: &7%s&f, &7%s&f, &7%s&f, &7%s", "add", "remove", "clear", "preview")));
            return true;
        }

        final String kitName = args[0];
        SWKit kit = plugin.getKitManager().getKitByName(kitName);
        if (kit == null) {
            sender.sendMessage(plugin.getUtils().colorize("&cThere is no kit with that name."));
            return true;
        }

        final List<String> lore = kit.getLore();
        switch (action) {
            case "add" -> {
                args = Arrays.copyOfRange(args, 2, args.length);
                String value = String.join(" ", args);
                lore.add(value);
                sender.sendMessage(plugin.getUtils().colorize("&aA new lore line has been added to kit &e" + kitName + " &awith the value &e'" + value + "&e'&a!"));
            }
            case "remove" -> {
                int index = Integer.parseInt(args[2]);
                if (index > lore.size()) {
                    // out of bounds
                    sender.sendMessage(plugin.getUtils().colorize("&cPlease enter a valid lore line index (max is " + lore.size() + ")"));
                    return true;
                }
                lore.remove(index - 1);
                sender.sendMessage(plugin.getUtils().colorize("&aThe lore line with index &e" + index + "&a has been &cremoved &afrom kit &e" + kitName + "&a!"));
            }
            case "clear" -> {
                kit.setLore(new ArrayList<>());
                sender.sendMessage(plugin.getUtils().colorize("&aThe lore of the kit &e" + kitName + "&a has been &ccleared&a!"));
            }
            default -> {
                // action is preview
                sender.sendMessage(plugin.getUtils().colorize("&7Lore of kit &a&n" + kitName + "&r&7:"));
                if (lore.isEmpty()) {
                    sender.sendMessage(plugin.getUtils().colorize("&cThis kit does not have any lore lines."));
                } else {
                    for (int i = 0; i < lore.size(); i++) {
                        sender.sendMessage(plugin.getUtils().colorize(String.format("&e%2d: &f%s", i + 1, lore.get(i))));
                    }
                }
            }
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
