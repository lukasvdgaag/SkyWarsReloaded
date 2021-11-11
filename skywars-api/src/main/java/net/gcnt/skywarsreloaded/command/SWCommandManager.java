package net.gcnt.skywarsreloaded.command;

import net.gcnt.skywarsreloaded.wrapper.SWCommandSender;

import java.util.Collection;
import java.util.List;

public interface SWCommandManager {

    void registerBaseCommands();

    void registerMapCommands();

    void registerKitCommands();

    List<SWCommand> getCommands(String baseCmd);

    Collection<SWCommand> getCommands();

    void registerCommand(SWCommand cmd);

    <T extends SWCommand> T getCommand(Class<T> clazz);

    void runCommand(SWCommandSender sender, String name, String subCommand, String[] args);
}
