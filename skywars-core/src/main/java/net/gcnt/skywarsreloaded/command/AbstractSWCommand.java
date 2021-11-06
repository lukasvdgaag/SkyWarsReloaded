package net.gcnt.skywarsreloaded.command;

import net.gcnt.skywarsreloaded.wrapper.SWCommandSender;
import net.gcnt.skywarsreloaded.wrapper.SWPlayer;

public abstract class AbstractSWCommand implements SWCommand {

    // Parent info
    public String parentCommand = "skywars";

    // Definition
    public String command;
    public String permission = "skywars.command";

    // Attributes
    public boolean forcePlayer = false;
    public String subUsage = "";
    public String usage = "";

    public AbstractSWCommand(String parentCommandIn, String commandIn, String permissionIn, Boolean forcePlayerIn, String subUsageIn, String usageIn) {
        if (parentCommandIn != null) parentCommand = parentCommandIn;
        this.command = commandIn;
        if (permissionIn != null) permission = permissionIn;
        if (forcePlayerIn != null) forcePlayer = forcePlayerIn;
        if (subUsageIn != null) subUsage = subUsageIn;
        if (usageIn != null) usage = usageIn;
    }

    public boolean processCommand(SWCommandSender sender, String[] args) {
        SWPlayer player = null;

        if (sender instanceof SWPlayer) player = (SWPlayer) sender;
        if (forcePlayer)
            if (player == null) {
                sender.sendMessage("§cYou must be a player to perform this command.");
                return true;
            }

        if (!sender.hasPermission(permission))
            sender.sendMessage("§cYou don't have permission to do this."); // todo make configurable.
        else return run(sender, args);
        return true;
    }

    public abstract boolean run(SWCommandSender sender, String[] args);

    public String sendUsage(SWCommandSender sender, boolean send) {
        String k = usage.equals("") ? "&7" : " &7";
        // TODO create chat color util adapter for different platforms
        String s = /*(ChatColor.translateAlternateColorCodes('&', */"&b/" + parentCommand + " " + command + " " + usage + k + subUsage/*))*/;
        if (send) {
            s = "§fUsage: " + s;
            sender.sendMessage(s);
        }
        return s;
    }

}
