package com.walrusone.skywarsreloaded.commands;


import com.walrusone.skywarsreloaded.api.command.SWRCmdManagerAPI;
import com.walrusone.skywarsreloaded.commands.maps.*;
import com.walrusone.skywarsreloaded.utilities.Messaging;
import com.walrusone.skywarsreloaded.utilities.Util;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.List;

public class MapCmdManager implements CommandExecutor, SWRCmdManagerAPI {
    private List<BaseCmd> mapcmds = new ArrayList<>();
    private static MapCmdManager mcm;

    //Add New Commands Here
    public MapCmdManager() {
        mcm = this;
        mapcmds.add(new ListCmd("map"));
        mapcmds.add(new CreateCmd("map"));
        mapcmds.add(new EditCmd("map"));
        mapcmds.add(new RegisterCmd("map"));
        mapcmds.add(new SaveCmd("map"));
        mapcmds.add(new UnregisterCmd("map"));
        mapcmds.add(new RefreshData("map"));
        mapcmds.add(new TeamSizeCmd("map")); // new
        mapcmds.add(new NameCmd("map"));
        mapcmds.add(new DeleteCmd("map"));
        mapcmds.add(new MinimumCmd("map"));
        mapcmds.add(new CreatorCmd("map"));
        mapcmds.add(new DebugCmd("map")); // new
        mapcmds.add(new ArenaCmd("map"));
        mapcmds.add(new AddSpawnCmd("map"));
        mapcmds.add(new ChestTypeCmd("map"));
        mapcmds.add(new CheckChestTypeCmd("map"));
        mapcmds.add(new LegacyLoadCmd("map"));
    }

    public static List<BaseCmd> getCommands() { return mcm.mapcmds; }

    public boolean onCommand(CommandSender s, Command command, String label, String[] args) {
        if (args.length == 0 || getCommand(args[0]) == null) {
            s.sendMessage(new Messaging.MessageFormatter().format("helpList.header"));
            sendHelp(mapcmds, s);
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
                    s.sendMessage(new Messaging.MessageFormatter().format("helpList.swmap.header" + 1));
                }
                s.sendMessage(new Messaging.MessageFormatter().format("helpList.swmap." + cmd.cmdName));
            }
        }
    }

    public BaseCmd getCommand(String s) {
        return getCmd(mapcmds, s);
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
        mapcmds.add(commandIn);
    }

    @Override
    public void unregisterCommand(BaseCmd commandIn) {
        if (commandIn == null) return;
        mapcmds.remove(commandIn);
    }

    @Override
    public BaseCmd getSubCommand(String name) {
        return null;
    }
}
