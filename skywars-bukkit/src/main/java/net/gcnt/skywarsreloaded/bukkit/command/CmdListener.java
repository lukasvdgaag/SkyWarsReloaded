package net.gcnt.skywarsreloaded.bukkit.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import java.util.ArrayList;
import java.util.List;

public abstract class CmdListener implements CommandExecutor, TabCompleter {

    private final List<Cmd> commands = new ArrayList<>();

    public List<Cmd> getCommands() {
        return this.commands;
    }

    public void addCommand(Cmd cmd) {
        this.commands.add(cmd);
    }

    public abstract boolean onCommand(CommandSender sender, Command command, String s, String[] args);

    public abstract List<String> onTabComplete(CommandSender sender, Command command, String s, String[] args);

}
