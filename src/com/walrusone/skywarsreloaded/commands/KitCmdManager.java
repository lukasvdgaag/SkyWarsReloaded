package com.walrusone.skywarsreloaded.commands;

import com.walrusone.skywarsreloaded.api.command.SWRCmdManagerAPI;
import com.walrusone.skywarsreloaded.commands.kits.*;
import com.walrusone.skywarsreloaded.utilities.Messaging;
import com.walrusone.skywarsreloaded.utilities.Util;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.List;

public class KitCmdManager implements CommandExecutor, SWRCmdManagerAPI {
    private List<BaseCmd> kitcmds = new ArrayList<>();
    private static KitCmdManager kcm;

    //Add New Commands Here
    public KitCmdManager() {
        kcm = this;
        kitcmds.add(new CreateCmd("kit"));
        kitcmds.add(new EnableCmd("kit"));
        kitcmds.add(new IconCmd("kit"));
        kitcmds.add(new LockedIconCmd("kit"));
        kitcmds.add(new LoadCmd("kit"));
        kitcmds.add(new LoreCmd("kit"));
        kitcmds.add(new NameCmd("kit"));
        kitcmds.add(new PositionCmd("kit"));
        kitcmds.add(new PermCmd("kit"));
        kitcmds.add(new UpdateCmd("kit"));
        kitcmds.add(new ListCmd("kit"));
    }

    public static List<BaseCmd> getCommands() { return kcm.kitcmds; }

    public boolean onCommand(CommandSender s, Command command, String label, String[] args) {
        if (args.length == 0 || getCommand(args[0]) == null) {
            s.sendMessage(new Messaging.MessageFormatter().format("helpList.header"));
            sendHelp(kitcmds, s);
            s.sendMessage(new Messaging.MessageFormatter().format("helpList.footer"));
        } else getCommand(args[0]).processCmd(s, args);
        return true;
    }

    private void sendHelp(List<BaseCmd> cmds, CommandSender s) {
        int count = 0;
        for (BaseCmd cmd : cmds) {
            if (Util.get().hp(cmd.getType(), s, cmd.cmdName)) {
                count++;
                if (count == 1) {
                    s.sendMessage(" ");
                    s.sendMessage(new Messaging.MessageFormatter().format("helpList.swkit.header" + 1));
                }
                s.sendMessage(new Messaging.MessageFormatter().format("helpList.swkit." + cmd.cmdName));
            }
        }
    }

    public BaseCmd getCommand(String s) {
        return getCmd(kitcmds, s);
    }

    private BaseCmd getCmd(List<BaseCmd> cmds, String s) {
        for (BaseCmd cmd : cmds) {
            if (cmd.cmdName.equalsIgnoreCase(s)) {
                return cmd;
            }
            for (String alias : cmd.alias) {
                if (alias.equalsIgnoreCase(s))
                    return cmd;
            }
        }
        return null;
    }

    @Override
    public void registerCommand(BaseCmd commandIn) {
        if (commandIn == null) return;
        kitcmds.add(commandIn);
    }

    @Override
    public void unregisterCommand(BaseCmd commandIn) {
        if (commandIn == null) return;
        kitcmds.remove(commandIn);
    }

    @Override
    public BaseCmd getSubCommand(String name) {
        return null;
    }
}
