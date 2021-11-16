package net.gcnt.skywarsreloaded.wrapper.sender;

public interface SWCommandSender {

    void sendMessage(String message);

    boolean hasPermission(String permission);

}
