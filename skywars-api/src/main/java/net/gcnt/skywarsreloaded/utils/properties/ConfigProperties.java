package net.gcnt.skywarsreloaded.utils.properties;

public enum ConfigProperties {

    // Server info
    SERVER_NAME("server.name"),
    SERVER_PROXY("server.proxy"),
    SERVER_TYPE("server.type"),

    // Storage database implementation & auth
    STORAGE_TYPE("storage.type"),
    STORAGE_MYSQL_HOSTNAME("storage.mysql.hostname"),
    STORAGE_MYSQL_PORT("storage.mysql.port"),
    STORAGE_MYSQL_USERNAME("storage.mysql.username"),
    STORAGE_MYSQL_PASSWORD("storage.mysql.password"),
    STORAGE_MYSQL_DATABASE("storage.mysql.database"),
    STORAGE_MYSQL_TABLE_PREFIX("storage.mysql.table-prefix"),
    STORAGE_MYSQL_USE_SSL("storage.mysql.use-ssl"),

    // Messaging

    MESSAGING_TYPE("messaging.type"),
    MESSAGING_REDIS_HOSTNAME("messaging.redis.hostname"),
    MESSAGING_REDIS_USERNAME("messaging.redis.username"),
    MESSAGING_REDIS_PASSWORD("messaging.redis.password"),
    MESSAGING_REDIS_PORT("messaging.redis.port"),

    // menus
    MENUS_KITS_LAYOUT("menus.kits.layout"),
    MENUS_KITS_AUTO_LAYOUT("menus.kits.auto-layout"),
    MENUS_KITS_ENCHANT_SELECTED_KIT("menus.kits.enchant-selected-kit"),

    // items
    ITEMS_GAME_KIT_SELECTOR_SLOT("items.game.kit-selector.slot"),
    ITEMS_GAME_LEAVE_SLOT("items.game.leave.slot"),
    // todo: messaging impl & auth

    // Game instances
    GAME_INSTANCES("game-instances"),
    GAME_INSTANCES_LOCAL_MODE("game-instances.local-mode"),
    GAME_INSTANCES_LOCAL_MODE_IDLE_INSTANCES("game-instances.local-mode.idle-instances"),
    GAME_INSTANCES_PROXY_MODE("game-instances.proxy-mode"),
    GAME_INSTANCES_PROXY_MODE_DYNAMIC_MODE("game-instances.proxy-mode.dynamic-mode"),
    GAME_INSTANCES_PROXY_MODE_DYNAMIC_MODE_MIN_IDLE("game-instances.proxy-mode.dynamic-mode.min-idle"),
    GAME_INSTANCES_PROXY_MODE_DYNAMIC_MODE_MAX_INSTANCES("game-instances.proxy-mode.dynamic-mode.max-instances"),
    GAME_INSTANCES_PROXY_MODE_FIXED_TEMPLATES("game-instances.proxy-mode.fixed-templates"),

    // Game preferences
    GAME_SOLO_WAITING_LOBBY("game.solo-waiting-lobby"),
    CAGE_SEPARATE_CAGES("game.cages.separate-cages"),
    // Game prefs - Timer
    GAME_TIMER_WAITING_LOBBY("game.timers.waiting-lobby"),
    GAME_TIMER_WAITING_LOBBY_FULL("game.timers.waiting-lobby-full"),
    GAME_TIMER_WAITING_CAGES("game.timers.waiting-cages"),
    GAME_TIMER_WAITING_CAGES_FULL("game.timers.waiting-cages-full"),
    GAME_TIMER_ENDING("game.timers.ending"),
    // Game prefs - Chests
    GAME_CHESTS_MAX_ITEMS("game.chests.max-items"),
    GAME_CHESTS_MAX_ITEMS_DOUBLE("game.chests.max-items-double"),

    // World loader
    ENABLE_SLIME_WORLD_MANAGER("world-loader.enable-swm"),
    SLIME_WORLD_LOADER("world-loader.swm-loader"),

    // Parties
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
