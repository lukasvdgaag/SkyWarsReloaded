package net.gcnt.skywarsreloaded.bukkit.wrapper;

import net.gcnt.skywarsreloaded.wrapper.AbstractSWConsoleSender;
import org.bukkit.command.ConsoleCommandSender;

public class BukkitSWConsoleSender extends AbstractSWConsoleSender {

    private final ConsoleCommandSender consoleSender;

    public BukkitSWConsoleSender(ConsoleCommandSender consoleSenderIn) {
        this.consoleSender = consoleSenderIn;
    }

    @Override
    public void sendMessage(String message) {
        consoleSender.sendMessage(message);
    }

    @Override
    public boolean hasPermission(String permission) {
        return true;
    }
}
