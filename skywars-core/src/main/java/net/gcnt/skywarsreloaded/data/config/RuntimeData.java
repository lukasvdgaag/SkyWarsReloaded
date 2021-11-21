package net.gcnt.skywarsreloaded.data.config;

import net.gcnt.skywarsreloaded.utils.SWCoord;

public class RuntimeData implements Data {

    private final YAMLConfig config;

    public SWCoord LOBBY_SPAWN;

    public RuntimeData(YAMLConfig config) {
        this.config = config;
    }

    @Override
    public void loadData(YAMLConfig config) {

    }

    @Override
    public void saveData(YAMLConfig config) {

    }
}
