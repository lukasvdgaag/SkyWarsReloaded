package net.gcnt.skywarsreloaded.bukkit.command;

import net.gcnt.skywarsreloaded.SkyWarsReloaded;
import net.gcnt.skywarsreloaded.command.SWCommand;
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
        SWCommandSender swSender;

        if (sender instanceof Player player)
            swSender = main.getPlayerManager().getPlayerByUUID(player.getUniqueId());
        else if (sender instanceof ConsoleCommandSender)
            swSender = main.getConsoleSender();

        this.main.getCommandManager().runCommand(swSender, command.getName(), args.length > 0 ? args[0] : "", args);
        return true;
    }

    public List<String> onTabComplete(CommandSender sender, Command command, String s, String[] args) {
        // todo
        return null;
    }

}
