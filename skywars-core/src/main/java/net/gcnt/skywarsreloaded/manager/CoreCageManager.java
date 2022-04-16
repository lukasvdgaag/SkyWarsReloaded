package net.gcnt.skywarsreloaded.manager;

import com.google.common.collect.ImmutableList;
import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.extent.clipboard.Clipboard;
import net.gcnt.skywarsreloaded.SkyWarsReloaded;
import net.gcnt.skywarsreloaded.data.config.YAMLConfig;
import net.gcnt.skywarsreloaded.game.cages.Cage;
import net.gcnt.skywarsreloaded.game.cages.MaterialCage;
import net.gcnt.skywarsreloaded.game.cages.NormalCageShape;
import net.gcnt.skywarsreloaded.game.cages.SchematicCage;
import net.gcnt.skywarsreloaded.utils.SWCoord;
import net.gcnt.skywarsreloaded.utils.properties.BaseCageTypeProperties;

import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

public class CoreCageManager implements CageManager {

    private final SkyWarsReloaded plugin;
    private final YAMLConfig cagesFile;

    // String id, Cage cage
    private final HashMap<String, Cage> cages;

    public CoreCageManager(SkyWarsReloaded pluginIn) {
        this.plugin = pluginIn;
        this.cages = new HashMap<>();

        this.cagesFile = plugin.getYAMLManager().loadConfig("cages", plugin.getDataFolder(), "cages.yml");

        for (NormalCageShape shape : NormalCageShape.values()) {
            shape.loadLocations();
        }
    }

    @Override
    public void loadAllCages() {
        cagesFile.getKeys("cages").forEach(cageName -> {
            String cageType = cagesFile.getString("cages." + cageName + ".type");

            Cage cage = null;
            if (cageType != null) {
                if (cageType.equals(BaseCageTypeProperties.SCHEMATIC)) {
                    try {
                        cage = new SchematicCage(plugin, cageName, cagesFile.getString("cages." + cageName + ".file-name"));
                    } catch (FileNotFoundException ex) {
                        plugin.getLogger().error(ex.getMessage());
                        return;
                    }
                } else {
                    // In the case that the cage is only using one item type
                    String material;
                    material = cagesFile.getString("cages." + cageName + ".material");
                    NormalCageShape shape;
                    try {
                        shape = NormalCageShape.valueOf(cagesFile.getString("cages." + cageName + ".shape", "DEFAULT"));
                    } catch (Exception ex) {
                        plugin.getLogger().error("Invalid shape for cage " + cageName + "! Using default shape!");
                        shape = NormalCageShape.DEFAULT;
                    }

                    cage = new MaterialCage(cageName, shape, material);
                }
            }

            if (cage == null) return;

            this.cages.put(cage.getId(), cage);
        });
        createDefaultCage();
    }

    private void createDefaultCage() {
        if (this.cages.containsKey("default")) return;

        MaterialCage defaultCage = new MaterialCage("default", NormalCageShape.DEFAULT, "GLASS");
        this.cages.put("default", defaultCage);
    }

    @Override
    public Cage getCageById(String id) {
        return this.cages.get(id);
    }

    @Override
    public List<Cage> getCagesByType(String type) {
        return this.cages.values().stream().filter(cage -> cage.getType().equals(type)).collect(Collectors.toList());
    }

    @Override
    public List<Cage> getAllCages() {
        return ImmutableList.copyOf(this.cages.values());
    }

    @Override
    public EditSession placeCage(Cage cage, SWCoord cageLocation) {
        if (cage.getType().equals(BaseCageTypeProperties.SCHEMATIC)) {
            SchematicCage schematicCage = (SchematicCage) cage;

            Clipboard clipboard = plugin.getSchematicManager().getSchematic(schematicCage.getFile());
            return plugin.getSchematicManager().pasteSchematic(clipboard, cageLocation, true);
        } else {
            MaterialCage materialCage = (MaterialCage) cage;
            NormalCageShape shape = materialCage.getShape();

            shape.getLocations().forEach(blockLoc -> {
                final String blockName = materialCage.getMaterials().size() == 0 ? "BARRIER" : materialCage.getMaterials().get(0);
                // todo fix small offset when placing blocks
                cageLocation.getWorld().setBlockAt(cageLocation.clone().add(blockLoc), blockName);
            });
            return null;
        }
    }
}
