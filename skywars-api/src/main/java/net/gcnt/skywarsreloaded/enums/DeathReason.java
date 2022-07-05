package net.gcnt.skywarsreloaded.enums;

public enum DeathReason {

    DROWNING("drowning"),
    DROWNING_KILL("drowning-kill", true),
    EXPLOSION("explosion"),
    EXPLOSION_KILL("explosion-kill", true),
    FALL("fall"),
    FALL_KILL("fall-kill", true),
    FALLING_BLOCK("falling-block"),
    FALLING_BLOCK_KILL("falling-block-kill", true),
    FIRE("fire"),
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
    DEFAULT("default"),
    DEFAULT_KILL("default-kill", true);

    private final String property;
    private final boolean kill;

    DeathReason(String property) {
        this(property, false);
    }

    DeathReason(String property, boolean kill) {
        this.property = property;
        this.kill = kill;
    }

    public static DeathReason fromString(String input) {
        for (DeathReason value : values()) {
            if (value.name().equalsIgnoreCase(input) || value.getProperty().equalsIgnoreCase(input))
                return value;
        }
        return null;
    }

    public String getProperty() {
        return property;
    }

    public boolean isKill() {
        return kill;
    }
}
