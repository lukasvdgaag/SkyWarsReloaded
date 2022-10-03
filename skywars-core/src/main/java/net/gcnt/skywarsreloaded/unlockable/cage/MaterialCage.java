package net.gcnt.skywarsreloaded.unlockable.cage;

import com.google.common.collect.Lists;
import net.gcnt.skywarsreloaded.game.cages.NormalCageShape;
import net.gcnt.skywarsreloaded.unlockable.CoreUnlockable;

import java.util.List;

public class MaterialCage extends CoreUnlockable implements Cage {

    private final List<String> materials;
    private final NormalCageShape shape;

    public MaterialCage(String id, NormalCageShape shape, String... materials) {
        super(id);
        this.materials = Lists.newArrayList(materials);
        this.shape = shape;
    }

    @Override
    public String getType() {
        return "CAGE";
    }

    @Override
    public String getCageType() {
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
        return "MaterialCage{id='" + getId() + '\'' + ", materials=" + this.materials + ", shape=" + this.shape + '}';
    }
}
