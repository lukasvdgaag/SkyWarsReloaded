package com.walrusone.skywarsreloaded.commands;

import com.walrusone.skywarsreloaded.SkyWarsReloaded;
import com.walrusone.skywarsreloaded.api.command.SWRCmdManagerAPI;
import com.walrusone.skywarsreloaded.commands.admin.*;
import com.walrusone.skywarsreloaded.commands.player.*;
import com.walrusone.skywarsreloaded.utilities.Messaging;
import com.walrusone.skywarsreloaded.utilities.Util;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.List;

public class MainCmdManager implements CommandExecutor, SWRCmdManagerAPI {
    private final List<BaseCmd> adminCommands = new ArrayList<>();
    private final List<BaseCmd> playerCommands = new ArrayList<>();

    public MainCmdManager(SkyWarsReloaded plugin) {
        adminCommands.add(new ReloadCmd(plugin, "sw"));
        adminCommands.add(new ChestAddCmd(plugin, "sw"));
        adminCommands.add(new ChestEditCmd(plugin, "sw"));
        adminCommands.add(new SetStatsCmd(plugin, "sw"));
        adminCommands.add(new ClearStatsCmd(plugin, "sw"));
        adminCommands.add(new SetSpawnCmd(plugin, "sw"));
        adminCommands.add(new StartCmd(plugin, "sw"));
        adminCommands.add(new UpdateTopCmd(plugin, "sw"));
        adminCommands.add(new HoloAddCmd(plugin, "sw"));
        adminCommands.add(new HoloRemoveCmd(plugin, "sw"));

        playerCommands.add(new SWJoinCmd(plugin, "sw"));
        playerCommands.add(new SWQuitCmd(plugin, "sw"));
        playerCommands.add(new SWStatsCmd(plugin, "sw"));
        playerCommands.add(new SWTopCmd(plugin, "sw"));
        playerCommands.add(new SWOptionsCmd(plugin, "sw"));
        playerCommands.add(new SWSpectateCmd(plugin, "sw"));
        playerCommands.add(new SWLobbyTeleportCmd(plugin, "sw"));

        if (SkyWarsReloaded.getCfg().winsoundMenuEnabled()) {
            playerCommands.add(new SWWinsoundCmd(plugin, "sw"));
        }
        if (SkyWarsReloaded.getCfg().killsoundMenuEnabled()) {
            playerCommands.add(new SWKillsoundCmd(plugin, "sw"));
        }
        if (SkyWarsReloaded.getCfg().tauntsMenuEnabled()) {
            playerCommands.add(new SWTauntCmd(plugin, "sw"));
        }
        if (SkyWarsReloaded.getCfg().projectileMenuEnabled()) {
            playerCommands.add(new SWProjectileCmd(plugin, "sw"));
        }
        if (SkyWarsReloaded.getCfg().particleMenuEnabled()) {
            playerCommands.add(new SWParticleCmd(plugin, "sw"));
        }
        if (SkyWarsReloaded.getCfg().glassMenuEnabled()) {
            playerCommands.add(new SWGlassCmd(plugin, "sw"));
        }
    }

    public List<BaseCmd> getCommands() {
        List<BaseCmd> a = adminCommands;
        a.addAll(playerCommands);
        return a;
    }

    public boolean onCommand(CommandSender s, Command command, String label, String[] args) {
        if (args.length == 0 || getCommand(args[0]) == null) {
            s.sendMessage(new Messaging.MessageFormatter().format("helpList.header"));
            sendHelp(adminCommands, s, "1");
            sendHelp(playerCommands, s, "2");
            s.sendMessage(new Messaging.MessageFormatter().format("helpList.footer"));
        } else getCommand(args[0]).processCmd(s, args);
        return true;
    }

    private void sendHelp(List<BaseCmd> cmds, CommandSender s, String num) {
        int count = 0;
        for (BaseCmd cmd : cmds) {
            if (Util.get().hasPerm(cmd.getType(), s, cmd.cmdName)) {
                count++;
                if (count == 1) {
                    s.sendMessage(" ");
                    s.sendMessage(new Messaging.MessageFormatter().format("helpList.sw.header" + num));
                }
                s.sendMessage(new Messaging.MessageFormatter().format("helpList.sw." + cmd.cmdName));
            }
        }
    }

    public BaseCmd getCommand(String s) {
        BaseCmd cmd;
        cmd = getCmd(adminCommands, s);
        if (cmd == null) cmd = getCmd(playerCommands, s);
        return cmd;
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
        registerCommand(commandIn, 0);
    }

    public void registerCommand(BaseCmd commandIn, int type) {
        if (commandIn == null || type < 0 || type > 1) return;
        switch (type) {
            case 0:
                playerCommands.add(commandIn);
                break;
            case 1:
                adminCommands.add(commandIn);
                break;
        }
    }

    @Override
    public void unregisterCommand(BaseCmd commandIn) {
        unregisterCommand(commandIn, 0);
    }

    @Override
    public BaseCmd getSubCommand(String name) {
        return null;
    }

    public void unregisterCommand(BaseCmd commandIn, int type) {
        if (commandIn == null || type < 0 || type > 1) return;
        switch (type) {
            case 0:
                playerCommands.remove(commandIn);
                break;
            case 1:
                adminCommands.remove(commandIn);
                break;
        }
    }
}
