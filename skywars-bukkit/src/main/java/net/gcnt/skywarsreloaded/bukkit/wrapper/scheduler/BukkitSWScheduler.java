package net.gcnt.skywarsreloaded.bukkit.wrapper.scheduler;

import net.gcnt.skywarsreloaded.SkyWarsReloaded;
import net.gcnt.skywarsreloaded.bukkit.BukkitSkyWarsReloaded;
import net.gcnt.skywarsreloaded.wrapper.scheduler.AbstractSWScheduler;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.concurrent.CompletableFuture;
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
    public void runSyncTimer(Runnable runnable, int ticks, int period) {
        bukkitPlugin.getServer().getScheduler().runTaskTimer(bukkitPlugin, runnable, ticks, period);
    }

    @Override
    public void runAsyncTimer(Runnable runnable, int ticks, int period) {
        bukkitPlugin.getServer().getScheduler().runTaskTimerAsynchronously(bukkitPlugin, runnable, ticks, period);
    }
}
