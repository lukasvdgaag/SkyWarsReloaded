package net.gcnt.skywarsreloaded.bukkit.command;

import net.gcnt.skywarsreloaded.SkyWarsReloaded;
import net.gcnt.skywarsreloaded.command.SWCommand;
import net.gcnt.skywarsreloaded.command.SWCommandManager;
import net.gcnt.skywarsreloaded.wrapper.SWCommandSender;
import net.gcnt.skywarsreloaded.wrapper.SWPlayer;
import org.bukkit.command.*;
import org.bukkit.entity.Player;

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
        else return true;

        this.main.getCommandManager().runCommand(swSender, command.getName(), args.length > 0 ? args[0] : "", args);
        return true;
    }

    public List<String> onTabComplete(CommandSender sender, Command command, String s, String[] args) {
        // todo
        return null;
    }

}
