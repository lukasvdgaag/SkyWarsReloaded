package net.gcnt.skywarsreloaded.utils;

import net.gcnt.skywarsreloaded.SkyWarsReloaded;
import net.gcnt.skywarsreloaded.wrapper.player.SWPlayer;

public abstract class AbstractEffect implements Effect {

    private final SkyWarsReloaded plugin;
    private String type;
    private int duration;
    private int strength;
    private boolean particles;

    public AbstractEffect(SkyWarsReloaded plugin, String input) {
        this.plugin = plugin;
        this.type = input.toUpperCase();
        this.duration = Integer.MAX_VALUE;
        this.strength = 1;
        this.particles = true;

        if (input.contains(":")) {
            String[] split = input.split(":");
            if (split.length == 2) {
                // <TYPE>:[strength]
                type = split[0].toUpperCase();
                if (plugin.getUtils().isInt(split[1])) {
                    strength = Integer.parseInt(split[1]);
                    if (strength < 0) {
                        plugin.getLogger().error(String.format("Failed to load strength of effect from string %s. Using the default: %d. (%s)", input, 1, "java.lang.IllegalArgumentException: " + split[1] + " must be at least 1."));
                        strength = 1;
                    }
                } else {
                    plugin.getLogger().error(String.format("Failed to load strength of effect from string %s. Using the default: %d. (%s)", input, 1, "java.lang.NumberFormatException: " + split[1] + " is not a number."));
                }
            } else {
                // 3 or more args.
                // <TYPE>:[duration]:[strength]
                type = split[0].toUpperCase();
                if (plugin.getUtils().isInt(split[1])) {
                    duration = Integer.parseInt(split[1]);
                    if (duration < 0) {
                        plugin.getLogger().error(String.format("Failed to load duration option of effect from string %s. Using the default: %s. (%s)", input, "infinite", "java.lang.IllegalArgumentException: " + split[1] + " must be at least 1."));
                        duration = Integer.MAX_VALUE;
                    }
                } else {
                    plugin.getLogger().error(String.format("Failed to load duration option of effect from string %s. Using the default: %s. (%s)", input, "infinite", "java.lang.NumberFormatException: " + split[1] + " is not a number."));
                }
                if (plugin.getUtils().isInt(split[2])) {
                    strength = Integer.parseInt(split[2]);
                    if (strength < 0) {
                        plugin.getLogger().error(String.format("Failed to load strength option of effect from string %s. Using the default: %d. (%s)", input, 1, "java.lang.IllegalArgumentException: " + split[2] + " must be at least 1."));
                        strength = 1;
                    }
                } else {
                    plugin.getLogger().error(String.format("Failed to load strength option of effect from string %s. Using the default: %d. (%s)", input, 1, "java.lang.NumberFormatException: " + split[2] + " is not a number."));
                }

                if (split.length >= 4) {
                    // <TYPE>:[duration]:[strength]:[showParticles]
                    if (plugin.getUtils().isBoolean(split[3])) {
                        particles = Boolean.parseBoolean(split[3]);
                    } else {
                        plugin.getLogger().error(String.format("Failed to load show-particles option of effect from string %s. Using the default: %b. (%s)", input, true, "java.lang.IllegalArgumentException: " + split[3] + " is not a boolean."));
                    }
                }
            }
        }
    }

    public abstract void applyToPlayer(SWPlayer player);

    @Override
    public String getType() {
        return type;
    }

    @Override
    public int getDuration() {
        return duration;
    }

    @Override
    public int getStrength() {
        return strength;
    }

    @Override
    public boolean showParticles() {
        return particles;
    }
}
