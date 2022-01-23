package net.gcnt.skywarsreloaded.utils.properties;

public enum ChestProperties {

    DISPLAY_NAME("display-name"),
    CONTENTS("contents"),
    DIFFICULTIES("difficulties");

    private final String property;

    ChestProperties(String property) {
        this.property = property;
    }

    @Override
    public String toString() {
        return property;
    }

}
