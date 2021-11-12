package net.gcnt.skywarsreloaded.utils.properties;

public enum KitProperties {

    DISPLAY_NAME("display-name"),
    ICON("icon"),
    UNAVAILABLE_ICON("unavailable_icon"),
    DESCRIPTION("description"),
    SLOT("slot"),
    LORE("lore"),
    EFFECTS("effects"),

    REQUIREMENTS_PERMISSION("requirements.permission"),
    REQUIREMENTS_COST("requirements.cost"),
    REQUIREMENTS_STATS("requirements.stats"),

    CONTENTS("contents"),
    ARMOR_CONTENTS("contents.armor"),
    INVENTORY_CONTENTS("contents.inventory"),
    HELMET("contents.armor.helmet"),
    CHESTPLATE("contents.armor.chestplate"),
    LEGGINGS("contents.armor.leggings"),
    BOOTS("contents.armor.boots"),
    OFF_HAND("contents.off-hand");


    private final String property;

    KitProperties(String property) {
        this.property = property;
    }

    @Override
    public String toString() {
        return property;
    }

}
