package net.gcnt.skywarsreloaded.command;

import net.gcnt.skywarsreloaded.wrapper.SWCommandSender;

import java.util.List;

public interface SWCommand {

    String sendUsage(SWCommandSender sender, boolean send);

    boolean processCommand(SWCommandSender sender, String[] args);

    List<String> processTabCompletion(SWCommandSender sender, String[] args);

    String getParentCommand();

    String getPermission();

    String getName();

}
