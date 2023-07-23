package com.walrusone.skywarsreloaded.commands.kits;

import com.walrusone.skywarsreloaded.menus.gameoptions.objects.GameKit;
import com.walrusone.skywarsreloaded.utilities.Messaging;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Collections;

public class ListCmd extends com.walrusone.skywarsreloaded.commands.BaseCmd {
    public ListCmd(String t) {
        type = t;
        forcePlayer = true;
        cmdName = "list";
        alias = new String[]{"li"};
        argLength = 1;
    }

    public boolean run(CommandSender sender, Player player, String[] args) {
        if (GameKit.getKits().size() < 1) {
            player.sendMessage(new Messaging.MessageFormatter().format("command.kit-listno"));
            return true;
        }
        player.sendMessage(new Messaging.MessageFormatter().format("command.kit-listheader"));
        player.sendMessage(new Messaging.MessageFormatter().format("command.kit-listheader2"));
        ArrayList<GameKit> sortedKits = new ArrayList<>(GameKit.getKits());
        Collections.sort(sortedKits, Collections.reverseOrder());

        for (GameKit kit : sortedKits) {
            if ((!kit.getName().equalsIgnoreCase(new Messaging.MessageFormatter().format("kit.vote-random"))) &&
                    (!kit.getName().equalsIgnoreCase(new Messaging.MessageFormatter().format("kit.vote-nokit")))) {
                String message;
                if (kit.getEnabled()) {
                    message = ChatColor.GREEN + "enabled" + "§f - §7"+kit.getWinCount()+"/"+kit.getUseCount()+" "+((kit.getUseCount()==0)?0:String.format( "%.2f" , kit.getWinCount()/(float)kit.getUseCount()*100)+"%");
                } else {
                    message = ChatColor.RED + "disabled" + "§f - §7"+kit.getWinCount()+"/"+kit.getUseCount()+" "+((kit.getUseCount()==0)?0:String.format( "%.2f" , kit.getWinCount()/(float)kit.getUseCount()*100)+"%");;
                }
                player.sendMessage(new Messaging.MessageFormatter().setVariable("filename", kit.getFilename())
                        .setVariable("position", "" + kit.getPosition()).setVariable("status", message).format("command.kit-list"));
            }
        }

        return true;
    }
}
