package net.gcnt.skywarsreloaded.game.cages;

import net.gcnt.skywarsreloaded.data.CoreUnlockable;

import java.util.ArrayList;
import java.util.List;

public class MaterialCage extends CoreUnlockable implements Cage {

    private final String id;
    private final List<String> materials;
    private final NormalCageShape shape;

    public MaterialCage(String id, List<String> materials) {
        this.id = id;
        this.materials = materials == null ? new ArrayList<>() : materials;
    }

    @Override
    public String getId() {
        return this.id;
    }

    @Override
    public String getType() {
        return "material";
    }

    public List<String> getMaterials() {
        return this.materials;
    }

    @Override
    public String getPermissionPrefix() {
        return "sw.kit.";
    }
}
