package net.gcnt.skywarsreloaded.enums;

import com.google.common.collect.Lists;

import java.util.ArrayList;
import java.util.List;

public enum DeathReason {

    DROWNING("drowning"),
    DROWNING_KILL("drowning-kill", true),
    EXPLOSION("explosion"),
    EXPLOSION_KILL("explosion-kill", true),
    FALL("fall"),
    FALL_KILL("fall-kill", true),
    FALLING_BLOCK("falling-block"),
    FALLING_BLOCK_KILL("falling-block-kill", true),
    FIRE("fire", Lists.newArrayList("HOT_FLOOR", "MELTING", "FIRE_TICK")),
    FIRE_KILL("fire-kill", true),
    LIGHTNING("lightning"),
    LIGHTNING_KILL("lightning-kill", true),
    LAVA("lava"),
    LAVA_KILL("lava-kill", true),
    POISON("poison"),
    POISON_KILL("poison-kill", true),
    STARVATION("starvation"),
    STARVATION_KILL("starvation-kill", true),
    SUFFOCATION("suffocation"),
    SUFFOCATION_KILL("suffocation-kill", true),
    VOID("void"),
    VOID_KILL("void-kill", true),
    MAGIC("magic"),
    MAGIC_KILL("magic-kill", true),
    ENTITY("entity"),
    PROJECTILE("projectile"),
    PROJECTILE_KILL("projectile-kill", true),
    DEFAULT("default"),
    DEFAULT_KILL("default-kill", true);

    private final String property;
    private final boolean kill;
    private final List<String> aliases;

    DeathReason(String property) {
        this(property, false);
    }

    DeathReason(String property, boolean kill) {
        this(property, kill, new ArrayList<>());
    }

    DeathReason(String property, List<String> aliases) {
        this(property, false, aliases);
    }

    DeathReason(String property, boolean kill, List<String> aliases) {
        this.property = property;
        this.kill = kill;
        this.aliases = aliases;
    }

    public static DeathReason fromString(String input) {
        for (DeathReason value : values()) {
            if (value.name().equalsIgnoreCase(input) || value.getProperty().equalsIgnoreCase(input) || value.getAliases().contains(input)) {
                return value;
            }
        }
        return null;
    }

    public String getProperty() {
        return property;
    }

    public boolean isKill() {
        return kill;
    }

    public List<String> getAliases() {
        return aliases;
    }
}
