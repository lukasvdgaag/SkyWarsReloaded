package net.gcnt.skywarsreloaded.command;

import net.gcnt.skywarsreloaded.wrapper.sender.SWCommandSender;

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

    SWCommand matchCommand(String name, String subCommand);

    void runCommand(SWCommandSender sender, String name, String subCommand, String[] args);

    List<String> runTabCompletion(SWCommandSender sender, String name, String subCommand, String[] args);

}
