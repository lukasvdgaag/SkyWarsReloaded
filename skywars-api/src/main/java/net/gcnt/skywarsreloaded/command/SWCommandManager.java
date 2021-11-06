package net.gcnt.skywarsreloaded.command;

import java.util.Collection;

public interface SWCommandManager {

    void registerBaseCommands();

    Collection<SWCommand> getCommands();

    void registerCommand(SWCommand cmd);

    <T extends SWCommand> T getCommand(Class<T> clazz);

}
