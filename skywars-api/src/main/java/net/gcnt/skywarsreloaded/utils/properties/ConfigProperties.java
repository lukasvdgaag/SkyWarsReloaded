package net.gcnt.skywarsreloaded.utils.properties;

public enum ConfigProperties {

    STORAGE_TYPE("storage.type"),
    STORAGE_USERNAME("storage.username"),
    STORAGE_HOSTNAME("storage.hostname"),
    STORAGE_PASSWORD("storage.password"),
    STORAGE_DATABASE("storage.database"),
    STORAGE_USE_SSL("storage.use-ssl"),

    GAME_INSTANCES("game-instances"),
    GAME_INSTANCES_DEFAULT("game-instances.default"),

    ENABLE_SLIME_WORLD_MANAGER("world-loader.enable-swm"),
    SLIME_WORLD_LOADER("world-loader.swm-loader");

    private final String value;

    ConfigProperties(String valueIn) {
        this.value = valueIn;
    }

    @Override
    public String toString() {
        return this.value;
    }

}
