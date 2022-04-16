package net.gcnt.skywarsreloaded.game.cages;

import com.google.common.collect.Lists;
import net.gcnt.skywarsreloaded.data.CoreUnlockable;

import java.util.List;

public class MaterialCage extends CoreUnlockable implements Cage {

    private final String id;
    private final List<String> materials;
    private final NormalCageShape shape;

    public MaterialCage(String id, NormalCageShape shape, String... materials) {
        this.id = id;
        this.materials = Lists.newArrayList(materials);
        this.shape = shape;
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

    public NormalCageShape getShape() {
        return this.shape;
    }

    @Override
    public String toString() {
        return "MaterialCage{id='" + this.id + '\'' + ", materials=" + this.materials + ", shape=" + this.shape + '}';
    }
}
