package net.gcnt.skywarsreloaded.game;

import net.gcnt.skywarsreloaded.SkyWarsReloaded;
import net.gcnt.skywarsreloaded.utils.properties.FolderProperties;
import net.gcnt.skywarsreloaded.wrapper.world.SWWorld;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public abstract class CoreGameManager implements GameManager {

    public final SkyWarsReloaded plugin;
    public final HashMap<GameTemplate, List<GameWorld>> gameWorlds;
    private final HashMap<String, GameTemplate> templates;

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
        File[] files = dir.listFiles();
        assert files != null;
        for (File file : files) {

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
    public GameWorld getGameWorldByName(String worldName) {
        if (worldName == null) return null;
        for (GameWorld gameWorld : getGameWorlds()) {
            if (gameWorld.getWorldName().equals(worldName)) return gameWorld;
        }
        return null;
    }

    @Override
    public GameWorld getGameWorldBySWWorld(SWWorld swWorld) {
        if (swWorld == null) return null;
        for (GameWorld gameWorld : getGameWorlds()) {
            if (gameWorld.getWorld().equals(swWorld)) return gameWorld;
        }
        return null;
    }

    @Override
    public boolean deleteGameTemplate(String gameId, boolean deleteMap) {
        GameTemplate template = this.getGameTemplateByName(gameId);
        this.templates.remove(gameId);
        if (template == null) return false;

        // Delete template data
        try {
            Files.deleteIfExists(template.getConfig().getFile().toPath());
        } catch (IOException e) {
            this.plugin.getLogger().error("Could not delete the template file do to the error below:");
            e.printStackTrace();
            return false;
        }

        // Delete map data (blocks of the skywars map, not matter the storage method)
        if (deleteMap) {
            this.plugin.getWorldLoader().deleteMap(template, false);
        }

        return true;
    }

    @Override
    public List<GameTemplate> getGameTemplates() {
        return this.templates.values().stream().toList();
    }

    @Override
    public GameTemplate createGameTemplate(String gameId) {
        // If a game template already exists with that name, return null
        if (this.getGameTemplateByName(gameId) != null) return null;

        // Create new game template & save to disk
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

    @Override
    public List<GameWorld> getGameWorldsByTemplate(GameTemplate template) {
        return new ArrayList<>(gameWorlds.get(template));
    }

    // Internal util

    protected void registerGameWorld(GameTemplate temp, GameWorld world) {
        List<GameWorld> worlds = this.gameWorlds.getOrDefault(temp, new ArrayList<>());
        worlds.add(world);
        this.gameWorlds.put(temp, worlds);
    }

}
