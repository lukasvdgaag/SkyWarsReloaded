package com.walrusone.skywarsreloaded.utilities;

import com.walrusone.skywarsreloaded.SkyWarsReloaded;
import com.walrusone.skywarsreloaded.enums.MatchState;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.Sign;

import java.util.ArrayList;
import java.util.List;

public class SWRServer {


    private static ArrayList<SWRServer> servers;

    static {
        SWRServer.servers = new ArrayList<>();
    }

    private String serverName;
    private String displayName;
    private int playerCount;
    private int maxPlayers;
    private int port;
    private MatchState state;
    private ArrayList<Location> signs;
    private int teamsize;
    private String hostname;

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
        return this.hostname;
    }

    public void setHostname(String host) {
        this.hostname = host;
    }

    public static ArrayList<SWRServer> getServers() {
        return SWRServer.servers;
    }

    public static void setPlayers(final ArrayList<SWRServer> serverData) {
        SWRServer.servers = serverData;
    }

    public static SWRServer getServer(final String name) {
        for (final SWRServer server : getServers()) {
            if (server.getServerName().equals(name)) {
                return server;
            }
        }
        return null;
    }

    public static SWRServer getAvailableServer() {
        int highestPlayers = 0;
        SWRServer swrServer = null;
        for (SWRServer server : getServers()) {
            if ((server.getMatchState().equals(MatchState.WAITINGSTART) || server.getMatchState().equals(MatchState.WAITINGLOBBY)) && server.getPlayerCount() < server.getMaxPlayers()) {
                if (server.getPlayerCount() >= highestPlayers) {
                    highestPlayers = server.getPlayerCount();
                    swrServer = server;
                }
            }
        }
        return swrServer;
    }

    public static void saveAllSigns() {
        for (SWRServer server : servers) {
            server.saveSigns();
        }
    }

    public static SWRServer getSign(Location loc) {
        for (SWRServer server : servers) {
            if (server.getSigns().contains(loc)) {
                return server;
            }
        }
        return null;
    }

    public static SWRServer getServerByDisplayName(String stripColor) {
        for (SWRServer server : servers) {
            if (ChatColor.stripColor(ChatColor.translateAlternateColorCodes('&', server.getDisplayName())).equalsIgnoreCase(stripColor)) {
                return server;
            }
        }
        return null;
    }

    private static void updateBlock(Block block, String item) {
        block.setType(SkyWarsReloaded.getIM().getItem(item).getType());
        if ((SkyWarsReloaded.getNMS().getVersion() < 13) && (
                (SkyWarsReloaded.getIM().getItem(item).getType().equals(Material.valueOf("WOOL"))) || (SkyWarsReloaded.getIM().getItem(item).getType().equals(Material.valueOf("STAINED_GLASS"))) || (SkyWarsReloaded.getIM().getItem(item).getType().equals(Material.valueOf("STAINED_CLAY"))))) {
            SkyWarsReloaded.getNMS().setBlockWithColor(block.getWorld(), block.getX(), block.getY(), block.getZ(), SkyWarsReloaded.getIM().getItem(item).getType(), SkyWarsReloaded.getIM().getItem(item).getData().getData());
        }
    }

    public int getTeamSize() {
        return this.teamsize;
    }

    public void setTeamsize(int size) { this.teamsize = size ;}

    public String getServerName() {
        return serverName;
    }

    public int getPlayerCount() {
        return this.playerCount;
    }

    public void setPlayerCount(int count) {
        this.playerCount = count;
    }

    public int getMaxPlayers() {
        return this.maxPlayers;
    }

    public void setMaxPlayers(int max) {
        this.maxPlayers = max;
    }

    public MatchState getMatchState() {
        return this.state;
    }

    public void setMatchState(String gameStarted) {
        try {
            this.state = MatchState.valueOf(gameStarted);
        } catch (IllegalArgumentException e) {
            this.state = MatchState.OFFLINE;
        }
    }

    public void setMatchState(MatchState serverState) {
        state = serverState;
    }

    public String getDisplayName() {
        return this.displayName;
    }

    public void setDisplayName(String string) {
        this.displayName = string;
    }

    public int getPort() {
        return this.port;
    }

    public ArrayList<Location> getSigns() {
        return signs;
    }

    public void addSign(Location loc) {
        this.signs.add(loc);
        saveSigns();
    }

    public void clearSigns() {
        signs.clear();
    }

    public boolean hasGameSign(Location loc) {
        return signs.contains(loc);
    }

    public void removeSign(Location loc) {
        signs.remove(loc);
        saveSigns();
    }

    public void saveSigns() {
        List<String> signLocs = new ArrayList<>();
        for (Location loc : signs) {
            signLocs.add(Util.get().locationToString(loc));
        }
        SkyWarsReloaded.get().getConfig().set("signs." + this.getServerName(), signLocs);
        SkyWarsReloaded.get().saveConfig();
        SkyWarsReloaded.get().reloadConfig();
        SkyWarsReloaded.getCfg().load();
    }

    public void updateSigns() {
        for (Location loc : signs) {
            if (loc.getBlock() == null) { return; }
            BlockState bs = loc.getBlock().getState();
            Sign sign = null;
            if (bs instanceof Sign) {
                sign = (Sign) bs;
            }
            if (sign != null && sign.getBlock() != null) {
                Block b = sign.getBlock();
                org.bukkit.material.Sign meteSign = (org.bukkit.material.Sign) b.getState().getData();
                Block attachedBlock = b.getRelative(meteSign.getAttachedFace());
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
                        setVariable("playercount", "" + playerCount).
                        setVariable("teamsize", "" + teamsize).
                        setVariable("maxplayers", "" + maxPlayers).format("signs.line1"));
                sign.setLine(1, new Messaging.MessageFormatter().setVariable("matchstate", signState).
                        setVariable("mapname", displayName.toUpperCase()).
                        setVariable("playercount", "" + playerCount).
                        setVariable("teamsize", "" + teamsize).
                        setVariable("maxplayers", "" + maxPlayers).format("signs.line2"));
                sign.setLine(2, new Messaging.MessageFormatter().setVariable("matchstate", signState).
                        setVariable("mapname", displayName.toUpperCase()).
                        setVariable("playercount", "" + playerCount).
                        setVariable("teamsize", "" + teamsize).
                        setVariable("maxplayers", "" + maxPlayers).format("signs.line3"));
                sign.setLine(3, new Messaging.MessageFormatter().setVariable("matchstate", signState).
                        setVariable("mapname", displayName.toUpperCase()).
                        setVariable("playercount", "" + playerCount).
                        setVariable("teamsize", "" + teamsize).
                        setVariable("maxplayers", "" + maxPlayers).format("signs.line4"));
                sign.update();
            }
        }
    }

    private void setMaterial(Block attachedBlock) {
        if (state.equals(MatchState.OFFLINE)) {
            updateBlock(attachedBlock, "blockoffline");
        } else if (state.equals(MatchState.WAITINGSTART) || state.equals(MatchState.WAITINGLOBBY)) {
            updateBlock(attachedBlock, "blockwaiting");
        } else if (state.equals(MatchState.PLAYING)) {
            updateBlock(attachedBlock, "blockplaying");
        } else if (state.equals(MatchState.ENDING)) {
            updateBlock(attachedBlock, "blockending");
        }
    }

    public boolean canAddParty(Party party) {
        // todo add specific test for parties
        if (this.state != MatchState.WAITINGSTART && !state.equals(MatchState.WAITINGLOBBY)) {
            return false;
        }

        int players = getPlayerCount();
        return players + party.getSize() - 1 < getMaxPlayers();
    }

    public boolean canAddPlayer() {
        if (this.state != MatchState.WAITINGSTART && !state.equals(MatchState.WAITINGLOBBY)) {
            return false;
        }

        int players = getPlayerCount();
        return players + 1 < getMaxPlayers();
    }

}
