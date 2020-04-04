package com.walrusone.skywarsreloaded.game.cages;

public enum CageType {
    CUBE,
    DOME,
    PYRAMID,
    SPHERE,
    STANDARD;

    public static CageType getNext(CageType type) {
        switch (type) {
            case CUBE:
                return CageType.DOME;
            case DOME:
                return CageType.PYRAMID;
            case PYRAMID:
                return CageType.SPHERE;
            case SPHERE:
                return CageType.STANDARD;
            case STANDARD:
                return CageType.CUBE;
            default:
                return CageType.STANDARD;
        }
    }

    public static CageType matchType(String string) {
        for (CageType type : CageType.values()) {
            if (type.toString().equalsIgnoreCase(string)) {
                return type;
            }
        }
        return CageType.STANDARD;
    }
}