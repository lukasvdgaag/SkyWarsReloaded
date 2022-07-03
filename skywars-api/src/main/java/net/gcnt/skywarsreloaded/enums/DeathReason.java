package net.gcnt.skywarsreloaded.enums;

public enum DeathReason {

    DROWNING("drowning"),
    DROWNING_KILL("drowning-kill"),
    EXPLOSION("explosion"),
    EXPLOSION_KILL("explosion-kill"),
    FALL("fall"),
    FALL_KILL("fall-kill"),
    FALLING_BLOCK("falling-block"),
    FALLING_BLOCK_KILL("falling-block-kill"),
    FIRE("fire"),
    FIRE_KILL("fire-kill"),
    LIGHTNING("lightning"),
    LIGHTNING_KILL("lightning-kill"),
    LAVA("lava"),
    LAVA_KILL("lava-kill"),
    POISON("poison"),
    POISON_KILL("poison-kill"),
    STARVATION("starvation"),
    STARVATION_KILL("starvation-kill"),
    SUFFOCATION("suffocation"),
    SUFFOCATION_KILL("suffocation-kill"),
    VOID("void"),
    VOID_KILL("void-kill"),
    MAGIC("magic"),
    MAGIC_KILL("magic-kill"),
    ENTITY("entity"),
    DEFAULT("default"),
    DEFAULT_KILL("default-kill");

    private final String property;

    DeathReason(String property) {
        this.property = property;
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
}
