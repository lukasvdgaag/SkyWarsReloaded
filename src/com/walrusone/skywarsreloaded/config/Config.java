package com.walrusone.skywarsreloaded.config;

import com.google.common.collect.Lists;
import com.walrusone.skywarsreloaded.SkyWarsReloaded;
import com.walrusone.skywarsreloaded.enums.LeaderType;
import com.walrusone.skywarsreloaded.utilities.Util;
import org.bukkit.Location;
import org.bukkit.Material;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

public class Config {

    private final List<String> itemNames = Arrays.asList("kitvote", "votingItem", "teamSelectItem",
            "exitMenuItem", "nextPageItem", "prevPageItem",
            "exitGameItem",
            "chestvote", "chestrandom", "chestbasic", "chestnormal", "chestop", "chestscavenger",
            "healthvote", "healthrandom", "healthfive", "healthten", "healthfifteen", "healthtwenty",
            "nopermission",
            "timevote", "timerandom", "timedawn", "timenoon", "timedusk", "timemidnight",
            "weathervote", "weatherrandom", "weathersunny", "weatherrain", "weatherstorm", "weathersnow",
            "modifiervote", "modifierrandom", "modifierspeed", "modifierjump", "modifierstrength", "modifiernone",
            "joinselect",
            "singlemenu",
            "teammenu",
            "spectateselect",
            "optionselect",
            "particleselect",
            "projectileselect",
            "killsoundselect", "killsounditem",
            "winsoundselect",
            "glassselect", "tauntselect");
    private final List<String> defItems13 = Arrays.asList("ENDER_EYE", "COMPASS", "END_CRYSTAL",
            "BARRIER", "FEATHER", "FEATHER",
            "IRON_DOOR",
            "SHIELD", "NETHER_STAR", "STONE_SWORD", "IRON_SWORD", "DIAMOND_SWORD", "WOODEN_HOE",
            "EXPERIENCE_BOTTLE", "NETHER_STAR", "REDSTONE", "REDSTONE", "REDSTONE", "REDSTONE",
            "BARRIER",
            "CLOCK", "NETHER_STAR", "CLOCK", "CLOCK", "CLOCK", "CLOCK",
            "BLAZE_POWDER", "NETHER_STAR", "PRISMARINE_SHARD", "PRISMARINE_SHARD", "PRISMARINE_SHARD", "PRISMARINE_SHARD",
            "DRAGON_BREATH", "NETHER_STAR", "BOOK", "BOOK", "BOOK", "BOOK",
            "DIAMOND_HELMET",
            "REDSTONE_TORCH",
            "COMPARATOR",
            "LEATHER_HELMET",
            "ENDER_EYE",
            "BLAZE_POWDER",
            "ARROW",
            "DIAMOND_SWORD", "NOTE_BLOCK",
            "DRAGON_EGG",
            "GLASS", "SHIELD");
    private final List<String> defItems12 = Arrays.asList("EYE_OF_ENDER", "COMPASS", "END_CRYSTAL",
            "BARRIER", "FEATHER", "FEATHER",
            "IRON_DOOR",
            "SHIELD", "NETHER_STAR", "STONE_SWORD", "IRON_SWORD", "DIAMOND_SWORD", "WOOD_HOE",
            "EXP_BOTTLE", "NETHER_STAR", "REDSTONE", "REDSTONE", "REDSTONE", "REDSTONE",
            "BARRIER",
            "WATCH", "NETHER_STAR", "WATCH", "WATCH", "WATCH", "WATCH",
            "BLAZE_POWDER", "NETHER_STAR", "PRISMARINE_SHARD", "PRISMARINE_SHARD", "PRISMARINE_SHARD", "PRISMARINE_SHARD",
            "DRAGONS_BREATH", "NETHER_STAR", "BOOK", "BOOK", "BOOK", "BOOK",
            "DIAMOND_HELMET",
            "REDSTONE_TORCH_OFF",
            "REDSTONE_COMPARATOR",
            "LEATHER_HELMET",
            "EYE_OF_ENDER",
            "BLAZE_POWDER",
            "ARROW",
            "DIAMOND_SWORD", "NOTE_BLOCK",
            "DRAGON_EGG",
            "STAINED_GLASS", "SHIELD");
    private final List<String> defItems8 = Arrays.asList("EYE_OF_ENDER", "COMPASS", "WATCH",
            "BARRIER", "FEATHER", "FEATHER",
            "IRON_DOOR",
            "DIAMOND", "NETHER_STAR", "STONE_SWORD", "IRON_SWORD", "DIAMOND_SWORD", "WOOD_HOE",
            "EXP_BOTTLE", "NETHER_STAR", "REDSTONE", "REDSTONE", "REDSTONE", "REDSTONE",
            "BARRIER",
            "WATCH", "NETHER_STAR", "WATCH", "WATCH", "WATCH", "WATCH",
            "BLAZE_POWDER", "NETHER_STAR", "PRISMARINE_SHARD", "PRISMARINE_SHARD", "PRISMARINE_SHARD", "PRISMARINE_SHARD",
            "DRAGON_EGG", "NETHER_STAR", "BOOK", "BOOK", "BOOK", "BOOK",
            "DIAMOND_HELMET",
            "REDSTONE_TORCH_OFF",
            "REDSTONE_COMPARATOR",
            "LEATHER_HELMET",
            "EYE_OF_ENDER",
            "BLAZE_POWDER",
            "ARROW",
            "DIAMOND_SWORD", "NOTE_BLOCK",
            "DRAGON_EGG",
            "STAINED_GLASS", "DRAGON_EGG");
    private final List<String> signItems = Arrays.asList("blockoffline", "blockwaiting", "blockplaying", "blockending", "almostfull", "threefull", "halffull", "almostempty");
    private final List<String> signDef8 = Arrays.asList("COAL_BLOCK", "EMERALD_BLOCK", "REDSTONE_BLOCK", "LAPIS_BLOCK", "DIAMOND_HELMET", "GOLD_HELMET", "IRON_HELMET", "LEATHER_HELMET");
    private final List<String> signDef13 = Arrays.asList("COAL_BLOCK", "EMERALD_BLOCK", "REDSTONE_BLOCK", "LAPIS_BLOCK", "DIAMOND_HELMET", "GOLDEN_HELMET", "IRON_HELMET", "LEATHER_HELMET");
    private boolean debug;
    private boolean bungeeMode;
    private boolean economyEnabled;
    private String bungeeLobby;
    private List<String> gameEndCommands;
    private boolean resetTimerOnJoin;
    private String resourcePack;
    private boolean promptResource;
    private int kitvotepos;
    private boolean kitsEnabled;
    private int votepos;
    private boolean voteEnabled;
    private int exitpos;
    private int teamSelectPos;

    private boolean clearInventoryOnLobbyJoin;

    private boolean randomOptionVoteEnabled;
    private boolean scavengerChestEnabled;
    private boolean joinGameItemEnabled;
    private boolean spectateGameItemEnabled;
    private boolean optionsGameItemEnabled;

    private int chestvotepos;
    private boolean chestVoteEnabled;
    private int healthvotepos;
    private boolean healthVoteEnabled;
    private int timevotepos;
    private boolean timeVoteEnabled;
    private int weathervotepos;
    private boolean weatherVoteEnabled;
    private int modifiervotepos;
    private boolean modifierVoteEnabled;
    private int particleselectslot;
    private int projectileselectslot;
    private int killsoundselectslot;
    private int winsoundselectslot;
    private int glassselectslot;
    private int tauntselectslot;
    private int leaderSize;
    private boolean leaderSignsEnabled;
    private boolean leaderHeadsEnabled;
    private int leaderboardUpdateInterval;
    private boolean winsEnabled;
    private boolean lossesEnabled;
    private boolean killsEnabled;
    private boolean deathsEnabled;
    private boolean xpEnabled;
    private boolean lobbyBoardEnabled;
    private boolean protectlobby;
    private boolean displayPlayerExeperience;
    private boolean borderEnabled;
    private int borderSize;
    private boolean showHealth;
    private int winnerEco;
    private int killerEco;
    private int snowballDamage;
    private int eggDamage;
    private int winnerXP;
    private List<String> winCommands;
    private int killerXP;
    private List<String> killCommands;
    private int vip1;
    private int vip2;
    private int vip3;
    private int vip4;
    private int vip5;
    private boolean tauntsEnabled;
    private boolean titlesEnabled;
    private boolean kitVotingEnabled;
    private int waitTimer;
    private int strength;
    private int speed;
    private int jump;

