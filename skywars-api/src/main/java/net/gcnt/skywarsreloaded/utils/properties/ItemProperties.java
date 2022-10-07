package net.gcnt.skywarsreloaded.utils.properties;

public enum ItemProperties {

    GENERAL_CLOSE("items.general.close"),
    GENERAL_CONFIRM("items.general.confirm"),
    GENERAL_DENY("items.general.deny"),

    GAME_KIT_SELECTOR("items.game.kit-selector"),
    GAME_KIT_LEAVE("items.game.leave"),

    KITS_DESELECT("items.kits.deselect");

    private final String value;

    ItemProperties(String valueIn) {
        this.value = valueIn;
    }

    @Override
    public String toString() {
        return this.value;
    }

}
