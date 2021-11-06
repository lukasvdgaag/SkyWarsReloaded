package net.gcnt.skywarsreloaded.command;

import net.gcnt.skywarsreloaded.SkyWarsReloaded;
import net.gcnt.skywarsreloaded.command.general.MainCmd;
import net.gcnt.skywarsreloaded.wrapper.SWCommandSender;

import java.util.*;
import java.util.stream.Collectors;

public class CoreSWCommandManager implements SWCommandManager {

    private final SkyWarsReloaded main;

    private final HashMap<Class<? extends SWCommand>, SWCommand> commands;

    public CoreSWCommandManager(SkyWarsReloaded mainIn) {
        this.main = mainIn;
        this.commands = new HashMap<>();
    }

    public void registerBaseCommands() {
        this.registerCommand(new MainCmd(this.main));
    }

    @Override
    public List<SWCommand> getBaseCommands() {
        return this.commands.values().stream().filter(swCommand -> swCommand.getParentCommand().equals("skywars")).collect(Collectors.toList());
    }

    public Collection<SWCommand> getCommands() {
        return this.commands.values();
    }

    public void registerCommand(SWCommand cmd) {
        this.commands.put(cmd.getClass(), cmd);
    }

    public <T extends SWCommand> T getCommand(Class<T> clazz) {
        return (T) commands.get(clazz);
    }

    public void runCommand(SWCommandSender sender, String command, String subCommand, String[] args) {
        for (SWCommand cmd : commands.values()) {
            if (cmd.getParentCommand().equalsIgnoreCase(command) && cmd.getName().equalsIgnoreCase(subCommand)) {
                cmd.processCommand(sender, Arrays.copyOfRange(args, 1, args.length));
            }
        }
    }
}