    private boolean usePlayerNames;
    private boolean usePlayerGlassColors;
    private String teamMaterial;
    private boolean useTeamMaterialBytes;
    private int standardTeamMaterialByte;
    private boolean useSeparateCages;
    private boolean changeTablistNames;
    private boolean useTeamNumberInMenu;

    private int timeAfterMatch;
    private boolean fireworksEnabled;
    private int fireworksPer5Tick;
    private int maxMapSize;
    private Location spawn;
    private boolean lookDirectionEnabled;
    private boolean pressurePlate;
    private boolean teleportOnJoin;
    private boolean teleportOnWorldEnter;
    private int maxPartySize;
    private boolean partyEnabled;
    private List<String> lobbyWorlds;
    private boolean loadTrappedChestsAsCenter;
    private int maxChest;
    private int maxDoubleChest;
    private boolean useHolograms;
    private int cooldown;
    private int kitMenuSize;
    private int randPos;
    private int noKitPos;
    private String randMat;
    private String noKitMat;
    private boolean particlesEnabled;
    private int ticksPerUpdate;
    private boolean joinEnabled;
    private int joinSlot;
    private int singleSlot;
    private int teamSlot;
    private boolean spectateMenuEnabled;
    private int spectateSlot;
    private boolean optionsEnabled;
    private int optionsSlot;
    private boolean glassEnabled;
    private boolean particleEnabled;
    private boolean projectEnabled;
    private boolean killsoundEnabled;
    private boolean winsoundEnabled;
    private boolean tauntsMenuEnabled;
    private boolean playSounds;
    private String countdown;
    private String joinSound;
    private String leaveSound;
    private String openJoinMenu;
    private String openSpectateMenu;
    private String openOptionsMenu;
    private String openGlassMenu;
    private String openWinSoundMenu;
    private String openKillSoundMenu;
    private String openParticleMenu;
    private String openProjectileMenu;
    private String openTauntMenu;
    private String openKitMenu;
    private String openChestMenu;
    private String openTimeMenu;
    private String openWeatherMenu;
    private String openHealthMenu;
    private String openModifierMenu;
    private String confirmSelection;
    private String errorSound;
    private boolean spectateEnabled;
    private boolean disableCommands;
    private List<String> enabledCommands;
    private boolean disableCommandsSpectate;
    private List<String> enabledCommandsSpectate;
    private boolean useExternalChat;
    private boolean addPrefix;
    private boolean enableFormatter;
    private boolean limitGameChat;
    private boolean limitSpecChat;
    private boolean limitLobbyChat;
    private Map<String, String> materials = new HashMap<>();
    private boolean loading = false;

    private boolean enableWinMessage = true;
    private boolean enablePVPTimer = true;
    private int PVPTimerTime = 0;
    private boolean enableQuickDeath = true;
    private int quickDeathY = 0;
    private boolean kickOnWorldTeleport = true;

    private boolean clearInventoryOnWin = true;
    private boolean enableFlightOnWin = false;

    private List<String> gameServers = Lists.newArrayList();
    private boolean isLobbyServer = false;

    private boolean useSlimeWorldManager = false;
    private String slimeWorldManagerSource = "file";
    private boolean usePartyAndFriends = false;
    private boolean useTeamChat = true;
    private String timeFormat = "mm:ss";
    private boolean checkForBetaVersion = true;
    private boolean displayTimerOnLevelbar = true;

    public Config() {
        load();
    }

