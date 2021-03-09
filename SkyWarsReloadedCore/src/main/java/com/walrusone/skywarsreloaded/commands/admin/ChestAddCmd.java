package com.walrusone.skywarsreloaded.commands.admin;

import com.walrusone.skywarsreloaded.SkyWarsReloaded;
import com.walrusone.skywarsreloaded.commands.BaseCmd;
import com.walrusone.skywarsreloaded.enums.ChestType;
import com.walrusone.skywarsreloaded.utilities.Messaging;
import com.walrusone.skywarsreloaded.utilities.Util;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class ChestAddCmd extends BaseCmd {

    public ChestAddCmd(String t) {
        type = t;
        forcePlayer = true;
        cmdName = "chestadd";
        alias = new String[]{"ca"};
        argLength = 4; //counting cmdName
    }

    @Override
    public boolean run() {

        String type = args[1];
        ChestType ct;
        if (type.equalsIgnoreCase("basic")) {
            ct = ChestType.BASIC;
        } else if (type.equalsIgnoreCase("basiccenter")) {
            ct = ChestType.BASICCENTER;
        } else if (type.equalsIgnoreCase("normal")) {
            ct = ChestType.NORMAL;
        } else if (type.equalsIgnoreCase("normalcenter")) {
            ct = ChestType.NORMALCENTER;
        } else if (type.equalsIgnoreCase("op")) {
            ct = ChestType.OP;
        } else if (type.equalsIgnoreCase("opcenter")) {
            ct = ChestType.OPCENTER;
        } else if (type.equalsIgnoreCase("crate")) {
            ct = ChestType.CRATE;
        } else {
            player.sendMessage(new Messaging.MessageFormatter().format("error.chesttype"));
            return true;
        }

        int percent;
        if (Util.get().isInteger(args[3])) {
            percent = Integer.parseInt(args[3]);
        } else {
            player.sendMessage(new Messaging.MessageFormatter().format("error.chestpercent"));
            return true;
        }

        if (!(percent > 0 && percent <= 100)) {
            player.sendMessage(new Messaging.MessageFormatter().format("error.chestpercent"));
            return true;
        }


        String method = args[2];
        if (!method.equalsIgnoreCase("hand") && !method.equalsIgnoreCase("inv")) {
            player.sendMessage(new Messaging.MessageFormatter().format("error.chestmethod"));
            return true;
        }

        List<ItemStack> items = new ArrayList<>();
        if (method.equalsIgnoreCase("hand")) {
            ItemStack item = SkyWarsReloaded.getNMS().getMainHandItem(player).clone();
            items.add(item);
        } else {
            ItemStack[] inventory = player.getInventory().getContents();
            for (ItemStack item : inventory) {
                if (item != null && !item.getType().equals(Material.AIR)) {
                    items.add(item);
                }
            }
        }

        SkyWarsReloaded.getCM().addItems(items, ct, percent);
        String meth = "INVENTORY";
        if (method.equalsIgnoreCase("hand")) {
            meth = "HAND";
        }

        player.sendMessage(new Messaging.MessageFormatter().setVariable("method", meth)
                .setVariable("type", type.toUpperCase()).setVariable("percent", "" + percent).format("command.chestadd"));
        return true;
    }

}