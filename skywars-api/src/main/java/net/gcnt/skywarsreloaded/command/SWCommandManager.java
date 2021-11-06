package net.gcnt.skywarsreloaded.command;

import net.gcnt.skywarsreloaded.wrapper.SWCommandSender;

import java.util.Collection;
import java.util.List;

public interface SWCommandManager {

    void registerBaseCommands();

    List<SWCommand> getBaseCommands();

    Collection<SWCommand> getCommands();

    void registerCommand(SWCommand cmd);

    <T extends SWCommand> T getCommand(Class<T> clazz);

}