    public void load() {
        if (!loading) {
            loading = true;
            debug = SkyWarsReloaded.get().getConfig().getBoolean("debugMode");

            checkForBetaVersion = SkyWarsReloaded.get().getConfig().getBoolean("updater.checkForBetaVersions");

            gameServers = SkyWarsReloaded.get().getConfig().getStringList("gameServers");

            bungeeMode = SkyWarsReloaded.get().getConfig().getBoolean("bungeeMode");
            isLobbyServer = SkyWarsReloaded.get().getConfig().getBoolean("isLobbyServer");
            economyEnabled = SkyWarsReloaded.get().getConfig().getBoolean("economyEnabled");
            bungeeLobby = SkyWarsReloaded.get().getConfig().getString("bungeeLobby");
            gameEndCommands = SkyWarsReloaded.get().getConfig().getStringList("gameEndCommands");
            resourcePack = SkyWarsReloaded.get().getConfig().getString("resourcepack");
            promptResource = SkyWarsReloaded.get().getConfig().getBoolean("promptForResourcePackOnJoin");
            clearInventoryOnLobbyJoin = SkyWarsReloaded.get().getConfig().getBoolean("clearInventoryOnLobbyJoin");
            timeFormat = SkyWarsReloaded.get().getConfig().getString("timeFormat");

            lobbyBoardEnabled = SkyWarsReloaded.get().getConfig().getBoolean("lobbyBoardEnabled");
            protectlobby = SkyWarsReloaded.get().getConfig().getBoolean("enabledLobbyGuard");
            displayPlayerExeperience = SkyWarsReloaded.get().getConfig().getBoolean("displayPlayerLevelOnXpBar");
            leaderSize = SkyWarsReloaded.get().getConfig().getInt("leaderboards.length");
            leaderSignsEnabled = SkyWarsReloaded.get().getConfig().getBoolean("leaderboards.signsEnabled");
            leaderHeadsEnabled = SkyWarsReloaded.get().getConfig().getBoolean("leaderboards.headsEnabled");
            winsEnabled = SkyWarsReloaded.get().getConfig().getBoolean("leaderboards.winsLeaderboardEnabled");
            lossesEnabled = SkyWarsReloaded.get().getConfig().getBoolean("leaderboards.lossesLeaderboardEnabled");
            killsEnabled = SkyWarsReloaded.get().getConfig().getBoolean("leaderboards.killsLeaderboardEnabled");
            deathsEnabled = SkyWarsReloaded.get().getConfig().getBoolean("leaderboards.deathsLeaderboardEnabled");
            xpEnabled = SkyWarsReloaded.get().getConfig().getBoolean("leaderboards.xpLeaderboardEnabled");
            leaderboardUpdateInterval = SkyWarsReloaded.get().getConfig().getInt("leaderboards.leaderboardUpdateInterval");

            displayTimerOnLevelbar = SkyWarsReloaded.get().getConfig().getBoolean("game.displayTimerOnLevelbar");
            enableFlightOnWin = SkyWarsReloaded.get().getConfig().getBoolean("game.win.enableFlight");
            clearInventoryOnWin = SkyWarsReloaded.get().getConfig().getBoolean("game.win.clearInventory");
            kickOnWorldTeleport = SkyWarsReloaded.get().getConfig().getBoolean("game.kickOnWorldTeleport");
            enableQuickDeath = SkyWarsReloaded.get().getConfig().getBoolean("game.enableQuickDeath");
            quickDeathY = SkyWarsReloaded.get().getConfig().getInt("game.quickDeathY");
            enablePVPTimer = SkyWarsReloaded.get().getConfig().getBoolean("game.enablePVPTimer");
            PVPTimerTime = SkyWarsReloaded.get().getConfig().getInt("game.PVPTimerTime");
            enableWinMessage = SkyWarsReloaded.get().getConfig().getBoolean("game.enableWinMessage");
            borderEnabled = SkyWarsReloaded.get().getConfig().getBoolean("game.worldBorder.enabled");
            lookDirectionEnabled = SkyWarsReloaded.get().getConfig().getBoolean("game.enableLookDirection");
            borderSize = SkyWarsReloaded.get().getConfig().getInt("game.worldBorder.borderSize");
            showHealth = SkyWarsReloaded.get().getConfig().getBoolean("game.showHealth");
            winnerEco = SkyWarsReloaded.get().getConfig().getInt("game.ecoForWin");
            killerEco = SkyWarsReloaded.get().getConfig().getInt("game.ecoForKill");
            winnerXP = SkyWarsReloaded.get().getConfig().getInt("game.xpForWin");
            snowballDamage = SkyWarsReloaded.get().getConfig().getInt("game.snowballDamage");
            eggDamage = SkyWarsReloaded.get().getConfig().getInt("game.eggDamage");
            winCommands = SkyWarsReloaded.get().getConfig().getStringList("game.winCommands");
            killerXP = SkyWarsReloaded.get().getConfig().getInt("game.xpForKill");
            killCommands = SkyWarsReloaded.get().getConfig().getStringList("game.killCommands");
            vip1 = SkyWarsReloaded.get().getConfig().getInt("game.vip1Multiplier");
            vip2 = SkyWarsReloaded.get().getConfig().getInt("game.vip2Multiplier");
            vip3 = SkyWarsReloaded.get().getConfig().getInt("game.vip3Multiplier");
            vip4 = SkyWarsReloaded.get().getConfig().getInt("game.vip4Multiplier");
            vip5 = SkyWarsReloaded.get().getConfig().getInt("game.vip5Multiplier");
            spawn = Util.get().stringToLocation(SkyWarsReloaded.get().getConfig().getString("spawn"));
            debugTesting();
            timeAfterMatch = SkyWarsReloaded.get().getConfig().getInt("game.timeAfterMatch");
            fireworksPer5Tick = SkyWarsReloaded.get().getConfig().getInt("fireworks.per5Ticks");
            fireworksEnabled = SkyWarsReloaded.get().getConfig().getBoolean("fireworks.enabled");
            waitTimer = SkyWarsReloaded.get().getConfig().getInt("game.waitTimer");
            resetTimerOnJoin = SkyWarsReloaded.get().getConfig().getBoolean("game.resetTimerOnJoin");
            tauntsEnabled = SkyWarsReloaded.get().getConfig().getBoolean("game.tauntsEnabled");
            titlesEnabled = SkyWarsReloaded.get().getConfig().getBoolean("titles.enabled");
            kitVotingEnabled = SkyWarsReloaded.get().getConfig().getBoolean("game.kitVotingEnabled");
            spectateEnabled = SkyWarsReloaded.get().getConfig().getBoolean("game.spectateEnabled");
            maxMapSize = SkyWarsReloaded.get().getConfig().getInt("game.maxMapSize");
            pressurePlate = SkyWarsReloaded.get().getConfig().getBoolean("enablePressurePlateJoin");
            teleportOnJoin = SkyWarsReloaded.get().getConfig().getBoolean("teleportToSpawnOnJoin");
            teleportOnWorldEnter = SkyWarsReloaded.get().getConfig().getBoolean("teleportToSpawnOnWorldEnter");
            strength = SkyWarsReloaded.get().getConfig().getInt("game.modifierLevel.strength");
            speed = SkyWarsReloaded.get().getConfig().getInt("game.modifierLevel.speed");
            jump = SkyWarsReloaded.get().getConfig().getInt("game.modifierLevel.jump");

            useSlimeWorldManager = SkyWarsReloaded.get().getConfig().getBoolean("slimeworldmanager.enable");
            slimeWorldManagerSource = SkyWarsReloaded.get().getConfig().getString("slimeworldmanager.source");

            usePlayerNames = SkyWarsReloaded.get().getConfig().getBoolean("teams.usePlayerNames");
            usePlayerGlassColors = SkyWarsReloaded.get().getConfig().getBoolean("teams.usePlayerGlassColors");
            useTeamNumberInMenu = SkyWarsReloaded.get().getConfig().getBoolean("teams.useTeamNumberInMenu");
            teamMaterial = SkyWarsReloaded.get().getConfig().getString("teams.teamCageMaterial");
            standardTeamMaterialByte = SkyWarsReloaded.get().getConfig().getInt("teams.standardTeamMaterialByte");
            useTeamMaterialBytes = SkyWarsReloaded.get().getConfig().getBoolean("teams.useTeamMaterialBytes");
            if (useTeamMaterialBytes) {
                if (teamMaterial == null || (!teamMaterial.equalsIgnoreCase("wool") && !teamMaterial.equalsIgnoreCase("stained_glass") && !teamMaterial.equalsIgnoreCase("banner"))) {
                    teamMaterial = "STAINED_GLASS";
                }
            }
            useSeparateCages = SkyWarsReloaded.get().getConfig().getBoolean("teams.useSeparateCages");
            changeTablistNames = SkyWarsReloaded.get().getConfig().getBoolean("teams.changeTablistNames");

            maxPartySize = SkyWarsReloaded.get().getConfig().getInt("parties.maxPartySize");
            partyEnabled = SkyWarsReloaded.get().getConfig().getBoolean("parties.enabled");
            usePartyAndFriends = SkyWarsReloaded.get().getConfig().getBoolean("parties.enablePartyAndFriendsSupport");
            lobbyWorlds = SkyWarsReloaded.get().getConfig().getStringList("parties.lobbyWorlds");

            loadTrappedChestsAsCenter = SkyWarsReloaded.get().getConfig().getBoolean("chests.loadTrappedChestsAsCenter");
            maxChest = SkyWarsReloaded.get().getConfig().getInt("chests.maxItemsChest");
            maxDoubleChest = SkyWarsReloaded.get().getConfig().getInt("chests.maxItemsDoubleChest");

            useHolograms = SkyWarsReloaded.get().getConfig().getBoolean("holograms.enabled");

            boolean requireSave = false;

            if (spawn != null) {
                if (lobbyWorlds == null) {
                    lobbyWorlds = Lists.newArrayList();
                } else {
                    String world = SkyWarsReloaded.get().getConfig().getString("spawn").split(":")[0];
                    if (!lobbyWorlds.contains(world)) {
                        lobbyWorlds.add(world);

                        requireSave = true;
                    }
                }
            }

            randomOptionVoteEnabled = SkyWarsReloaded.get().getConfig().getBoolean("items.randomVoteEnabled");
            scavengerChestEnabled = SkyWarsReloaded.get().getConfig().getBoolean("items.scavengerChestEnabled");
            joinGameItemEnabled = SkyWarsReloaded.get().getConfig().getBoolean("items.joinGameItemEnabled");
            spectateGameItemEnabled = SkyWarsReloaded.get().getConfig().getBoolean("items.spectateGameItemEnabled");
            optionsGameItemEnabled = SkyWarsReloaded.get().getConfig().getBoolean("items.optionsItemEnabled");

            teamSelectPos = SkyWarsReloaded.get().getConfig().getInt("items.teamSelectPosition");
            kitvotepos = SkyWarsReloaded.get().getConfig().getInt("items.kitVotePosition");
            kitsEnabled = SkyWarsReloaded.get().getConfig().getBoolean("items.kitsEnabled");
            votepos = SkyWarsReloaded.get().getConfig().getInt("items.votingPosition");
            voteEnabled = SkyWarsReloaded.get().getConfig().getBoolean("items.voteEnabled");
            exitpos = SkyWarsReloaded.get().getConfig().getInt("items.exitPosition");
            chestvotepos = SkyWarsReloaded.get().getConfig().getInt("items.chestVotePosition");
            chestVoteEnabled = SkyWarsReloaded.get().getConfig().getBoolean("items.chestVoteEnabled");
            healthvotepos = SkyWarsReloaded.get().getConfig().getInt("items.healthVotePosition");
            healthVoteEnabled = SkyWarsReloaded.get().getConfig().getBoolean("items.healthVoteEnabled");
            timevotepos = SkyWarsReloaded.get().getConfig().getInt("items.timeVotePosition");
            timeVoteEnabled = SkyWarsReloaded.get().getConfig().getBoolean("items.timeVoteEnabled");
            weathervotepos = SkyWarsReloaded.get().getConfig().getInt("items.weatherVotePosition");
            weatherVoteEnabled = SkyWarsReloaded.get().getConfig().getBoolean("items.weatherVoteEnabled");
            modifiervotepos = SkyWarsReloaded.get().getConfig().getInt("items.modifierVotePosition");
            modifierVoteEnabled = SkyWarsReloaded.get().getConfig().getBoolean("items.modifierVoteEnabled");
            particleselectslot = SkyWarsReloaded.get().getConfig().getInt("items.particleselectslot");
            projectileselectslot = SkyWarsReloaded.get().getConfig().getInt("items.projectileselectslot");
            killsoundselectslot = SkyWarsReloaded.get().getConfig().getInt("items.killsoundselectslot");
            winsoundselectslot = SkyWarsReloaded.get().getConfig().getInt("items.winsoundselectslot");
            glassselectslot = SkyWarsReloaded.get().getConfig().getInt("items.glassselectslot");
            tauntselectslot = SkyWarsReloaded.get().getConfig().getInt("items.tauntselectslot");

            cooldown = SkyWarsReloaded.get().getConfig().getInt("tauntCooldown");

            randPos = SkyWarsReloaded.get().getConfig().getInt("kit.randPos");
            kitMenuSize = SkyWarsReloaded.get().getConfig().getInt("kit.menuSize");
            noKitPos = SkyWarsReloaded.get().getConfig().getInt("kit.noKitPos");
            randMat = SkyWarsReloaded.get().getConfig().getString("kit.randItem");
            noKitMat = SkyWarsReloaded.get().getConfig().getString("kit.noKitItem");

            particlesEnabled = SkyWarsReloaded.get().getConfig().getBoolean("particles.enabled");
            ticksPerUpdate = SkyWarsReloaded.get().getConfig().getInt("particles.ticksperupdate");

            spectateMenuEnabled = SkyWarsReloaded.get().getConfig().getBoolean("enabledMenus.spectate");
            spectateSlot = SkyWarsReloaded.get().getConfig().getInt("enabledMenus.spectateSlot");
            joinEnabled = SkyWarsReloaded.get().getConfig().getBoolean("enabledMenus.join");
            joinSlot = SkyWarsReloaded.get().getConfig().getInt("enabledMenus.joinSlot");
            singleSlot = SkyWarsReloaded.get().getConfig().getInt("items.singleSlot");
            teamSlot = SkyWarsReloaded.get().getConfig().getInt("items.teamSlot");
            optionsEnabled = SkyWarsReloaded.get().getConfig().getBoolean("enabledMenus.options");
            optionsSlot = SkyWarsReloaded.get().getConfig().getInt("enabledMenus.optionsSlot");
            glassEnabled = SkyWarsReloaded.get().getConfig().getBoolean("enabledMenus.glass");
            particleEnabled = SkyWarsReloaded.get().getConfig().getBoolean("enabledMenus.particle");
            projectEnabled = SkyWarsReloaded.get().getConfig().getBoolean("enabledMenus.projectile");
            killsoundEnabled = SkyWarsReloaded.get().getConfig().getBoolean("enabledMenus.killsound");
            winsoundEnabled = SkyWarsReloaded.get().getConfig().getBoolean("enabledMenus.winsound");
            tauntsMenuEnabled = SkyWarsReloaded.get().getConfig().getBoolean("enabledMenus.taunts");

            playSounds = SkyWarsReloaded.get().getConfig().getBoolean("sounds.enabled");
            countdown = SkyWarsReloaded.get().getConfig().getString("sounds.countdown");
            joinSound = SkyWarsReloaded.get().getConfig().getString("sounds.join");
            leaveSound = SkyWarsReloaded.get().getConfig().getString("sounds.leave");
            openJoinMenu = SkyWarsReloaded.get().getConfig().getString("sounds.openJoinMenu");
            openSpectateMenu = SkyWarsReloaded.get().getConfig().getString("sounds.openSpectateMenu");
            openOptionsMenu = SkyWarsReloaded.get().getConfig().getString("sounds.openOptionsMenu");
            openGlassMenu = SkyWarsReloaded.get().getConfig().getString("sounds.openGlassMenu");
            openWinSoundMenu = SkyWarsReloaded.get().getConfig().getString("sounds.openWinSoundMenu");
            openKillSoundMenu = SkyWarsReloaded.get().getConfig().getString("sounds.openKillSoundMenu");
            openParticleMenu = SkyWarsReloaded.get().getConfig().getString("sounds.openParticleMenu");
            openProjectileMenu = SkyWarsReloaded.get().getConfig().getString("sounds.openProjectileMenu");
            openTauntMenu = SkyWarsReloaded.get().getConfig().getString("sounds.openTauntMenu");
            openKitMenu = SkyWarsReloaded.get().getConfig().getString("sounds.openKitMenu");
            openChestMenu = SkyWarsReloaded.get().getConfig().getString("sounds.openChestMenu");
            openHealthMenu = SkyWarsReloaded.get().getConfig().getString("sounds.openHealthMenu");
            openTimeMenu = SkyWarsReloaded.get().getConfig().getString("sounds.openTimeMenu");
            openWeatherMenu = SkyWarsReloaded.get().getConfig().getString("sounds.openWeatherMenu");
            openModifierMenu = SkyWarsReloaded.get().getConfig().getString("sounds.openModifierMenu");
            confirmSelection = SkyWarsReloaded.get().getConfig().getString("sounds.confirmSelectionSound");
            errorSound = SkyWarsReloaded.get().getConfig().getString("sounds.errorSound");

            enabledCommands = SkyWarsReloaded.get().getConfig().getStringList("disable-commands.exceptions");
            disableCommands = SkyWarsReloaded.get().getConfig().getBoolean("disable-commands.enabled");

            disableCommandsSpectate = SkyWarsReloaded.get().getConfig().getBoolean("disable-commands-spectate.enabled");
            enabledCommandsSpectate = SkyWarsReloaded.get().getConfig().getStringList("disable-commands-spectate.exceptions");

            useExternalChat = SkyWarsReloaded.get().getConfig().getBoolean("chat.externalChat.useExternalChat");
            addPrefix = SkyWarsReloaded.get().getConfig().getBoolean("chat.externalChat.addPrefix");
            enableFormatter = SkyWarsReloaded.get().getConfig().getBoolean("chat.enableFormatter");
            limitGameChat = SkyWarsReloaded.get().getConfig().getBoolean("chat.limitGameChatToGame");
            limitSpecChat = SkyWarsReloaded.get().getConfig().getBoolean("chat.limitSpecChatToSpec");
            limitLobbyChat = SkyWarsReloaded.get().getConfig().getBoolean("chat.limitLobbyChatToLobby");
            useTeamChat = SkyWarsReloaded.get().getConfig().getBoolean("chat.useTeamChat");

            for (int i = 0; i < itemNames.size(); i++) {
                String name = itemNames.get(i);
                String def;
                if (SkyWarsReloaded.getNMS().getVersion() < 9) {
                    def = defItems8.get(i);
                } else if (SkyWarsReloaded.getNMS().getVersion() > 8 && SkyWarsReloaded.getNMS().getVersion() < 13) {
                    def = defItems12.get(i);
                } else {
                    def = defItems13.get(i);
                }
                addMaterial(name, SkyWarsReloaded.get().getConfig().getString("items." + name), def);
            }

            for (int i = 0; i < signItems.size(); i++) {
                String name = signItems.get(i);
                String def;
                if (SkyWarsReloaded.getNMS().getVersion() < 13) {
                    def = signDef8.get(i);
                } else {
                    def = signDef13.get(i);
                }

                addMaterial(name, SkyWarsReloaded.get().getConfig().getString("signs." + name), def);
            }

            if (requireSave) {
                save();
            }
        }
        loading = false;
    }

