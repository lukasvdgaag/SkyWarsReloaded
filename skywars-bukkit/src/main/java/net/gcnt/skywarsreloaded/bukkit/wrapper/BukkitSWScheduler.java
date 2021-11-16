package net.gcnt.skywarsreloaded.bukkit.wrapper;

import net.gcnt.skywarsreloaded.SkyWarsReloaded;
import net.gcnt.skywarsreloaded.bukkit.BukkitSkyWarsReloaded;
import net.gcnt.skywarsreloaded.wrapper.AbstractSWScheduler;
import net.gcnt.skywarsreloaded.wrapper.SWRunnable;
import org.bukkit.plugin.java.JavaPlugin;

public class BukkitSWScheduler extends AbstractSWScheduler {

    private final JavaPlugin bukkitPlugin;

    public BukkitSWScheduler(final SkyWarsReloaded plugin) {
        super(plugin);
        this.bukkitPlugin = ((BukkitSkyWarsReloaded) this.plugin).getPlugin();
    }

    @Override
    public void runSync(SWRunnable runnable) {
        bukkitPlugin.getServer().getScheduler().runTask(bukkitPlugin, runnable);
    }

    @Override
    public void runAsync(SWRunnable runnable) {
        bukkitPlugin.getServer().getScheduler().runTaskAsynchronously(bukkitPlugin, runnable);
    }

    @Override
    public void runSyncLater(SWRunnable runnable, int ticks) {
        bukkitPlugin.getServer().getScheduler().runTaskLater(bukkitPlugin, runnable, ticks);
    }

    @Override
    public void runAsyncLater(SWRunnable runnable, int ticks) {
        bukkitPlugin.getServer().getScheduler().runTaskLaterAsynchronously(bukkitPlugin, runnable, ticks);
    }

    @Override
    public void runSyncTimer(SWRunnable runnable, int ticks, int period) {
        bukkitPlugin.getServer().getScheduler().runTaskTimer(bukkitPlugin, runnable, ticks, period);
    }

    @Override
    public void runAsyncTimer(SWRunnable runnable, int ticks, int period) {
        bukkitPlugin.getServer().getScheduler().runTaskTimerAsynchronously(bukkitPlugin, runnable, ticks, period);
    }
}
