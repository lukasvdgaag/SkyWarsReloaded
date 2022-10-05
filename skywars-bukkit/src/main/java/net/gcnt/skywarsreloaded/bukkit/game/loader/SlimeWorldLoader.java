package net.gcnt.skywarsreloaded.bukkit.game.loader;

import com.grinderwolf.swm.api.SlimePlugin;
import com.grinderwolf.swm.api.exceptions.CorruptedWorldException;
import com.grinderwolf.swm.api.exceptions.NewerFormatException;
import com.grinderwolf.swm.api.exceptions.UnknownWorldException;
import com.grinderwolf.swm.api.exceptions.WorldInUseException;
import com.grinderwolf.swm.api.loaders.SlimeLoader;
import com.grinderwolf.swm.api.world.SlimeWorld;
import com.grinderwolf.swm.api.world.properties.SlimeProperties;
import com.grinderwolf.swm.api.world.properties.SlimePropertyMap;
import net.gcnt.skywarsreloaded.SkyWarsReloaded;
import net.gcnt.skywarsreloaded.bukkit.game.BukkitLocalGameInstance;
import net.gcnt.skywarsreloaded.game.GamePlayer;
import net.gcnt.skywarsreloaded.game.GameTemplate;
import net.gcnt.skywarsreloaded.game.gameinstance.GameInstance;
import net.gcnt.skywarsreloaded.game.gameinstance.LocalGameInstance;
import net.gcnt.skywarsreloaded.game.types.GameState;
import net.gcnt.skywarsreloaded.utils.CoreSWCoord;
import net.gcnt.skywarsreloaded.utils.SWCoord;
import net.gcnt.skywarsreloaded.utils.properties.ConfigProperties;
import net.gcnt.skywarsreloaded.utils.properties.RuntimeDataProperties;
import org.bukkit.Bukkit;