    private void debugTesting() {
        if (debugEnabled()) {
            Logger log = SkyWarsReloaded.get().getLogger();
            //log.info("[DEBUG] spawn.x null? " + String.join(", ", SkyWarsReloaded.get().getServer().getWorlds()));
            //if (t3) return;
            boolean t1 = getSpawn() == null;
            log.info("[DEBUG] spawn null? " + t1);
            if (t1) return;
            boolean t2 = getSpawn().getWorld() == null;
            log.info("[DEBUG] spawn.world null? " + t2);
            if (t2) return;
        }
    }

    private void addMaterial(String key, String mat, String def) {
        int data = -1;
        String matWithData = "";
        String[] matParts = mat.split(":");
        if (matParts.length == 2) {
            matWithData = matParts[0];
            data = Integer.parseInt(matParts[1]);
        }
        Material material;
        if (data != -1) {
            material = Material.matchMaterial(matWithData);
        } else {
            material = Material.matchMaterial(mat);
        }
        if (material == null) {
            materials.put(key, def);
        } else {
            materials.put(key, mat);
        }
    }

    public void save() {
        SkyWarsReloaded.get().getConfig().set("debugMode", debug);
        SkyWarsReloaded.get().getConfig().set("gameServers", gameServers);
        SkyWarsReloaded.get().getConfig().set("isLobbyServer", isLobbyServer);

        SkyWarsReloaded.get().getConfig().set("updater.checkForBetaVersions", checkForBetaVersion);

        if (spawn != null) {
            SkyWarsReloaded.get().getConfig().set("spawn", Util.get().locationToString(spawn));
        }

        SkyWarsReloaded.get().getConfig().set("economyEnabled", economyEnabled);
        SkyWarsReloaded.get().getConfig().set("bungeeMode", bungeeMode);
        SkyWarsReloaded.get().getConfig().set("bungeeLobby", bungeeLobby);
        SkyWarsReloaded.get().getConfig().set("gameEndCommands", gameEndCommands);

        SkyWarsReloaded.get().getConfig().set("resourcepack", resourcePack);
        SkyWarsReloaded.get().getConfig().set("promptForResourcePackOnJoin", promptResource);
        SkyWarsReloaded.get().getConfig().set("clearInventoryOnLobbyJoin", clearInventoryOnLobbyJoin);
        SkyWarsReloaded.get().getConfig().set("timeFormat", timeFormat);

        SkyWarsReloaded.get().getConfig().set("lobbyBoardEnabled", lobbyBoardEnabled);
        SkyWarsReloaded.get().getConfig().set("lobbyBoardEnabled", lobbyBoardEnabled);
        SkyWarsReloaded.get().getConfig().set("enabledLobbyGuard", protectlobby);
        SkyWarsReloaded.get().getConfig().set("leaderboards.length", leaderSize);
        SkyWarsReloaded.get().getConfig().set("leaderboards.signsEnabled", leaderSignsEnabled);
        SkyWarsReloaded.get().getConfig().set("leaderboards.headsEnabled", leaderHeadsEnabled);
        SkyWarsReloaded.get().getConfig().set("leaderboards.winsLeaderboardEnabled", winsEnabled);
        SkyWarsReloaded.get().getConfig().set("leaderboards.lossesLeaderboardEnabled", lossesEnabled);
        SkyWarsReloaded.get().getConfig().set("leaderboards.killsLeaderboardEnabled", killsEnabled);
        SkyWarsReloaded.get().getConfig().set("leaderboards.deathsLeaderboardEnabled", deathsEnabled);
        SkyWarsReloaded.get().getConfig().set("leaderboards.xpLeaderboardEnabled", xpEnabled);
        SkyWarsReloaded.get().getConfig().set("leaderboards.leaderboardUpdateInterval", leaderboardUpdateInterval);

        SkyWarsReloaded.get().getConfig().set("game.displayTimerOnLevelbar", displayTimerOnLevelbar);
        SkyWarsReloaded.get().getConfig().set("game.win.clearInventory", clearInventoryOnWin);
        SkyWarsReloaded.get().getConfig().set("game.win.enableFlight", enableFlightOnWin);
        SkyWarsReloaded.get().getConfig().set("game.kickOnWorldTeleport", kickOnWorldTeleport);
        SkyWarsReloaded.get().getConfig().set("game.enableQuickDeath", enableQuickDeath);
        SkyWarsReloaded.get().getConfig().set("game.quickDeathY", quickDeathY);
        SkyWarsReloaded.get().getConfig().set("game.enablePVPTimer", enablePVPTimer);
        SkyWarsReloaded.get().getConfig().set("game.PVPTimerTime", PVPTimerTime);
        SkyWarsReloaded.get().getConfig().set("game.enableWinMessage", enableWinMessage);
        SkyWarsReloaded.get().getConfig().set("game.enableLookDirection", lookDirectionEnabled);
        SkyWarsReloaded.get().getConfig().set("game.worldBorder.enabled", borderEnabled);
        SkyWarsReloaded.get().getConfig().set("game.worldBorder.borderSize", borderSize);
        SkyWarsReloaded.get().getConfig().set("game.showHealth", showHealth);
        SkyWarsReloaded.get().getConfig().set("game.ecoForWin", winnerEco);
        SkyWarsReloaded.get().getConfig().set("game.ecoForKill", killerEco);
        SkyWarsReloaded.get().getConfig().set("game.snowballDamage", snowballDamage);
        SkyWarsReloaded.get().getConfig().set("game.eggDamage", eggDamage);
        SkyWarsReloaded.get().getConfig().set("game.xpForWin", winnerXP);
        SkyWarsReloaded.get().getConfig().set("game.winCommands", winCommands);
        SkyWarsReloaded.get().getConfig().set("game.xpForKill", killerXP);
        SkyWarsReloaded.get().getConfig().set("game.killCommands", killCommands);
        SkyWarsReloaded.get().getConfig().set("game.vip1Multiplier", vip1);
        SkyWarsReloaded.get().getConfig().set("game.vip2Multiplier", vip2);
        SkyWarsReloaded.get().getConfig().set("game.vip3Multiplier", vip3);
        SkyWarsReloaded.get().getConfig().set("game.vip4Multiplier", vip4);
        SkyWarsReloaded.get().getConfig().set("game.vip5Multiplier", vip5);
        SkyWarsReloaded.get().getConfig().set("titles.enabled", titlesEnabled);
        SkyWarsReloaded.get().getConfig().set("game.waitTimer", waitTimer);
        SkyWarsReloaded.get().getConfig().set("game.resetTimerOnJoin", resetTimerOnJoin);
        SkyWarsReloaded.get().getConfig().set("game.timeAfterMatch", timeAfterMatch);
        SkyWarsReloaded.get().getConfig().set("fireworks.per5Ticks", fireworksPer5Tick);
        SkyWarsReloaded.get().getConfig().set("fireworks.enabled", fireworksEnabled);
        SkyWarsReloaded.get().getConfig().set("game.spectateEnabled", spectateEnabled);
        SkyWarsReloaded.get().getConfig().set("game.maxMapSize", maxMapSize);
        SkyWarsReloaded.get().getConfig().set("game.tauntsEnabled", tauntsEnabled);
        SkyWarsReloaded.get().getConfig().set("enablePressurePlateJoin", pressurePlate);
        SkyWarsReloaded.get().getConfig().set("teleportToSpawnOnJoin", teleportOnJoin);
        SkyWarsReloaded.get().getConfig().set("teleportToSpawnOnWorldEnter", teleportOnWorldEnter);
        SkyWarsReloaded.get().getConfig().set("game.kitVotingEnabled", kitVotingEnabled);
        SkyWarsReloaded.get().getConfig().set("game.modifierLevel.strength", strength);
        SkyWarsReloaded.get().getConfig().set("game.modifierLevel.speed", speed);
        SkyWarsReloaded.get().getConfig().set("game.modifierLevel.jump", jump);

        SkyWarsReloaded.get().getConfig().set("slimeworldmanager.enable", useSlimeWorldManager);
        SkyWarsReloaded.get().getConfig().set("slimeworldmanager.source", slimeWorldManagerSource);

        SkyWarsReloaded.get().getConfig().set("teams.usePlayerNames", usePlayerNames);
        SkyWarsReloaded.get().getConfig().set("teams.usePlayerGlassColors", usePlayerGlassColors);
        SkyWarsReloaded.get().getConfig().set("teams.useTeamNumberInMenu", useTeamNumberInMenu);
        SkyWarsReloaded.get().getConfig().set("teams.teamCageMaterial", teamMaterial.toUpperCase());
        SkyWarsReloaded.get().getConfig().set("teams.standardTeamMaterialByte", standardTeamMaterialByte);
        SkyWarsReloaded.get().getConfig().set("teams.useTeamMaterialBytes", useTeamMaterialBytes);
        SkyWarsReloaded.get().getConfig().set("teams.useSeparateCages", useSeparateCages);
        SkyWarsReloaded.get().getConfig().set("teams.changeTablistNames", changeTablistNames);

        SkyWarsReloaded.get().getConfig().set("parties.maxPartySize", maxPartySize);
        SkyWarsReloaded.get().getConfig().set("parties.enabled", partyEnabled);
        SkyWarsReloaded.get().getConfig().set("parties.enablePartyAndFriendsSupport", usePartyAndFriends);
        SkyWarsReloaded.get().getConfig().set("parties.lobbyWorlds", lobbyWorlds);

        SkyWarsReloaded.get().getConfig().set("chests.loadTrappedChestsAsCenter", loadTrappedChestsAsCenter);
        SkyWarsReloaded.get().getConfig().set("chests.maxItemsChest", maxChest);
        SkyWarsReloaded.get().getConfig().set("chests.maxItemsDoubleChest", maxDoubleChest);


        SkyWarsReloaded.get().getConfig().set("holograms.enabled", useHolograms);

        SkyWarsReloaded.get().getConfig().set("items.randomVoteEnabled", randomOptionVoteEnabled);
        SkyWarsReloaded.get().getConfig().set("items.scavengerChestEnabled", scavengerChestEnabled);
        SkyWarsReloaded.get().getConfig().set("items.joinGameItemEnabled", joinGameItemEnabled);
        SkyWarsReloaded.get().getConfig().set("items.spectateGameItemEnabled", spectateGameItemEnabled);
        SkyWarsReloaded.get().getConfig().set("items.optionsItemEnabled", optionsGameItemEnabled);

        SkyWarsReloaded.get().getConfig().set("items.teamSelectPosition", teamSelectPos);
        SkyWarsReloaded.get().getConfig().set("items.kitVotePosition", kitvotepos);
        SkyWarsReloaded.get().getConfig().set("items.kitsEnabled", kitsEnabled);
        SkyWarsReloaded.get().getConfig().set("items.votingPosition", votepos);
        SkyWarsReloaded.get().getConfig().set("items.voteEnabled", voteEnabled);
        SkyWarsReloaded.get().getConfig().set("items.exitPosition", exitpos);
        SkyWarsReloaded.get().getConfig().set("items.chestVotePosition", chestvotepos);
        SkyWarsReloaded.get().getConfig().set("items.chestVoteEnabled", chestVoteEnabled);
        SkyWarsReloaded.get().getConfig().set("items.healthVotePosition", healthvotepos);
        SkyWarsReloaded.get().getConfig().set("items.healthVoteEnabled", healthVoteEnabled);
        SkyWarsReloaded.get().getConfig().set("items.timeVotePosition", timevotepos);
        SkyWarsReloaded.get().getConfig().set("items.timeVoteEnabled", timeVoteEnabled);
        SkyWarsReloaded.get().getConfig().set("items.weatherVotePosition", weathervotepos);
        SkyWarsReloaded.get().getConfig().set("items.weatherVoteEnabled", weatherVoteEnabled);
        SkyWarsReloaded.get().getConfig().set("items.modifierVotePosition", modifiervotepos);
        SkyWarsReloaded.get().getConfig().set("items.modifierVoteEnabled", modifierVoteEnabled);
        SkyWarsReloaded.get().getConfig().set("items.particleselectslot", particleselectslot);
        SkyWarsReloaded.get().getConfig().set("items.projectileselectslot", projectileselectslot);
        SkyWarsReloaded.get().getConfig().set("items.killsoundselectslot", killsoundselectslot);
        SkyWarsReloaded.get().getConfig().set("items.winsoundselectslot", winsoundselectslot);
        SkyWarsReloaded.get().getConfig().set("items.glassselectslot", glassselectslot);
        SkyWarsReloaded.get().getConfig().set("items.tauntselectslot", tauntselectslot);

        SkyWarsReloaded.get().getConfig().set("tauntCooldown", cooldown);

        SkyWarsReloaded.get().getConfig().set("kit.randPos", randPos);
        SkyWarsReloaded.get().getConfig().set("kit.menuSize", kitMenuSize);
        SkyWarsReloaded.get().getConfig().set("kit.noKitPos", noKitPos);
        SkyWarsReloaded.get().getConfig().set("kit.randItem", randMat);
        SkyWarsReloaded.get().getConfig().set("kit.noKitItem", noKitMat);

        SkyWarsReloaded.get().getConfig().set("particles.enabled", particlesEnabled);
        SkyWarsReloaded.get().getConfig().set("particles.ticksperupdate", ticksPerUpdate);

        SkyWarsReloaded.get().getConfig().set("enabledMenus.spectate", spectateMenuEnabled);
        SkyWarsReloaded.get().getConfig().set("enabledMenus.spectateSlot", spectateSlot);
        SkyWarsReloaded.get().getConfig().set("enabledMenus.join", joinEnabled);
        SkyWarsReloaded.get().getConfig().set("enabledMenus.joinSlot", joinSlot);
        SkyWarsReloaded.get().getConfig().set("items.singleSlot", singleSlot);
        SkyWarsReloaded.get().getConfig().set("items.teamSlot", teamSlot);
        SkyWarsReloaded.get().getConfig().set("enabledMenus.options", optionsEnabled);
        SkyWarsReloaded.get().getConfig().set("enabledMenus.optionsSlot", optionsSlot);
        SkyWarsReloaded.get().getConfig().set("enabledMenus.glass", glassEnabled);
        SkyWarsReloaded.get().getConfig().set("enabledMenus.particle", particlesEnabled);
        SkyWarsReloaded.get().getConfig().set("enabledMenus.projectile", projectEnabled);
        SkyWarsReloaded.get().getConfig().set("enabledMenus.killsound", killsoundEnabled);
        SkyWarsReloaded.get().getConfig().set("enabledMenus.winsound", winsoundEnabled);
        SkyWarsReloaded.get().getConfig().set("enabledMenus.taunts", tauntsMenuEnabled);

        SkyWarsReloaded.get().getConfig().set("sounds.enabled", playSounds);
        SkyWarsReloaded.get().getConfig().set("sounds.countdown", countdown);
        SkyWarsReloaded.get().getConfig().set("sounds.join", joinSound);
        SkyWarsReloaded.get().getConfig().set("sounds.leave", leaveSound);
        SkyWarsReloaded.get().getConfig().set("sounds.openJoinMenu", openJoinMenu);
        SkyWarsReloaded.get().getConfig().set("sounds.openSpectateMenu", openSpectateMenu);
        SkyWarsReloaded.get().getConfig().set("sounds.openOptionsMenu", openOptionsMenu);
        SkyWarsReloaded.get().getConfig().set("sounds.openGlassMenu", openGlassMenu);
        SkyWarsReloaded.get().getConfig().set("sounds.openWinSoundMenu", openWinSoundMenu);
        SkyWarsReloaded.get().getConfig().set("sounds.openKillSoundMenu", openKillSoundMenu);
        SkyWarsReloaded.get().getConfig().set("sounds.openParticleMenu", openParticleMenu);
        SkyWarsReloaded.get().getConfig().set("sounds.openProjectileMenu", openProjectileMenu);
        SkyWarsReloaded.get().getConfig().set("sounds.openTauntMenu", openTauntMenu);
        SkyWarsReloaded.get().getConfig().set("sounds.openKitMenu", openKitMenu);
        SkyWarsReloaded.get().getConfig().set("sounds.openChestMenu", openChestMenu);
        SkyWarsReloaded.get().getConfig().set("sounds.openHealthMenu", openHealthMenu);
        SkyWarsReloaded.get().getConfig().set("sounds.openTimeMenu", openTimeMenu);
        SkyWarsReloaded.get().getConfig().set("sounds.openWeatherMenu", openWeatherMenu);
        SkyWarsReloaded.get().getConfig().set("sounds.openModifierMenu", openModifierMenu);
        SkyWarsReloaded.get().getConfig().set("sounds.confirmSelectionSound", confirmSelection);
        SkyWarsReloaded.get().getConfig().set("sounds.errorSound", errorSound);

        SkyWarsReloaded.get().getConfig().set("disable-commands.exceptions", enabledCommands);
        SkyWarsReloaded.get().getConfig().set("disable-commands.enabled", disableCommands);

        SkyWarsReloaded.get().getConfig().set("disable-commands-spectate.enabled", disableCommandsSpectate);
        SkyWarsReloaded.get().getConfig().set("disable-commands-spectate.exceptions", enabledCommandsSpectate);

        SkyWarsReloaded.get().getConfig().getBoolean("chat.externalChat.useExternalChat", useExternalChat);
        SkyWarsReloaded.get().getConfig().getBoolean("chat.externalChat.addPrefix", addPrefix);
        SkyWarsReloaded.get().getConfig().getBoolean("chat.enableFormatter", enableFormatter);
        SkyWarsReloaded.get().getConfig().getBoolean("chat.limitGameChatToGame", limitGameChat);
        SkyWarsReloaded.get().getConfig().getBoolean("chat.limitSpecChatToSpec", limitSpecChat);
        SkyWarsReloaded.get().getConfig().getBoolean("chat.limitLobbyChatToLobby", limitLobbyChat);
        SkyWarsReloaded.get().getConfig().getBoolean("chat.useTeamChat", useTeamChat);

        for (String name : itemNames) {
            SkyWarsReloaded.get().getConfig().set("items." + name, materials.get(name));
        }

        SkyWarsReloaded.get().saveConfig();
    }

