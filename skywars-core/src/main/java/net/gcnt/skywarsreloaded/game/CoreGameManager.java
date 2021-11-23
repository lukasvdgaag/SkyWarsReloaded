package net.gcnt.skywarsreloaded.game;

import com.google.common.collect.Lists;
import net.gcnt.skywarsreloaded.SkyWarsReloaded;
import net.gcnt.skywarsreloaded.utils.properties.FolderProperties;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public abstract class CoreGameManager implements GameManager {

    public final SkyWarsReloaded plugin;
    public HashMap<GameTemplate, List<GameWorld>> gameWorlds;
    private HashMap<String, GameTemplate> templates;

    public CoreGameManager(SkyWarsReloaded plugin) {
        this.plugin = plugin;
        this.templates = new HashMap<>();
        this.gameWorlds = new HashMap<>();
    }

    @Override
    public void loadAllGameTemplates() {
        File dir = new File(plugin.getDataFolder(), FolderProperties.TEMPLATE_FOLDER.toString());

        // Sanity checks
        if (!dir.exists()) return;

        // Reset all currently loaded templates
        this.templates.clear();

        // Load all from directory
        for (File file : dir.listFiles()) {

            // Sanity checks
            if (file.isDirectory() || !file.getName().endsWith(".yml")) continue;
            String name = file.getName().replace(".yml", "");
            if (getGameTemplateByName(name) != null) continue;

            // Load data & store in cache
            GameTemplate template = new CoreGameTemplate(plugin, name);
            templates.put(name, template);

            template.loadData();
            plugin.getLogger().info("Loaded game template '" + name + "'.");
        }
    }

    @Override
    public GameTemplate getGameTemplateByName(String gameId) {
        return templates.getOrDefault(gameId, null);
    }

    @Override
    public GameWorld getGameWorldFromWorldName(String worldName) {
        if (worldName == null) return null;
        for (GameWorld gameWorld : getGameWorlds()) {
            if (gameWorld.getWorldName().equals(worldName)) return gameWorld;
        }
        return null;
    }

    @Override
    public void deleteGameTemplate(String gameId) {
        // todo game template deletion here
    }

    @Override
    public List<GameTemplate> getGameTemplates() {
        return this.templates.values().stream().toList();
    }

    @Override
    public GameTemplate createGameTemplate(String gameId) {
        if (getGameTemplateByName(gameId) != null) return null;
        GameTemplate template = new CoreGameTemplate(plugin, gameId);
        template.loadData();
        template.saveData();
        return template;
    }

    @Override
    public abstract GameWorld createGameWorld(GameTemplate data);

    @Override
    public List<GameWorld> getGameWorlds(GameTemplate data) {
        return gameWorlds.getOrDefault(data, new ArrayList<>());
    }

    @Override
    public List<GameWorld> getGameWorlds() {
        List<GameWorld> worlds = new ArrayList<>();
        gameWorlds.forEach((gameTemplate, gameWorlds1) -> worlds.addAll(gameWorlds1));
        return worlds;
    }

    protected void addWorld(GameTemplate temp, GameWorld wrld) {
        List<GameWorld> worlds = this.gameWorlds.getOrDefault(temp, new ArrayList<>());
        worlds.add(wrld);
        this.gameWorlds.put(temp, worlds);
    }

}
