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
import net.gcnt.skywarsreloaded.game.GameWorld;
import net.gcnt.skywarsreloaded.utils.properties.ConfigProperties;
import org.bukkit.Bukkit;

import java.io.IOException;
import java.util.HashMap;
import java.util.concurrent.ExecutionException;

public class SlimeWorldLoader extends BukkitWorldLoader {

    private final SlimePlugin slimeWorldManagerPlugin;
    private final SlimeLoader slimeLoader;

    private final String slimeLoaderType;

    private final HashMap<GameWorld, SlimePropertyMap> templatePropertyMap;

    public SlimeWorldLoader(SkyWarsReloaded plugin) {
        super(plugin);

        this.slimeWorldManagerPlugin = (SlimePlugin) Bukkit.getPluginManager().getPlugin("SlimeWorldManager");
        assert this.slimeWorldManagerPlugin != null;

        slimeLoaderType = this.plugin.getConfig().getString(ConfigProperties.SLIME_WORLD_LOADER.toString(), "file");
        slimeLoader = slimeWorldManagerPlugin.getLoader(slimeLoaderType);

        this.templatePropertyMap = new HashMap<>();
    }

    @Override
    public boolean generateWorldInstance(GameWorld gameWorld) {
        this.plugin.getScheduler().runAsync(() -> {
            String templateName = gameWorld.getTemplate().getName();
            try {
                // This method should be called asynchronously
                SlimeWorld templateWorld = slimeWorldManagerPlugin.loadWorld(slimeLoader, templateName, true, templatePropertyMap.get(gameWorld));
                SlimeWorld tmpWorld = templateWorld.clone(gameWorld.getId());

                // This method must be called synchronously
                plugin.getScheduler().callSyncMethod(() -> {
                    slimeWorldManagerPlugin.generateWorld(tmpWorld);
                    return null;
                }).get();

            } catch (UnknownWorldException ex) {
                plugin.getLogger().error(String.format("Attempted to load template '%s$' from SWM but doesn't exist! (loader: %s$)", templateName, slimeLoaderType));
            } catch(IOException ex) {
                ex.printStackTrace();
                plugin.getLogger().reportException(ex);
            } catch(CorruptedWorldException ex) {
                plugin.getLogger().error(String.format("The world template '%s$' is corrupted! (loader: %s$)", templateName, slimeLoaderType));
            } catch(NewerFormatException ex) {
                plugin.getLogger().error(String.format("The world template '%s$' is in a newer format! (loader: %s$)", templateName, slimeLoaderType));
            } catch(WorldInUseException ex) {
                plugin.getLogger().error(String.format("The world template '%s$' is in use by another server in non read-only mode! (loader: %s$)", templateName, slimeLoaderType));
            } catch (ExecutionException | InterruptedException ignored) {}
        });
    }

    @Override
    public void createEmptyWorld(GameWorld gameWorld) {
        // Create a new and empty property map
        SlimePropertyMap properties = new SlimePropertyMap();

        properties.setString(SlimeProperties.DIFFICULTY, "normal");
        properties.setInt(SlimeProperties.SPAWN_X, 0);
        properties.setInt(SlimeProperties.SPAWN_Y, 64);
        properties.setInt(SlimeProperties.SPAWN_Z, 0);
    }

    @Override
    public void deleteWorld(GameWorld gameWorld) {

    }

    @Override
    public void createBasePlatform(GameWorld gameWorld) {

    }

    @Override
    public void updateWorldBorder(GameWorld gameWorld) {

    }
}