    public List<String> getEnabledCommands() {
        return enabledCommands;
    }

    public List<String> getEnabledCommandsSpectate() {
        return enabledCommandsSpectate;
    }

    public int getUpdateTime() {
        return leaderboardUpdateInterval;
    }

    public boolean disableCommands() {
        return disableCommands;
    }

    public boolean disableCommandsSpectate() {
        return disableCommandsSpectate;
    }

    public int getWaitTimer() {
        return waitTimer;
    }

    public int getTimeAfterMatch() {
        return timeAfterMatch;
    }

    public int getFireWorksPer5Tick() {
        return fireworksPer5Tick;
    }

    public boolean fireworksEnabled() {
        return fireworksEnabled;
    }

    public boolean titlesEnabled() {
        return titlesEnabled;
    }

    public boolean particlesEnabled() {
        return particlesEnabled;
    }

    public boolean soundsEnabled() {
        return playSounds;
    }

    public boolean spectateEnable() {
        return spectateEnabled;
    }

    public boolean debugEnabled() {
        return debug;
    }

    public int getMaxMapSize() {
        return maxMapSize;
    }

    public Location getSpawn() {
        return spawn;
    }

    public void setSpawn(Location location) {
        this.spawn = location;
        if (!lobbyWorlds.contains(spawn.getWorld().getName())) {
            lobbyWorlds.add(spawn.getWorld().getName());
            save();
        }
    }

