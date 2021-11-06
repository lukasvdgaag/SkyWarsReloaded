package net.gcnt.skywarsreloaded.command;

import net.gcnt.skywarsreloaded.SkyWarsReloaded;
import net.gcnt.skywarsreloaded.command.general.MainCmd;
import net.gcnt.skywarsreloaded.wrapper.SWCommandSender;

import java.util.*;
import java.util.stream.Collectors;

public class CoreSWCommandManager implements SWCommandManager {

    private final HashMap<Class<? extends SWCommand>, SWCommand> commands;

    public CoreSWCommandManager() {
        this.commands = new HashMap<>();
    }

    public void registerBaseCommands() {
        // todo: add base commands to register here
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

}
