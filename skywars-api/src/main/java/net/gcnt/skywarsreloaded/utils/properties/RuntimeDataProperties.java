package net.gcnt.skywarsreloaded.utils.properties;

public enum RuntimeDataProperties {

    LOBBY_SPAWN("lobby-spawn"),
    ;

    private final String value;

    RuntimeDataProperties(String valueIn) {
        this.value = valueIn;
    }

    @Override
    public String toString() {
        return this.value;
    }

}
