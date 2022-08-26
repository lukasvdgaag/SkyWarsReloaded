package net.gcnt.skywarsreloaded.utils.properties;

public enum MapDataProperties {

    DISPLAY_NAME("display-name"),
    CREATOR("creator"),
    TEAM_SIZE("team-size"),
    MIN_PLAYERS("min-players"),
    ENABLED("enabled"),
    BORDER_RADIUS("border-radius"),
    ENABLED_CHESTTIERS("enabled-chesttiers"),

    IS_TEAMSIZE_SETUP("checks.teamsize-setup"),

    DEFAULT_CHEST("default-chest-type"),
    DEFAULT_HEALTH("default-health"),
    DEFAULT_TIME("default-time"),
    DEFAULT_WEATHER("default-weather"),
    DEFAULT_MODIFIER("default-modifier"),

    SPAWNPOINTS("spawn-points"),
    CHESTS("chests"),
    SIGNS("signs"),

    LOBBY_SPAWN("lobby-spawn"),
    SPECTATE_SPAWN("spectate-spawn"),

    ALLOW_DISPERSED_PARTIES("allow-dispersed-parties");

    private final String value;

    MapDataProperties(String valueIn) {
        this.value = valueIn;
    }

    @Override
    public String toString() {
        return this.value;
    }

}