    public boolean bungeeMode() {
        return (bungeeMode && SkyWarsReloaded.get().isEnabled());
    }

    public String getBungeeLobby() {
        return bungeeLobby;
    }

    public int getCooldown() {
        return cooldown;
    }

    public String getMaterial(String string) {
        return materials.get(string);
    }

    public boolean pressurePlateJoin() {
        return pressurePlate;
    }

    public boolean resetTimerOnJoin() {
        return resetTimerOnJoin;
    }

    public boolean enableWinMessage() {
        return enableWinMessage;
    }

    public String getCountdownSound() {
        return countdown;
    }

    public int getWinnerXP() {
        return winnerXP;
    }

    public int getKillerXP() {
        return killerXP;
    }

    public int getVip1() {
        return vip1;
    }

    public int getVip2() {
        return vip2;
    }

    public int getVip3() {
        return vip3;
    }

    public int getVip4() {
        return vip4;
    }

    public int getVip5() {
        return vip5;
    }

    public List<String> getWinCommands() {
        return winCommands;
    }

    public List<String> getKillCommands() {
        return killCommands;
    }

    public boolean glassMenuEnabled() {
        return glassEnabled;
    }

    public boolean particleMenuEnabled() {
        return particleEnabled;
    }

    public boolean projectileMenuEnabled() {
        return projectEnabled;
    }

