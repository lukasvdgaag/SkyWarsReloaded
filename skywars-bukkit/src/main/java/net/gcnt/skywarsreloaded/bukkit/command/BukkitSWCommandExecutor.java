package net.gcnt.skywarsreloaded.bukkit.command;

import net.gcnt.skywarsreloaded.SkyWarsReloaded;
import net.gcnt.skywarsreloaded.command.SWCommandManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import java.util.ArrayList;
import java.util.List;

public class BukkitSWCommandExecutor implements CommandExecutor, TabCompleter {

    private SkyWarsReloaded main;
    private SWCommandManager cmdManager;

    public BukkitSWCommandExecutor(SkyWarsReloaded mainIn) {
        this.main = mainIn;
        this.cmdManager = main.getCommandManager();
    }

    public boolean onCommand(CommandSender sender, Command command, String name, String[] args) {
        // todo
        return true;
    }

    public List<String> onTabComplete(CommandSender sender, Command command, String s, String[] args) {
        // todo
        return null;
    }

}
