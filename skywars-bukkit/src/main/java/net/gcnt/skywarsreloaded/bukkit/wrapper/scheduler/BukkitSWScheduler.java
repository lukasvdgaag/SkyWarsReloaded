package net.gcnt.skywarsreloaded.bukkit.wrapper.scheduler;

import net.gcnt.skywarsreloaded.SkyWarsReloaded;
import net.gcnt.skywarsreloaded.bukkit.BukkitSkyWarsReloaded;
import net.gcnt.skywarsreloaded.wrapper.scheduler.AbstractSWScheduler;
import net.gcnt.skywarsreloaded.wrapper.scheduler.CoreSWRunnable;
import net.gcnt.skywarsreloaded.wrapper.scheduler.SWRunnable;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;

import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class BukkitSWScheduler extends AbstractSWScheduler {

    private final JavaPlugin bukkitPlugin;

    public BukkitSWScheduler(final SkyWarsReloaded plugin) {
        super(plugin);
        this.bukkitPlugin = ((BukkitSkyWarsReloaded) this.plugin).getBukkitPlugin();
    }

    @Override
    public <T> CompletableFuture<T> callSyncMethod(Supplier<T> supplier) {
        CompletableFuture<T> future = new CompletableFuture<>();
        bukkitPlugin.getServer().getScheduler().runTask(bukkitPlugin, () -> {
            future.complete(supplier.get());
        });
        return future;
    }

    @Override
    public void runSync(Runnable runnable) {
        bukkitPlugin.getServer().getScheduler().runTask(bukkitPlugin, runnable);
    }

    @Override
    public void runAsync(Runnable runnable) {
        bukkitPlugin.getServer().getScheduler().runTaskAsynchronously(bukkitPlugin, runnable);
    }

    @Override
    public void runSyncLater(Runnable runnable, int ticks) {
        bukkitPlugin.getServer().getScheduler().runTaskLater(bukkitPlugin, runnable, ticks);
    }

    @Override
    public void runAsyncLater(Runnable runnable, int ticks) {
        bukkitPlugin.getServer().getScheduler().runTaskLaterAsynchronously(bukkitPlugin, runnable, ticks);
    }

    @Override
    public SWRunnable runSyncTimer(SWRunnable runnable, int ticks, int period) {
        BukkitTask task = bukkitPlugin.getServer().getScheduler().runTaskTimer(bukkitPlugin, runnable, ticks, period);
        runnable.setTaskId(task.getTaskId());
        return runnable;
    }

    @Override
    public SWRunnable runAsyncTimer(SWRunnable runnable, int ticks, int period) {
        BukkitTask task = bukkitPlugin.getServer().getScheduler().runTaskTimerAsynchronously(bukkitPlugin, runnable, ticks, period);
        runnable.setTaskId(task.getTaskId());
        return runnable;
    }

    @Override
    public SWRunnable createRunnable(Runnable runnable) {
        return new BukkitSWRunnable() {
            @Override
            public void run() {
                runnable.run();
            }
        };
    }
}
