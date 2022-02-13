package net.gcnt.skywarsreloaded.wrapper.sender;

public abstract class AbstractSWConsoleSender implements SWConsoleSender {

    @Override
    public String getName() {
        return "CONSOLE";
    }

    @Override
    public boolean hasPermission(String permission) {
        return true;
    }

}
