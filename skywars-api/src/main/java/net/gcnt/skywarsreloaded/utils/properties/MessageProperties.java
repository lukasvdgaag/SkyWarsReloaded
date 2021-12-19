package net.gcnt.skywarsreloaded.utils.properties;

public enum MessageProperties {

    CHAT_HEADER("chat.header"),
    CHAT_LOBBY_SPAWN_SET("chat.lobby-spawn-set"),

    ERROR_CANNOT_SET_LOBBYSPAWN_IN_GAMEWORLD("errors.cannot-set-lobbyspawn-in-gameworld"),
    ERROR_WORLD_NOT_RECOGNIZED("errors.world-not-recognized"),
    ERROR_MUST_HOLD_ITEM("errors.must-hold-item"),

    KITS_ENTER_NAME("kits.enter-name"),
    KITS_ENTER_DESCRIPTION("kits.enter-description"),
    KITS_ENTER_DISPLAYNAME("kits.enter-displayname"),
    KITS_ENTER_LORE_ACTION("kits.enter-lore-action"),
    KITS_ENTER_LORE_LINE("kits.enter-lore-line"),
    KITS_ENTER_SLOT("kits.enter-slot"),
    KITS_ENTER_SLOT_NUMBER("kits.enter-slot-number"),
    KITS_ENTER_SLOT_GREATER("kits.enter-slot-greater"),
    KITS_ENTER_LORE_INDEX_NUMBER("kits.enter-lore-index-number"),
    KITS_ENTER_LORE_INDEX_GREATER("kits.enter-lore-index-greater"),
    KITS_ENTER_LORE_INDEX_INVALID("kits.enter-lore-index-invalid"),
    KITS_ENTER_KIT_REQUIREMENT("kits.enter-kit-requirement"),
    KITS_ENTER_KIT_REQUIREMENT_VALUE("kits.enter-kit-requirement-value"),
    KITS_ENTER_KIT_REQUIREMENT_VALUE_BOOLEAN("kits.enter-kit-requirement-value-boolean"),
    KITS_ENTER_KIT_STAT_REQUIREMENT("kits.enter-kit-stat-requirement"),
    KITS_ENTER_KIT_REQUIREMENT_VALUE_NUMBER("kits.enter-kit-requirement-value-number"),
    KITS_ENTER_KIT_REQUIREMENT_VALUE_GREATER("kits.enter-kit-requirement-value-greater"),
    KITS_ALREADY_EXISTS("kits.already-exists"),
    KITS_CREATED("kits.created"),
    KITS_DOESNT_EXIST("kits.doesnt-exist"),
    KITS_PREVIEWED("kits.previewed"),
    KITS_SET_CONTENTS("kits.set-contents"),
    KITS_SET_DESCRIPTION("kits.set-description"),
    KITS_SET_DISPLAYNAME("kits.set-displayname"),
    KITS_SET_ICON("kits.set-icon"),
    KITS_SET_SLOT("kits.set-slot"),
    KITS_SET_UNAVAILABLE_ICON("kits.set-unavailable-icon"),
    KITS_SET_REQUIREMENT("kits.set-requirement"),
    KITS_ADDED_LORE_LINE("kits.added-lore-line"),
    KITS_REMOVED_LORE_LINE("kits.removed-lore-line"),
    KITS_CLEARED_LORE("kits.cleared-lore"),
    KITS_PREVIEW_LORE_HEADER("kits.preview-lore-header"),
    KITS_PREVIEW_LORE_NO_LINES("kits.preview-lore-no-lines"),
    KITS_PREVIEW_LORE_LINE("kits.preview-lore-line");

    private final String value;

    MessageProperties(String valueIn) {
        this.value = valueIn;
    }

    public String toString() {
        return value;
    }
}
