package net.gcnt.skywarsreloaded.game.types;

public enum SpawnType {

    PLAYER("team"),
    SPECTATE("spectator", "spectating", "spec"),
    LOBBY;

    private final String[] aliases;

    SpawnType(String... aliases) {
        this.aliases = aliases;
    }

    public static SpawnType fromString(String input) {
        for (SpawnType s : values()) {
            if (s.name().equalsIgnoreCase(input)) return s;
            for (String a : LOBBY.getAliases()) {
                if (a.equalsIgnoreCase(input)) return s;
            }
        }
        return null;
    }

    public String[] getAliases() {
        return aliases;
    }

}
