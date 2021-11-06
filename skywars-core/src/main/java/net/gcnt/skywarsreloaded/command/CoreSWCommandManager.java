package net.gcnt.skywarsreloaded.command;

import java.util.Collection;
import java.util.HashMap;

public class CoreSWCommandManager implements SWCommandManager {

    private final HashMap<Class<? extends SWCommand>, SWCommand> commands;

    public CoreSWCommandManager() {
        this.commands = new HashMap<>();
    }

    public void registerBaseCommands() {
        // todo: add base commands to register here
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
