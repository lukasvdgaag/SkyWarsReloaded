package com.walrusone.skywarsreloaded.commands;


import com.walrusone.skywarsreloaded.api.command.SWRCmdManagerAPI;
import com.walrusone.skywarsreloaded.commands.party.*;
import com.walrusone.skywarsreloaded.utilities.Messaging;
import com.walrusone.skywarsreloaded.utilities.Util;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.List;

public class PartyCmdManager implements CommandExecutor, SWRCmdManagerAPI {
    private List<BaseCmd> partycmds = new ArrayList<>();

    //Add New Commands Here
    public PartyCmdManager() {
        partycmds.add(new AcceptCmd("party"));
        partycmds.add(new CreateCmd("party"));
        partycmds.add(new DisbandCmd("party"));
        partycmds.add(new InviteCmd("party"));
        partycmds.add(new LeaveCmd("party"));
        partycmds.add(new NameCmd("party"));
        partycmds.add(new InfoCmd("party"));
        partycmds.add(new DeclineCmd("party"));
    }

    public boolean onCommand(CommandSender s, Command command, String label, String[] args) {
        if (args.length == 0 || getCommand(args[0]) == null) {
            s.sendMessage(new Messaging.MessageFormatter().format("helpList.header"));
            sendHelp(partycmds, s);
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
                    s.sendMessage(new Messaging.MessageFormatter().format("helpList.swparty.header" + 1));
                }
                s.sendMessage(new Messaging.MessageFormatter().format("helpList.swparty." + cmd.cmdName));
            }
        }
    }

    public BaseCmd getCommand(String s) {
        return getCmd(partycmds, s);
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
        partycmds.add(commandIn);
    }

    @Override
    public void unregisterCommand(BaseCmd commandIn) {
        if (commandIn == null) return;
        partycmds.remove(commandIn);
    }

    @Override
    public BaseCmd getSubCommand(String name) {
        return null;
    }
}
