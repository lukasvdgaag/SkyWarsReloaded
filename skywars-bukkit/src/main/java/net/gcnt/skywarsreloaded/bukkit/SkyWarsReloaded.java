package net.gcnt.skywarsreloaded.bukkit;

import org.bukkit.plugin.java.JavaPlugin;

public class SkyWarsReloaded extends JavaPlugin {

    private static SkyWarsReloaded inst;

    public static SkyWarsReloaded get() {
        return inst;
    }


    @Override
    public void onEnable() {
        // test push
        inst = this;
    }
}
