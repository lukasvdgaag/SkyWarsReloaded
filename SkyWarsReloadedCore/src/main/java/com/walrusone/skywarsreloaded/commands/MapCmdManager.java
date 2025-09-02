package com.walrusone.skywarsreloaded.commands;


import com.walrusone.skywarsreloaded.SkyWarsReloaded;
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
    private final List<BaseCmd> mapCommands = new ArrayList<>();

    //Add New Commands Here
    public MapCmdManager(SkyWarsReloaded plugin) {
        mapCommands.add(new ListCmd(plugin, "map"));
        mapCommands.add(new CreateCmd(plugin, "map"));
        mapCommands.add(new EditCmd(plugin, "map"));
        mapCommands.add(new RegisterCmd(plugin, "map"));
        mapCommands.add(new SaveCmd(plugin, "map"));
        mapCommands.add(new UnregisterCmd(plugin, "map"));
        mapCommands.add(new RefreshDataCmd(plugin, "map"));
        mapCommands.add(new TeamSizeCmd(plugin, "map")); // new
        mapCommands.add(new NameCmd(plugin, "map"));
        mapCommands.add(new DeleteCmd(plugin, "map"));
        mapCommands.add(new MinimumCmd(plugin, "map"));
        mapCommands.add(new CreatorCmd(plugin, "map"));
        mapCommands.add(new DebugCmd(plugin, "map")); // new
        mapCommands.add(new ArenaCmd(plugin, "map"));
        mapCommands.add(new AddSpawnCmd(plugin, "map"));
        mapCommands.add(new ChestTypeCmd(plugin, "map"));
        mapCommands.add(new CheckChestTypeCmd(plugin, "map"));
        mapCommands.add(new LegacyLoadCmd(plugin, "map"));
    }

    public List<BaseCmd> getCommands() {
        return mapCommands;
    }

    public boolean onCommand(CommandSender s, Command command, String label, String[] args) {
        if (args.length == 0 || getCommand(args[0]) == null) {
            s.sendMessage(new Messaging.MessageFormatter().format("helpList.header"));
            sendHelp(mapCommands, s);
            s.sendMessage(new Messaging.MessageFormatter().format("helpList.footer"));
        } else getCommand(args[0]).processCmd(s, args);
        return true;
    }

    private void sendHelp(List<BaseCmd> cmds, CommandSender s) {
        int count = 0;
        for (BaseCmd cmd : cmds) {
            if (Util.get().hasPerm(cmd.getType(), s, cmd.cmdName)) {
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
        return getCmd(mapCommands, s);
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
        mapCommands.add(commandIn);
    }

    @Override
    public void unregisterCommand(BaseCmd commandIn) {
        if (commandIn == null) return;
        mapCommands.remove(commandIn);
    }

    @Override
    public BaseCmd getSubCommand(String name) {
        return null;
    }
}
