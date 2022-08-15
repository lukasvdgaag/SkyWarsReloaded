package net.gcnt.skywarsreloaded.utils.properties;

public enum ChestTierProperties {

    DISPLAY_NAME("display-name"),
    CONTENTS("contents"),
    TYPES("type");

    private final String property;

    ChestTierProperties(String property) {
        this.property = property;
    }

    @Override
    public String toString() {
        return property;
    }

}
