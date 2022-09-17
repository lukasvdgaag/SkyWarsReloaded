package net.gcnt.skywarsreloaded.manager.gameinstance;

import net.gcnt.skywarsreloaded.SkyWarsReloaded;
import net.gcnt.skywarsreloaded.game.CoreGameTemplate;
import net.gcnt.skywarsreloaded.game.GameInstance;
import net.gcnt.skywarsreloaded.game.GameTemplate;
import net.gcnt.skywarsreloaded.game.LocalGameInstance;
import net.gcnt.skywarsreloaded.utils.properties.FolderProperties;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public abstract class CoreGameInstanceManager implements GameManager {

    public final SkyWarsReloaded plugin;
    private final HashMap<GameTemplate, List<GameInstance>> gameInstances;
    private final HashMap<String, GameTemplate> templates;

    public CoreGameInstanceManager(SkyWarsReloaded plugin) {
        this.plugin = plugin;
        this.templates = new HashMap<>();
        this.gameInstances = new HashMap<>();
    }

    @Override
    public void loadAllGameTemplates() {
        File dir = new File(plugin.getDataFolder(), FolderProperties.TEMPLATE_FOLDER.toString());

        // Sanity checks
        if (!dir.exists()) return;

        // Reset all currently loaded templates
        this.getGameTemplates().clear();

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
            getGameTemplates().put(name, template);

            template.loadData();
            plugin.getLogger().info("Loaded game template '" + name + "'.");
        }
    }

    @Override
    public GameTemplate getGameTemplateByName(String gameId) {
        return getGameTemplates().getOrDefault(gameId, null);
    }

    @Override
    public GameInstance getGameInstanceByName(String name) {
        if (name == null) return null;
        for (GameInstance gameInstance : getGameInstancesListCopy()) {
            if (gameInstance instanceof LocalGameInstance && ((LocalGameInstance) gameInstance).getWorldName().equals(name)) return gameInstance;
        }
        return null;
    }

    @Override
    public List<GameInstance> getGameInstancesCopy(GameTemplate data) {
        return getGameInstances().getOrDefault(data, new ArrayList<>());
    }

    @Override
    public List<GameInstance> getGameInstancesListCopy() {
        List<GameInstance> instances = new ArrayList<>();
        getGameInstances().forEach((gameTemplate, templateInstances) -> instances.addAll(templateInstances));
        return instances;
    }

    @Override
    public boolean deleteGameTemplate(String gameId, boolean deleteMap) {
        GameTemplate template = this.getGameTemplateByName(gameId);
        this.getGameTemplates().remove(gameId);
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
    public List<GameTemplate> getGameTemplatesCopy() {
        return new ArrayList<>(this.getGameTemplates().values());
    }

    @Override
    public GameTemplate createGameTemplate(String gameId) {
        // If a game template already exists with that name, return null
        if (this.getGameTemplateByName(gameId) != null) return null;

        // Create new game template & save to disk
        GameTemplate template = new CoreGameTemplate(plugin, gameId);
        template.loadData();
        template.saveData();
        this.getGameTemplates().put(gameId, template);
        return template;
    }

    @Override
    public abstract CompletableFuture<GameInstance> createGameWorld(GameTemplate data);

    @Override
    public CompletableFuture<Void> deleteGameInstance(GameInstance world) {
        plugin.getWorldLoader().deleteWorldInstance(world);
        final HashMap<GameTemplate, List<GameInstance>> gameInstances = this.getGameInstances();

        for (GameTemplate template : gameInstances.keySet()) {
            List<GameInstance> instances = gameInstances.get(template);
            if (instances.contains(world)) {
                instances.remove(world);
                gameInstances.put(template, instances);
                return CompletableFuture.completedFuture(null);
            }
        }

        return CompletableFuture.completedFuture(null);
    }

    @Override
    public List<GameInstance> getGameInstancesByTemplate(GameTemplate template) {
        return new ArrayList<>(getGameInstances().get(template));
    }

    // Internal util

    protected void registerGameWorld(GameTemplate temp, GameInstance gameInstance) {
        List<GameInstance> instances = this.getGameInstances().getOrDefault(temp, new ArrayList<>());
        instances.add(gameInstance);
        this.getGameInstances().put(temp, instances);
    }

    public HashMap<GameTemplate, List<GameInstance>> getGameInstances() {
        return gameInstances;
    }

    public HashMap<String, GameTemplate> getGameTemplates() {
        return this.templates;
    }
}
