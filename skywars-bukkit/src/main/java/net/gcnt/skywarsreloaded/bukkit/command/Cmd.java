package net.gcnt.skywarsreloaded.bukkit.command;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@Deprecated
public abstract class Cmd {

    public CommandSender sender;
    public String[] args;
    public String command;
    public String mainCommand = "skywars";
    public boolean forcePlayer = false;
    public String permission = "skywars.command";
    public String subUsage = "";
    public String usage = "";
    public Player player;

    public boolean processCommand(CommandSender sender, String[] args) {
        this.sender = sender;
        this.args = args;

        if (sender instanceof Player) player = (Player) sender;
        if (forcePlayer)
            if (!(sender instanceof Player)) {
                sender.sendMessage("§cYou must be a player to perform this command.");
                return true;
            }

        if (!sender.hasPermission(permission))
            sender.sendMessage("§cYou don't have permission to do this."); // todo make configurable.
        else return run();
        return true;
    }

    public abstract boolean run();

    public String sendUsage(boolean send) {
        String k = usage.equals("") ? "&7" : " &7";
        String s = (ChatColor.translateAlternateColorCodes('&', "&b/" + mainCommand + " " + command + " " + usage + k + subUsage));
        if (send) {
            s = "§fUsage: " + s;
            sender.sendMessage(s);
        }
        return s;
    }

}
