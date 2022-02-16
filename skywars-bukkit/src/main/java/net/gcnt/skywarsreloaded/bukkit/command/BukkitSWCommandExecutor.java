package net.gcnt.skywarsreloaded.bukkit.command;

import net.gcnt.skywarsreloaded.SkyWarsReloaded;
import net.gcnt.skywarsreloaded.wrapper.sender.SWCommandSender;
import org.bukkit.command.*;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class BukkitSWCommandExecutor implements CommandExecutor, TabCompleter {

    private SkyWarsReloaded main;

    public BukkitSWCommandExecutor(SkyWarsReloaded mainIn) {
        this.main = mainIn;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String name, String[] args) {
        SWCommandSender swSender;

        if (sender instanceof Player)
            swSender = main.getPlayerManager().getPlayerByUUID(((Player) sender).getUniqueId());
        else if (sender instanceof ConsoleCommandSender)
            swSender = main.getConsoleSender();
        else return true;

        this.main.getCommandManager().runCommand(swSender, command.getName(), args.length > 0 ? args[0] : "", args);
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String s, String[] args) {
        SWCommandSender swSender;

        if (sender instanceof Player)
            // creating the player if not existing.
            swSender = main.getPlayerManager().getPlayerByUUID(((Player) sender).getUniqueId());
        else if (sender instanceof ConsoleCommandSender)
            swSender = main.getConsoleSender();
        else return new ArrayList<>();

        return this.main.getCommandManager().runTabCompletion(swSender, command.getName(), args.length > 0 ? args[0] : "", args);
    }

}
