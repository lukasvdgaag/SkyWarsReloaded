package net.gcnt.skywarsreloaded.game.types;

import java.util.HashMap;

public class GameDifficulty {

    public static final GameDifficulty BASIC = new GameDifficulty("basic");
    public static final GameDifficulty NORMAL = new GameDifficulty("normal");
    public static final GameDifficulty INSANE = new GameDifficulty("insane");

    private static final HashMap<String, GameDifficulty> difficulties = new HashMap<>();

    public static GameDifficulty getById(String key) {
        GameDifficulty difficulty = difficulties.get(key);
        if (difficulty == null) return new GameDifficulty(key);
        return difficulty;
    }

    // Instance
    private final String id;

    private GameDifficulty(String idIn) {
        this.id = idIn;
        if (!difficulties.containsKey(this.id)) difficulties.put(this.id, this);
    }

    public String getId() {
        return this.id;
    }

    @Override
    public String toString() {
        return this.getId();
    }
}
