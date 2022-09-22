package net.gcnt.skywarsreloaded.manager;

import net.gcnt.skywarsreloaded.AbstractSkyWarsReloaded;
import net.gcnt.skywarsreloaded.hook.SWHook;

import java.util.ArrayList;

public class CoreSWHookManager implements SWHookManager {

    private ArrayList<SWHook> hooks;

    public CoreSWHookManager(AbstractSkyWarsReloaded abstractSkyWarsReloaded) {
        this.hooks = new ArrayList<>();
    }

    @Override
    public void registerHook(SWHook hook) {
        this.hooks.add(hook);
    }

    @Override
    public void unregisterHook(SWHook hook) {
        this.hooks.remove(hook);
    }

    @Override
    public void enableAllHooks() {
        for (SWHook hook : this.hooks) {
            hook.enable();
        }
    }

    @Override
    public void disableAllHooks() {
        for (SWHook hook : this.hooks) {
            hook.disable();
        }
    }

    @Override
    public void unregisterAllHooks() {
        this.hooks.clear();
    }
}
