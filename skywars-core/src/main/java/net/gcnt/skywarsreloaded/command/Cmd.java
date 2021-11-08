package net.gcnt.skywarsreloaded.command;

import net.gcnt.skywarsreloaded.SkyWarsReloaded;
import net.gcnt.skywarsreloaded.wrapper.SWCommandSender;
import net.gcnt.skywarsreloaded.wrapper.SWPlayer;

/**
 * Extend this in all command handling classes. <3
 */
public abstract class Cmd implements SWCommand {

    // Parent info
    public final SkyWarsReloaded plugin;
    public String parentCommand = "skywars";

    // Definition
    public String command;
    public String permission = "skywars.command";

    // Attributes
    public boolean forcePlayer = false;
    public String subUsage = "";
    public String usage = "";

    public Cmd(SkyWarsReloaded plugin, String parentCommandIn, String commandIn, String permissionIn, Boolean forcePlayerIn, String subUsageIn, String usageIn) {
        this.plugin = plugin;
        this.command = commandIn;
        if (parentCommandIn != null) parentCommand = parentCommandIn;
        if (permissionIn != null) permission = permissionIn;
        if (forcePlayerIn != null) forcePlayer = forcePlayerIn;
        if (subUsageIn != null) subUsage = subUsageIn;
        if (usageIn != null) usage = usageIn;
    }

    @Override
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

    @Override
    public String sendUsage(SWCommandSender sender, boolean send) {
        String k = subUsage.isEmpty() ? "&7" : " &7" + subUsage;
        String c = command.isEmpty() ? "" : " " + command;
        String s = plugin.getUtils().colorize("&b/" + parentCommand + c + k + " &e" + usage);
        if (send) {
            s = "§fUsage: " + s;
            sender.sendMessage(s);
        }
        return s;
    }

    @Override
    public String getParentCommand() {
        return parentCommand;
    }

    @Override
    public String getName() {
        return command;
    }

    @Override
    public String getPermission() {
        return permission;
    }
}
