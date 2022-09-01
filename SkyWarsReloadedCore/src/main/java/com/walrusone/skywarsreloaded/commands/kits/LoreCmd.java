package com.walrusone.skywarsreloaded.commands.kits;

import com.walrusone.skywarsreloaded.commands.BaseCmd;
import com.walrusone.skywarsreloaded.menus.gameoptions.objects.GameKit;
import com.walrusone.skywarsreloaded.utilities.Messaging;
import com.walrusone.skywarsreloaded.utilities.Util;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class LoreCmd extends BaseCmd {
    public LoreCmd(String t) {
        type = t;
        forcePlayer = true;
        cmdName = "lore";
        alias = new String[]{"l"};
        argLength = 4;
    }

    public boolean run(CommandSender sender, Player player, String[] args) {
        // Arg numbers to make code easier to read
        int kitArgIndex = 1;
        int lineArgIndex = 2;
        int messageIndex = 3;

        String kitArg = args.length > kitArgIndex ? args[kitArgIndex] : "";
        String lineArg = args.length > lineArgIndex ? args[lineArgIndex] : "";
        String message;

        // Get kit by name
        GameKit kit = GameKit.getKit(kitArg);
        if (kit == null) {
            player.sendMessage(new Messaging.MessageFormatter().setVariable("kit", kitArg).format("command.no-kit"));
            return true;
        }

        // Compile message from command args
        StringBuilder strb = new StringBuilder();
        for (int i = messageIndex; i < args.length; i++) {
            strb.append(args[i]);
            strb.append(" ");
        }
        message = strb.toString();

        // Sanity check: is a number
        if (Util.get().isInteger(lineArg)) {
            int loreLineNumber = Integer.parseInt(lineArg);
            int loreLineIndex = loreLineNumber - 1;

            // Sanity check: line number
            if (loreLineNumber < 1 || loreLineNumber > 16) {
                player.sendMessage(ChatColor.RED + "The lore number must be between 1 - 16 or \"locked\".");
                return true;
            }

            // Add additional lines in between last existing line (excluded) and targeted line (included)
            int previousSize = kit.getLores().size();
            if (previousSize < loreLineNumber) {
                for (int line = previousSize; line < loreLineNumber; line++) {
                    kit.getLores().add(" ");
                }
            }

            // Update lore message at line
            kit.getLores().set(loreLineIndex, message.trim());

        } else if (args[2].equalsIgnoreCase("locked")) {
            kit.setLockedLore(strb.toString().trim());
        } else {
            player.sendMessage(new Messaging.MessageFormatter().format("command.kit-loreerror"));
            return true;
        }

        GameKit.saveKit(kit);

        player.sendMessage(new Messaging.MessageFormatter().setVariable("line", lineArg).setVariable("kit", kit.getColorName()).format("command.kit-lore"));
        return true;
    }
}
