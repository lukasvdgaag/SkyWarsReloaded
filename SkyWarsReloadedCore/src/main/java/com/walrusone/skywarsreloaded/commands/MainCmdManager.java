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
    private List<BaseCmd> admincmds = new ArrayList<>();
    private List<BaseCmd> pcmds = new ArrayList<>();
    private static MainCmdManager cm;

    public MainCmdManager() {
        cm = this;
        admincmds.add(new ReloadCmd("sw"));
        admincmds.add(new ChestAddCmd("sw"));
        admincmds.add(new ChestEditCmd("sw"));
        admincmds.add(new SetStatsCmd("sw"));
        admincmds.add(new ClearStatsCmd("sw"));
        admincmds.add(new SetSpawnCmd("sw"));
        admincmds.add(new StartCmd("sw"));
        admincmds.add(new UpdateTopCmd("sw"));
        admincmds.add(new HoloAddCmd("sw"));
        admincmds.add(new HoloRemoveCmd("sw"));

        pcmds.add(new SWJoinCmd("sw"));
        pcmds.add(new SWQuitCmd("sw"));
        pcmds.add(new SWStatsCmd("sw"));
        pcmds.add(new SWTopCmd("sw"));
        pcmds.add(new SWOptionsCmd("sw"));
        pcmds.add(new SWSpectateCmd("sw"));
        pcmds.add(new SWLobbyTeleportCmd("sw"));

        if (SkyWarsReloaded.getCfg().winsoundMenuEnabled()) {
            pcmds.add(new SWWinsoundCmd("sw"));
        }
        if (SkyWarsReloaded.getCfg().killsoundMenuEnabled()) {
            pcmds.add(new SWKillsoundCmd("sw"));
        }
        if (SkyWarsReloaded.getCfg().tauntsMenuEnabled()) {
            pcmds.add(new SWTauntCmd("sw"));
        }
        if (SkyWarsReloaded.getCfg().projectileMenuEnabled()) {
            pcmds.add(new SWProjectileCmd("sw"));
        }
        if (SkyWarsReloaded.getCfg().particleMenuEnabled()) {
            pcmds.add(new SWParticleCmd("sw"));
        }
        if (SkyWarsReloaded.getCfg().glassMenuEnabled()) {
            pcmds.add(new SWGlassCmd("sw"));
        }
    }

    public static List<BaseCmd> getCommands() {
        List<BaseCmd> a = cm.admincmds;
        a.addAll(cm.pcmds);
        return a;
    }

    public boolean onCommand(CommandSender s, Command command, String label, String[] args) {
        if (args.length == 0 || getCommand(args[0]) == null) {
            s.sendMessage(new Messaging.MessageFormatter().format("helpList.header"));
            sendHelp(admincmds, s, "1");
            sendHelp(pcmds, s, "2");
            s.sendMessage(new Messaging.MessageFormatter().format("helpList.footer"));
        } else getCommand(args[0]).processCmd(s, args);
        return true;
    }

    private void sendHelp(List<BaseCmd> cmds, CommandSender s, String num) {
        int count = 0;
        for (BaseCmd cmd : cmds) {
            if (Util.get().hp(cmd.getType(), s, cmd.cmdName)) {
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
        cmd = getCmd(admincmds, s);
        if (cmd == null) cmd = getCmd(pcmds, s);
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
                pcmds.add(commandIn);
                break;
            case 1:
                admincmds.add(commandIn);
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
                pcmds.remove(commandIn);
                break;
            case 1:
                admincmds.remove(commandIn);
                break;
        }
    }
}
