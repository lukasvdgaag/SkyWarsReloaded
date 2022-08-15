package net.gcnt.skywarsreloaded.game.types;

import java.util.HashMap;

public class ChestType {

    public static final ChestType ISLAND;
    public static final ChestType CENTER;
    private static final HashMap<String, ChestType> chestTypes;

    static {
        chestTypes = new HashMap<>();
        ISLAND = new ChestType("island");
        CENTER = new ChestType("center");
    }

    // Instance
    private final String id;

    private ChestType(String idIn) {
        this.id = idIn;
        if (!chestTypes.containsKey(this.id)) chestTypes.put(this.id, this);
    }

    public static ChestType getById(String key) {
        ChestType type = chestTypes.get(key);
        if (type == null) return new ChestType(key);
        return type;
    }

    public String getId() {
        return this.id;
    }

    @Override
    public String toString() {
        return this.getId();
    }
}
