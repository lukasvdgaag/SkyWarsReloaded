package com.walrusone.skywarsreloaded.commands;

import com.walrusone.skywarsreloaded.utilities.Messaging;
import com.walrusone.skywarsreloaded.utilities.Util;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public abstract class BaseCmd {

    public String[] alias;
    public String cmdName;
    public int argLength = 0;
    public boolean forcePlayer = true;
    public String type;
    public int maxArgs = -1;

    public BaseCmd() {
    }

    void processCmd(CommandSender sender, String[] args) {
        Player player = null;

        if (this.forcePlayer) {
            if (!(sender instanceof Player)) {
                sender.sendMessage(new Messaging.MessageFormatter().format("error.must-be-player"));
                return;
            }
            player = ((Player) sender);
        }


        if (!Util.get().hasPerm(type, sender, cmdName)) {
            sender.sendMessage(new Messaging.MessageFormatter().format("error.cmd-no-perm"));
        } else if ((maxArgs == -1 && argLength > args.length) || (maxArgs!=-1 && args.length > maxArgs)) {
            sender.sendMessage(ChatColor.DARK_RED + "Wrong usage: " + new Messaging.MessageFormatter().format("helpList." + Util.get().getMessageKey(type) + "." + cmdName));
        } else {
            boolean returnVal = run(sender, player, args);
            if (!returnVal) {
                sender.sendMessage(ChatColor.DARK_RED + "Wrong usage: " + new Messaging.MessageFormatter().format("helpList." + Util.get().getMessageKey(type) + "." + cmdName));
            }
        }
    }

    public String getType() {
        return type;
    }

    public abstract boolean run(CommandSender sender, Player player, String[] args);
}