    public boolean killsoundMenuEnabled() {
        return killsoundEnabled;
    }

    public boolean winsoundMenuEnabled() {
        return winsoundEnabled;
    }

    public boolean tauntsMenuEnabled() {
        return tauntsMenuEnabled;
    }

    public boolean optionsMenuEnabled() {
        return optionsEnabled;
    }

    public int getOptionsSlot() {
        return optionsSlot;
    }

    public boolean joinMenuEnabled() {
        return joinEnabled;
    }

    public int getJoinSlot() {
        return joinSlot;
    }

    public boolean spectateMenuEnabled() {
        return spectateMenuEnabled;
    }

    public int getSpectateSlot() {
        return spectateSlot;
    }

    public boolean teleportOnJoin() {
        return teleportOnJoin;
    }

    public int getRandPos() {
        return randPos;
    }

    public Material getRandMat() {
        return Material.valueOf(randMat);
    }

    public int getNoKitPos() {
        return noKitPos;
    }

    public Material getNoKitMat() {
        return Material.valueOf(noKitMat);
    }

    public boolean promptForResource() {
        return promptResource;
    }

    public String getResourceLink() {
        return resourcePack;
    }

    public int getLeaderSize() {
        return leaderSize;
    }

    public boolean leaderSignsEnabled() {
        return leaderSignsEnabled;
    }

    public boolean leaderHeadsEnabled() {
        return leaderHeadsEnabled;
    }

    public long getTicksPerUpdate() {
        return ticksPerUpdate;
    }

    public boolean kitVotingEnabled() {
        return kitVotingEnabled;
    }

    public int getKitVotePos() {
        return kitvotepos;
    }

    public boolean areKitsEnabled() {
        return kitsEnabled;
    }

    public int getChestVotePos() {
        return chestvotepos;
    }

    public boolean isChestVoteEnabled() {
        return chestVoteEnabled;
    }

