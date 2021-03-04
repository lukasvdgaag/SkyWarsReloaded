package com.walrusone.skywarsreloaded.utilities;

import com.walrusone.skywarsreloaded.SkyWarsReloaded;
import com.walrusone.skywarsreloaded.enums.MatchState;
import com.walrusone.skywarsreloaded.events.SkyWarsMatchStateChangeEvent;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.BlockState;
import org.bukkit.block.Sign;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class SWRServer {


    private static ArrayList<SWRServer> servers = new ArrayList<>();
    private static final Object serversLock = new Object();

    private String serverName;
    private final Object serverNameLock = new Object();
    private String displayName;
    private final Object displayNameLock = new Object();
    private int playerCount;
    private final Object playerCountLock = new Object();
    private int maxPlayers;
    private final Object maxPlayersLock = new Object();
    private int port;
    private final Object portLock = new Object();
    private MatchState state;
    private final Object stateLock = new Object();
    private ArrayList<Location> signs;
    private final Object signsLock = new Object();
    private int teamsize;
    private final Object teamsizeLock = new Object();
    private String hostname;
    private final Object hostnameLock = new Object();

    public SWRServer(final String name, final int port) {
        this.serverName = name;
        signs = new ArrayList<Location>();
        this.displayName = "Initializing";
        this.playerCount = 0;
        this.maxPlayers = 0;
        this.port = port;
        this.state = MatchState.OFFLINE;
        this.teamsize = 1;
    }

    public String getHostname() {
        synchronized (hostnameLock) {
            return this.hostname;
        }
    }

    public void setHostname(String host) {
        synchronized (hostnameLock) {
            this.hostname = host;
        }
    }

    public static ArrayList<SWRServer> getServersCopy() {
        synchronized (serversLock) {
            return new ArrayList<>(servers);
        }
    }

    public static void addServer(SWRServer server) {
        synchronized (serversLock) {
            servers.add(server);
        }
    }

    public static void clearServers() {
        synchronized (serversLock) {
            servers.clear();
        }
    }

    public static void setServerData(final ArrayList<SWRServer> serverData) {
        synchronized (serversLock) {
            SWRServer.servers = serverData;
        }
    }

    public static SWRServer getServer(final String name) {
        synchronized (serversLock) {
            for (final SWRServer server : getServersCopy()) {
                if (server.getServerName().equals(name)) {
                    return server;
                }
            }
            return null;
        }
    }

    public static SWRServer getAvailableServer() {
        int highestPlayers = 0;
        SWRServer swrServer = null;
        synchronized (serversLock) {
            for (SWRServer server : getServersCopy()) {
                if ((server.getMatchState().equals(MatchState.WAITINGSTART) || server.getMatchState().equals(MatchState.WAITINGLOBBY)) && server.getPlayerCount() < server.getMaxPlayers()) {
                    if (server.getPlayerCount() >= highestPlayers) {
                        highestPlayers = server.getPlayerCount();
                        swrServer = server;
                    }
                }
            }
        }
        return swrServer;
    }

    public static void saveAllSigns() {
        for (SWRServer server : getServersCopy()) {
            server.saveSigns();
        }
    }

    public static SWRServer getSign(Location loc) {
        for (SWRServer server : getServersCopy()) {
            if (server.getSignsCopy().contains(loc)) {
                return server;
            }
        }
        return null;
    }

    public static SWRServer getServerByDisplayName(String stripColor) {
        for (SWRServer server : getServersCopy()) {
            if (ChatColor.stripColor(ChatColor.translateAlternateColorCodes('&', server.getDisplayName())).equalsIgnoreCase(stripColor)) {
                return server;
            }
        }
        return null;
    }

    private static void updateBlock(Block block, String item) {
        ItemStack itemStack = SkyWarsReloaded.getIM().getItem(item);
        Material material = itemStack.getType();
        block.setType(material);
        if (SkyWarsReloaded.getNMS().getVersion() < 13 && (
                material.equals(Material.valueOf("WOOL")) ||
                material.equals(Material.valueOf("STAINED_GLASS")) ||
                material.equals(Material.valueOf("STAINED_CLAY")))
        ) {
            SkyWarsReloaded.getNMS().setBlockWithColor(
                    block.getWorld(), block.getX(), block.getY(), block.getZ(),
                    material,
                    itemStack.getData().getData());
        }
    }

    public int getTeamSize() {
        synchronized (teamsizeLock) {
            return this.teamsize;
        }
    }

    public void setTeamsize(int size) {
        synchronized (teamsizeLock) {
            this.teamsize = size;
        }
    }

    public String getServerName() {
        synchronized (serverNameLock) {
            return serverName;
        }
    }

    public int getPlayerCount() {
        synchronized (playerCountLock) {
            return this.playerCount;
        }
    }

    public void setPlayerCount(int count) {
        synchronized (playerCountLock) {
            this.playerCount = count;
        }
    }

    public int getMaxPlayers() {
        synchronized (maxPlayersLock) {
            return this.maxPlayers;
        }
    }

    public void setMaxPlayers(int max) {
        synchronized (maxPlayersLock) {
            this.maxPlayers = max;
        }
    }

    public MatchState getMatchState() {
        synchronized (stateLock) {
            return this.state;
        }
    }

    public void setMatchState(String gameStarted) {
        try {
            this.setMatchState(MatchState.valueOf(gameStarted));
        } catch (IllegalArgumentException e) {
            this.setMatchState(MatchState.OFFLINE);
        }
    }

    public void setMatchState(MatchState serverState) {
        Bukkit.getPluginManager().callEvent(new SkyWarsMatchStateChangeEvent(this, serverState));
        synchronized (stateLock) {
            this.state = serverState;
        }
    }

    public String getDisplayName() {
        synchronized (displayNameLock) {
            return this.displayName;
        }
    }

    public void setDisplayName(String string) {
        synchronized (displayNameLock) {
            this.displayName = string;
        }
    }

    public int getPort() {
        synchronized (portLock) {
            return this.port;
        }
    }

    public ArrayList<Location> getSignsCopy() {
        synchronized (signsLock) {
            return new ArrayList<>(signs);
        }
    }

    public void addSign(Location loc) {
        synchronized (signsLock) {
            this.signs.add(loc);
        }
        saveSigns();
    }

    public void clearSigns() {
        synchronized (signsLock) {
            signs.clear();
        }
    }

    public boolean hasGameSign(Location loc) {
        synchronized (signsLock) {
            return signs.contains(loc);
        }
    }

    public void removeSign(Location loc) {
        synchronized (signsLock) {
            signs.remove(loc);
        }
        saveSigns();
    }

    public void saveSigns() {
        List<String> signLocs = new ArrayList<>();
        for (Location loc : getSignsCopy()) {
            signLocs.add(Util.get().locationToString(loc));
        }
        SkyWarsReloaded.get().getConfig().set("signs." + this.getServerName(), signLocs);
        SkyWarsReloaded.get().saveConfig();
        SkyWarsReloaded.get().reloadConfig();
        SkyWarsReloaded.getCfg().load();
    }

    /** WARNING: Must be run on main thread */
    public void updateSigns() {
        for (Location loc : getSignsCopy()) {
            if (loc.getBlock() == null) { return; }
            BlockState bs = loc.getBlock().getState();
            Sign sign = null;
            if (bs instanceof Sign) {
                sign = (Sign) bs;
            }
            if (sign != null && sign.getBlock() != null) {
                Block b = sign.getBlock();
                org.bukkit.material.Sign metaSign = (org.bukkit.material.Sign) b.getState().getData();
                BlockFace attachedFace = metaSign.getAttachedFace();
                if (attachedFace == null) {
                    if (metaSign.isWallSign()) attachedFace = BlockFace.SOUTH;
                    else {
                        SkyWarsReloaded.get().getLogger().severe("ERROR: Sign at " + b.getX() + " " + b.getY() + " " + b.getZ() +
                                " has no attached face saved! Please replace the sign.");
                        continue;
                    }
                }
                if (SkyWarsReloaded.getCfg().debugEnabled()) {
                    SkyWarsReloaded.get().getLogger().info("Sign face mod " + attachedFace.getModX() + " " + attachedFace.getModY() + " " + attachedFace.getModZ());
                }
                Block attachedBlock = b.getRelative(attachedFace);
                setMaterial(attachedBlock);
                String signState = "";
                if (state.equals(MatchState.OFFLINE)) {
                    signState = new Messaging.MessageFormatter().format("signs.offline");
                } else if (state.equals(MatchState.WAITINGSTART) || state.equals(MatchState.WAITINGLOBBY)) {
                    signState = new Messaging.MessageFormatter().format("signs.joinable");
                } else if (state.equals(MatchState.PLAYING)) {
                    signState = new Messaging.MessageFormatter().format("signs.playing");
                } else if (state.equals(MatchState.ENDING)) {
                    signState = new Messaging.MessageFormatter().format("signs.ending");
                }
                sign.getBlock().getChunk().load();
                sign.setLine(0, new Messaging.MessageFormatter().setVariable("matchstate", signState).
                        setVariable("mapname", displayName.toUpperCase()).
                        setVariable("playercount", "" + getPlayerCount()).
                        setVariable("teamsize", "" + getTeamSize()).
                        setVariable("maxplayers", "" + getMaxPlayers()).format("signs.line1"));
                sign.setLine(1, new Messaging.MessageFormatter().setVariable("matchstate", signState).
                        setVariable("mapname", displayName.toUpperCase()).
                        setVariable("playercount", "" + getPlayerCount()).
                        setVariable("teamsize", "" + getTeamSize()).
                        setVariable("maxplayers", "" + getMaxPlayers()).format("signs.line2"));
                sign.setLine(2, new Messaging.MessageFormatter().setVariable("matchstate", signState).
                        setVariable("mapname", displayName.toUpperCase()).
                        setVariable("playercount", "" + getPlayerCount()).
                        setVariable("teamsize", "" + getTeamSize()).
                        setVariable("maxplayers", "" + getMaxPlayers()).format("signs.line3"));
                sign.setLine(3, new Messaging.MessageFormatter().setVariable("matchstate", signState).
                        setVariable("mapname", displayName.toUpperCase()).
                        setVariable("playercount", "" + getPlayerCount()).
                        setVariable("teamsize", "" + getTeamSize()).
                        setVariable("maxplayers", "" + getMaxPlayers()).format("signs.line4"));
                sign.update();
            }
        }
    }

    /** WARNING: Must be run on main thread */
    private void setMaterial(Block attachedBlock) {
        MatchState tmpState = getMatchState();
        if (tmpState.equals(MatchState.OFFLINE)) {
            updateBlock(attachedBlock, "blockoffline");
        } else if (tmpState.equals(MatchState.WAITINGSTART) || tmpState.equals(MatchState.WAITINGLOBBY)) {
            updateBlock(attachedBlock, "blockwaiting");
        } else if (tmpState.equals(MatchState.PLAYING)) {
            updateBlock(attachedBlock, "blockplaying");
        } else if (tmpState.equals(MatchState.ENDING)) {
            updateBlock(attachedBlock, "blockending");
        }
    }

    public boolean canAddParty(Party party) {
        // todo add specific test for parties
        if (getMatchState() != MatchState.WAITINGSTART && !getMatchState().equals(MatchState.WAITINGLOBBY)) {
            return false;
        }

        int players = getPlayerCount();
        return players + party.getSize() - 1 < getMaxPlayers();
    }

    public boolean canAddPlayer() {
        if (getMatchState() != MatchState.WAITINGSTART && !getMatchState().equals(MatchState.WAITINGLOBBY)) {
            return false;
        }

        int players = getPlayerCount();
        return players + 1 < getMaxPlayers();
    }

    public static void updateServerSigns() {
        for (SWRServer server : getServersCopy()) {
            server.clearSigns();
            List<String> signLocs = SkyWarsReloaded.get().getConfig().getStringList("signs." + server.getServerName());
            if (signLocs != null) {
                for (String sign : signLocs) {
                    Location loc = Util.get().stringToLocation(sign);
                    server.addSign(loc);
                }
            }
        }
    }

}
