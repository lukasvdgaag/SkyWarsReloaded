package net.gcnt.skywarsreloaded.bukkit.wrapper.sender;

import net.gcnt.skywarsreloaded.wrapper.sender.AbstractSWCommandSender;
import org.bukkit.command.ConsoleCommandSender;

public class BukkitSWCommandSender extends AbstractSWCommandSender {

    private ConsoleCommandSender sender;

    public BukkitSWCommandSender(ConsoleCommandSender senderIn) {
        this.sender = senderIn;
    }

    @Override
    public void sendMessage(String message) {
        sender.sendMessage(message);
    }

    @Override
    public boolean hasPermission(String permission) {
        return sender.hasPermission(permission);
    }
}