    public int getHealthVotePos() {
        return healthvotepos;
    }

    public boolean isHealthVoteEnabled() {
        return healthVoteEnabled;
    }

    public int getTimeVotePos() {
        return timevotepos;
    }

    public boolean isTimeVoteEnabled() {
        return timeVoteEnabled;
    }

    public int getWeatherVotePos() {
        return weathervotepos;
    }

    public boolean isWeatherVoteEnabled() {
        return weatherVoteEnabled;
    }

    public int getModifierVotePos() {
        return modifiervotepos;
    }

    public boolean isModifierVoteEnabled() {
        return modifierVoteEnabled;
    }

    public boolean lobbyBoardEnabled() {
        return lobbyBoardEnabled;
    }

    public String getJoinSound() {
        return joinSound;
    }

    public String getLeaveSound() {
        return leaveSound;
    }

    public String getOpenOptionsMenuSound() {
        return openOptionsMenu;
    }

    public String getOpenJoinMenuSound() {
        return openJoinMenu;
    }

    public String getOpenSpectateMenuSound() {
        return openSpectateMenu;
    }

    public String getOpenParticleMenuSound() {
        return openParticleMenu;
    }

    public String getOpenProjectileMenuSound() {
        return openProjectileMenu;
    }

    public String getOpenKillSoundMenuSound() {
        return openKillSoundMenu;
    }

    public String getOpenWinSoundMenuSound() {
        return openWinSoundMenu;
    }

    public String getOpenGlassMenuSound() {
        return openGlassMenu;
    }

    public String getOpenTauntMenuSound() {
        return openTauntMenu;
    }

    public String getOpenKitMenuSound() {
        return openKitMenu;
    }

    public String getOpenChestMenuSound() {
        return openChestMenu;
    }

    public String getOpenTimeMenuSound() {
        return openTimeMenu;
    }

    public String getOpenWeatherMenuSound() {
        return openWeatherMenu;
    }

    public String getOpenModifierMenuSound() {
        return openModifierMenu;
    }

    public String getConfirmeSelctionSound() {
        return confirmSelection;
    }

    public String getErrorSound() {
        return errorSound;
    }

    public boolean economyEnabled() {
        return economyEnabled;
    }

    public boolean protectLobby() {
        return protectlobby;
    }

    public boolean displayPlayerExeperience() {
        return displayPlayerExeperience;
    }

    public int maxPartySize() {
        return maxPartySize;
    }

    public boolean partyEnabled() {
        return partyEnabled;
    }

    public List<String> getLobbyWorlds() {
        return lobbyWorlds;
    }

    public boolean votingEnabled() {
        return voteEnabled;
    }

    public int getVotingPos() {
        return votepos;
    }

    public int getExitPos() {
        return exitpos;
    }

    public int getWinnerEco() {
        return winnerEco;
    }

    public int getKillerEco() {
        return killerEco;
    }

    public void setEconomyEnabled(boolean b) {
        economyEnabled = b;
    }

    public boolean hologramsEnabled() {
        return useHolograms;
    }

    public void setHologramsEnabled(boolean b) {
        useHolograms = b;
    }

    public boolean isTypeEnabled(LeaderType type) {
        switch (type) {
            case WINS:
                return winsEnabled;
            case LOSSES:
                return lossesEnabled;
            case KILLS:
                return killsEnabled;
            case DEATHS:
                return deathsEnabled;
            case XP:
                return xpEnabled;
            default:
                return false;
        }
    }

    public int getSpeed() {
        return speed;
    }

    public int getJump() {
        return jump;
    }

    public int getStrength() {
        return strength;
    }

    public boolean tauntsEnabled() {
        return tauntsEnabled;
    }

    public String getOpenHealthMenuSound() {
        return openHealthMenu;
    }

    public int getKitMenuSize() {
        return kitMenuSize;
    }

    public int getGlassSlot() {
        return glassselectslot;
    }

    public int getParticleSlot() {
        return particleselectslot;
    }

    public int getProjectileSlot() {
        return projectileselectslot;
    }

    public int getKillSoundSlot() {
        return killsoundselectslot;
    }

    public int getWinSoundSlot() {
        return winsoundselectslot;
    }

    public int getTauntSlot() {
        return tauntselectslot;
    }

    public double getSnowDamage() {
        return snowballDamage;
    }

    public double getEggDamage() {
        return eggDamage;
    }

    public int getMaxDoubleChest() {
        return maxDoubleChest;
    }

    public boolean getLoadTrappedChestsAsCenter() {
        return loadTrappedChestsAsCenter;
    }

    public int getMaxChest() {
        return maxChest;
    }

    public boolean useExternalChat() {
        return useExternalChat;
    }

    public boolean addPrefix() {
        return addPrefix;
    }

    public boolean formatChat() {
        return enableFormatter;
    }

    public boolean limitGameChat() {
        return limitGameChat;
    }

    public boolean limitSpecChat() {
        return limitSpecChat;
    }

    public boolean limitLobbyChat() {
        return limitLobbyChat;
    }

    public int getSingleSlot() {
        return singleSlot;
    }

    public int getTeamSlot() {
        return teamSlot;
    }

    public boolean usePlayerNames() {
        return usePlayerNames;
    }

    public String getTeamMaterial() {
        return teamMaterial;
    }

    public boolean usePlayerGlassColors() {
        return usePlayerGlassColors;
    }

    public boolean showHealth() {
        return showHealth;
    }

    public double getBorderSize() {
        return borderSize;
    }

    public boolean borderEnabled() {
        return borderEnabled;
    }

    public List<String> getGameEndCommands() {
        return gameEndCommands;
    }

    public boolean getLookDirectionEnabled() {
        return lookDirectionEnabled;
    }

    public boolean getEnablePVPTimer() {
        return enablePVPTimer;
    }

    public int getPVPTimerTime() {
        return PVPTimerTime;
    }

    public boolean getEnableQuickDeath() {
        return enableQuickDeath;
    }

    public int getQuickDeathY() {
        return quickDeathY;
    }

    public boolean getKickOnWorldTeleport() {
        return kickOnWorldTeleport;
    }

    public boolean getClearInventoryOnWin() { return clearInventoryOnWin; }

    public boolean getEnableFlightOnWin() { return enableFlightOnWin; }

    public List<String> getGameServers() { return gameServers; }

    public boolean isLobbyServer() { return isLobbyServer; }

    public boolean isRandomVoteEnabled() { return randomOptionVoteEnabled; }
    public boolean isScavengerChestEnabled() { return scavengerChestEnabled; }
    public boolean isJoinGameItemEnabled() { return joinGameItemEnabled; }
    public boolean isSpectateGameItemEnabled() { return spectateGameItemEnabled; }
    public boolean isOptionsItemEnabled() { return optionsGameItemEnabled; }

    public int getTeamSelectPos() { return teamSelectPos; }

    public boolean isUseTeamMaterialBytes() { return useTeamMaterialBytes; }
    public int getStandardTeamMaterialByte() { return standardTeamMaterialByte; }
    public boolean isUseSeparateCages() { return useSeparateCages; }

    public boolean isClearInventoryOnLobbyJoin() { return clearInventoryOnLobbyJoin; }

    public boolean isChangeTablistNames() {
        return changeTablistNames;
    }
    public boolean isUseTeamNumberInMenu() { return useTeamNumberInMenu; }


    public boolean isUseSlimeWorldManager() { return useSlimeWorldManager; }
    public String getSlimeWorldManagerSource() { return slimeWorldManagerSource; }
    public boolean isUsePartyAndFriends() { return usePartyAndFriends; }

    public boolean isUseTeamChat() { return useTeamChat; }
    public String getTimeFormat() { return timeFormat; }

    public boolean isCheckForBetaVersion() { return checkForBetaVersion; }
    public boolean isDisplayPlayerExeperience() {
        return displayPlayerExeperience;
    }
}