import java.io.IOException;
import java.util.HashMap;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class SlimeWorldLoader extends BukkitWorldLoader {

    private final SlimePlugin slimeWorldManagerPlugin;
    private final SlimeLoader slimeLoader;

    private final String slimeLoaderType;

    private final HashMap<GameInstance, SlimePropertyMap> templatePropertyMap;
    private final HashMap<GameInstance, SlimeWorld> slimeWorldMap;

    public SlimeWorldLoader(SkyWarsReloaded plugin) {
        super(plugin);

        this.slimeWorldManagerPlugin = (SlimePlugin) Bukkit.getPluginManager().getPlugin("SlimeWorldManager");
        assert this.slimeWorldManagerPlugin != null;

        slimeLoaderType = this.plugin.getConfig().getString(ConfigProperties.SLIME_WORLD_LOADER.toString(), "file");
        slimeLoader = slimeWorldManagerPlugin.getLoader(slimeLoaderType);

        this.templatePropertyMap = new HashMap<>();
        this.slimeWorldMap = new HashMap<>();
    }

    @Override
    public CompletableFuture<Boolean> generateWorldInstance(LocalGameInstance gameWorld) {
        CompletableFuture<Boolean> future = new CompletableFuture<>();
        this.plugin.getScheduler().runAsync(() -> {
            try {
                this.createEmptyWorld(gameWorld).get();
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
            String templateName = gameWorld.getTemplate().getName();
            try {
                // This method should be called asynchronously
                SlimePropertyMap propertyMap;
                synchronized (templatePropertyMap) {
                    propertyMap = templatePropertyMap.get(gameWorld);
                }

                SlimeWorld templateWorld = slimeWorldManagerPlugin.loadWorld(slimeLoader, templateName, true, propertyMap);
                SlimeWorld tmpWorld = templateWorld.clone(gameWorld.getId().toString());

                // This method must be called synchronously
                plugin.getScheduler().callSyncMethod(() -> {
                    slimeWorldManagerPlugin.generateWorld(tmpWorld);
                    future.complete(true);
                    return null;
                }).get();

                synchronized (slimeWorldMap) {
                    slimeWorldMap.put(gameWorld, tmpWorld);
                }

            } catch (UnknownWorldException ex) {
                plugin.getLogger().error(String.format("Attempted to load template '%s$' from SWM but doesn't exist! (loader: %s$)", templateName, slimeLoaderType));
                future.complete(false);
            } catch (IOException ex) {
                ex.printStackTrace();
                plugin.getLogger().reportException(ex);
                future.complete(false);
            } catch (CorruptedWorldException ex) {
                plugin.getLogger().error(String.format("The world template '%s$' is corrupted! (loader: %s$)", templateName, slimeLoaderType));
                future.complete(false);
            } catch (NewerFormatException ex) {
                plugin.getLogger().error(String.format("The world template '%s$' is in a newer format! (loader: %s$)", templateName, slimeLoaderType));
                future.complete(false);
            } catch (WorldInUseException ex) {
                plugin.getLogger().error(String.format("The world template '%s$' is in use by another server in non read-only mode! (loader: %s$)", templateName, slimeLoaderType));
                future.complete(false);
            } catch (ExecutionException | InterruptedException ex) {
                future.complete(false);
            }
        });
        return future;
    }

    @Override
    public CompletableFuture<Void> createEmptyWorld(LocalGameInstance gameWorld) {
        // Create a new and empty property map
        SlimePropertyMap properties = new SlimePropertyMap();

        properties.setString(SlimeProperties.DIFFICULTY, "normal");
        properties.setInt(SlimeProperties.SPAWN_X, 0);
        properties.setInt(SlimeProperties.SPAWN_Y, 64);
        properties.setInt(SlimeProperties.SPAWN_Z, 0);
        synchronized (templatePropertyMap) {
            templatePropertyMap.put(gameWorld, properties);
        }

        // This is run async anyway, there is no point executing it under another async task
        return CompletableFuture.completedFuture(null);
    }

    @Override
    public void deleteWorldInstance(LocalGameInstance gameWorld) {
        String spawnLocationStr = this.plugin.getDataConfig().getString(RuntimeDataProperties.LOBBY_SPAWN.toString(), null);
        SWCoord coord = null;
        try {
            coord = new CoreSWCoord(this.plugin, spawnLocationStr);
        } catch (IndexOutOfBoundsException | IllegalArgumentException ex) {
            ex.printStackTrace();
        }

        if (coord == null) {
            coord = this.plugin.getServer().getDefaultWorld().getDefaultSpawnLocation();
        }

        for (GamePlayer player : gameWorld.getPlayersCopy()) {
            player.getSWPlayer().teleport(coord);
        }

        gameWorld.getWorld().unload(false);
    }

    @Override
    public void deleteMap(GameTemplate gameTemplate, boolean forceUnloadInstances) {
        if (forceUnloadInstances) {
            for (GameInstance gameWorld : this.plugin.getGameInstanceManager().getGameInstancesByTemplate(gameTemplate)) {
                if (!gameWorld.getState().equals(GameState.DISABLED)) {
                    // todo gameWorld.forceStop();
                }
                ((LocalGameInstance) gameWorld).getWorld().unload(false);
            }
        }

        try {
            this.slimeLoader.deleteWorld(gameTemplate.getName());
        } catch (UnknownWorldException | IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public CompletableFuture<Boolean> save(LocalGameInstance gameWorld) {
        boolean successful = true;
        try {
            assert gameWorld instanceof BukkitLocalGameInstance;
            SlimeWorld slimeWorld;
            synchronized (slimeWorldMap) {
                slimeWorld = this.slimeWorldMap.get(gameWorld);
            }

            if (slimeWorld.isReadOnly()) {
                successful = false;
            } else {
                ((BukkitLocalGameInstance) gameWorld).getBukkitWorld().save();
            }

        } catch (Exception e) {
            successful = false;
        }
        return CompletableFuture.completedFuture(successful);
    }
}
