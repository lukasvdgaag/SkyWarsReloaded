package com.walrusone.skywarsreloaded.commands;

import com.walrusone.skywarsreloaded.SkyWarsReloaded;
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
    private final List<BaseCmd> commands = new ArrayList<>();

    public KitCmdManager(SkyWarsReloaded plugin) {
        commands.add(new CreateCmd(plugin, "kit"));
        commands.add(new EnableCmd(plugin, "kit"));
        commands.add(new IconCmd(plugin, "kit"));
        commands.add(new LockedIconCmd(plugin, "kit"));
        commands.add(new LoadCmd(plugin, "kit"));
        commands.add(new LoreCmd(plugin, "kit"));
        commands.add(new NameCmd(plugin, "kit"));
        commands.add(new PositionCmd(plugin, "kit"));
        commands.add(new PermCmd(plugin, "kit"));
        commands.add(new UpdateCmd(plugin, "kit"));
        commands.add(new ListCmd(plugin, "kit"));
    }

    public List<BaseCmd> getCommands() {
        return commands;
    }

    public boolean onCommand(CommandSender s, Command command, String label, String[] args) {
        if (args.length == 0 || getCommand(args[0]) == null) {
            s.sendMessage(new Messaging.MessageFormatter().format("helpList.header"));
            sendHelp(commands, s);
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
                    s.sendMessage(new Messaging.MessageFormatter().format("helpList.swkit.header" + 1));
                }
                s.sendMessage(new Messaging.MessageFormatter().format("helpList.swkit." + cmd.cmdName));
            }
        }
    }

    public BaseCmd getCommand(String s) {
        return getCmd(commands, s);
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
        commands.add(commandIn);
    }

    @Override
    public void unregisterCommand(BaseCmd commandIn) {
        if (commandIn == null) return;
        commands.remove(commandIn);
    }

    @Override
    public BaseCmd getSubCommand(String name) {
        return null;
    }
}
