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

    GAME_SOLO_WAITING_LOBBY("game.solo-waiting-lobby"),
    CAGE_SEPARATE_CAGES("game.cages.separate-cages"),

    GAME_TIMER_WAITING_LOBBY("game.timers.waiting-lobby"),
    GAME_TIMER_WAITING_LOBBY_FULL("game.timers.waiting-lobby-full"),
    GAME_TIMER_WAITING_CAGES("game.timers.waiting-cages"),
    GAME_TIMER_WAITING_CAGES_FULL("game.timers.waiting-cages-full"),
    GAME_TIMER_ENDING("game.timers.ending"),

    GAME_CHESTS_MAX_ITEMS("game.chests.max-items"),
    GAME_CHESTS_MAX_ITEMS_DOUBLE("game.chests.max-items-double"),

    ENABLE_SLIME_WORLD_MANAGER("world-loader.enable-swm"),
    SLIME_WORLD_LOADER("world-loader.swm-loader"),
    PARTIES_ALLOW_DISPERSED_PARTIES("parties.allow-dispersed-parties");

    private final String value;

    ConfigProperties(String valueIn) {
        this.value = valueIn;
    }

    @Override
    public String toString() {
        return this.value;
    }

}
