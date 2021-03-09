package com.walrusone.skywarsreloaded.commands.admin;

import com.walrusone.skywarsreloaded.SkyWarsReloaded;
import com.walrusone.skywarsreloaded.commands.BaseCmd;
import com.walrusone.skywarsreloaded.enums.ChestType;
import com.walrusone.skywarsreloaded.utilities.Messaging;
import com.walrusone.skywarsreloaded.utilities.Util;

public class ChestEditCmd extends BaseCmd {

    public ChestEditCmd(String t) {
        type = t;
        forcePlayer = true;
        cmdName = "chestedit";
        alias = new String[]{"ce"};
        argLength = 3; //counting cmdName
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
        if (Util.get().isInteger(args[2])) {
            percent = Integer.parseInt(args[2]);
        } else {
            player.sendMessage(new Messaging.MessageFormatter().format("error.chestpercent"));
            return true;
        }

        if (!(percent > 0 && percent <= 100)) {
            player.sendMessage(new Messaging.MessageFormatter().format("error.chestpercent"));
            return true;
        }

        SkyWarsReloaded.getCM().editChest(ct, percent, player);

        return true;
    }

}