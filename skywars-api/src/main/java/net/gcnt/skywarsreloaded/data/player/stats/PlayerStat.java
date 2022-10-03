package net.gcnt.skywarsreloaded.data.player.stats;

public enum PlayerStat {

    SOLO_WINS("solo_wins"),
    SOLO_KILLS("solo_kills"),
    SOLO_GAMES("solo_games"),
    TEAM_WINS("team_wins"),
    TEAM_KILLS("team_kills"),
    TEAM_GAMES("team_games"),
    TOTAL_WINS("wins"),
    TOTAL_KILLS("kills"),
    TOTAL_GAMES("games"),
    EXPERIENCE("experience"),
    LEVEL("level");

    private final String property;

    PlayerStat(String property) {
        this.property = property;
    }

    public static PlayerStat fromString(String input) {
        for (PlayerStat value : values()) {
            if (value.name().equalsIgnoreCase(input) || value.getProperty().equalsIgnoreCase(input))
                return value;
        }
        return null;
    }

    public String getProperty() {
        return property;
    }
}
