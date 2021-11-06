package net.gcnt.skywarsreloaded.wrapper;

public interface SWCommandSender {

    void sendMessage(String message);

    boolean hasPermission(String permission);

}
