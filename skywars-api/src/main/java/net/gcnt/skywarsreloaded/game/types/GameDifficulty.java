package net.gcnt.skywarsreloaded.game.types;

import java.util.HashMap;

public class GameDifficulty {

    public static final GameDifficulty BASIC;
    public static final GameDifficulty NORMAL;
    public static final GameDifficulty INSANE;
    private static final HashMap<String, GameDifficulty> difficulties;

    static {
        difficulties = new HashMap<>();
        BASIC = new GameDifficulty("basic");
        NORMAL = new GameDifficulty("normal");
        INSANE = new GameDifficulty("insane");
    }

    // Instance
    private final String id;

    private GameDifficulty(String idIn) {
        this.id = idIn;
        if (!difficulties.containsKey(this.id)) difficulties.put(this.id, this);
    }

    public static GameDifficulty getById(String key) {
        GameDifficulty difficulty = difficulties.get(key);
        if (difficulty == null) return new GameDifficulty(key);
        return difficulty;
    }

    public String getId() {
        return this.id;
    }

    @Override
    public String toString() {
        return this.getId();
    }
}
