package net.gcnt.skywarsreloaded.wrapper.server;

public enum SWGameRule {

    ANNOUNCE_ADVANCEMENTS("announceAdvancements", Boolean.class),
    COMMAND_BLOCK_OUTPUT("commandBlockOutput", Boolean.class),
    DISABLE_ELYTRA_MOVEMENT_CHECK("disableElytraMovementCheck", Boolean.class),
    DO_DAYLIGHT_CYCLE("doDaylightCycle", Boolean.class),
    DO_ENTITY_DROPS("doEntityDrops", Boolean.class),
    DO_FIRE_TICK("doFireTick", Boolean.class),
    DO_LIMITED_CRAFTING("doLimitedCrafting", Boolean.class),
    DO_MOB_LOOT("doMobLoot", Boolean.class),
    DO_MOB_SPAWNING("doMobSpawning", Boolean.class),
    DO_TILE_DROPS("doTileDrops", Boolean.class),
    DO_WEATHER_CYCLE("doWeatherCycle", Boolean.class),
    KEEP_INVENTORY("keepInventory", Boolean.class),
    LOG_ADMIN_COMMANDS("logAdminCommands", Boolean.class),
    MOB_GRIEFING("mobGriefing", Boolean.class),
    NATURAL_REGENERATION("naturalRegeneration", Boolean.class),
    REDUCED_DEBUG_INFO("reducedDebugInfo", Boolean.class),
    SEND_COMMAND_FEEDBACK("sendCommandFeedback", Boolean.class),
    SHOW_DEATH_MESSAGES("showDeathMessages", Boolean.class),
    SPECTATORS_GENERATE_CHUNKS("spectatorsGenerateChunks", Boolean.class),
    DISABLE_RAIDS("disableRaids", Boolean.class),
    DO_INSOMNIA("doInsomnia", Boolean.class),
    DO_IMMEDIATE_RESPAWN("doImmediateRespawn", Boolean.class),
    DROWNING_DAMAGE("drowningDamage", Boolean.class),
    FALL_DAMAGE("fallDamage", Boolean.class),
    FIRE_DAMAGE("fireDamage", Boolean.class),
    FREEZE_DAMAGE("freezeDamage", Boolean.class),
    DO_PATROL_SPAWNING("doPatrolSpawning", Boolean.class),
    DO_TRADER_SPAWNING("doTraderSpawning", Boolean.class),
    FORGIVE_DEAD_PLAYERS("forgiveDeadPlayers", Boolean.class),
    UNIVERSAL_ANGER("universalAnger", Boolean.class),
    RANDOM_TICK_SPEED("randomTickSpeed", Integer.class),
    SPAWN_RADIUS("spawnRadius", Integer.class),
    MAX_ENTITY_CRAMMING("maxEntityCramming", Integer.class),
    MAX_COMMAND_CHAIN_LENGTH("maxCommandChainLength", Integer.class),
    PLAYERS_SLEEPING_PERCENTAGE("playersSleepingPercentage", Integer.class);

    private final String id;
    private final Class<?> valueType;

    SWGameRule(String idIn, Class<?> valueTypeIn) {
        this.id = idIn;
        this.valueType = valueTypeIn;
    }

    public String getMinecraftId() {
        return this.id;
    }

    public Class<?> getValueType() {
        return this.valueType;
    }
}
