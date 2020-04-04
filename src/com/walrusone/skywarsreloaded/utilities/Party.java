package com.walrusone.skywarsreloaded.utilities;

import com.walrusone.skywarsreloaded.SkyWarsReloaded;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.UUID;


public class Party {
    private static ArrayList<Party> parties = new ArrayList();
    private UUID leader;
    private String name;
    private ArrayList<UUID> members;
    private ArrayList<UUID> invited;

    public Party(Player player, String partyName) {
        leader = player.getUniqueId();
        name = partyName;
        members = new ArrayList();
        invited = new ArrayList();
        members.add(player.getUniqueId());
        parties.add(this);
    }

    public static void removeParty(Party party) {
        party.sendPartyMessage(new Messaging.MessageFormatter()
                .setVariable("leader", Bukkit.getPlayer(party.getLeader()).getName())
                .setVariable("partyname", party.getPartyName()).format("party.disbanded"));
        parties.remove(party);
    }

    public static Party getParty(Player player) {
        for (Party party : parties) {
            if (party.getMembers().contains(player.getUniqueId())) {
                return party;
            }
        }
        return null;
    }

    public static Party getPartyOfInvite(Player player) {
        for (Party party : parties) {
            if (party.getInvited().contains(player.getUniqueId())) {
                return party;
            }
        }
        return null;
    }

    public String getPartyName() {
        return name;
    }

    public void setPartyName(String newName) {
        name = newName;
    }

    private void addMember(Player player) {
        if (!members.contains(player.getUniqueId())) {
            sendPartyMessage(new Messaging.MessageFormatter().setVariable("player", player.getName()).format("party.joined"));
            members.add(player.getUniqueId());
        }
    }

    public void removeMember(Player player) {
        if (members.contains(player.getUniqueId())) {
            members.remove(player.getUniqueId());
            sendPartyMessage(new Messaging.MessageFormatter().setVariable("player", player.getName()).format("party.left"));
        }
    }

    public void sendPartyMessage(String message) {
        for (UUID uuid : members) {
            Player player = Bukkit.getPlayer(uuid);
            if (player != null) {
                player.sendMessage(message);
            }
        }
    }

    public UUID getLeader() {
        return leader;
    }

    public ArrayList<UUID> getMembers() {
        return members;
    }

    public int getSize() {
        return members.size();
    }

    public void invite(Player player) {
        final UUID invite = player.getUniqueId();
        invited.add(invite);
        new BukkitRunnable() {
            public void run() {
                boolean remove = false;
                if (invited.contains(invite)) {
                    remove = true;
                }
                if (remove) {
                    invited.remove(invite);
                }

            }
        }.runTaskLater(SkyWarsReloaded.get(), 300L);
    }

    public boolean acceptInvite(Player player) {
        boolean result = false;
        if ((invited.contains(player.getUniqueId())) && (members.size() < SkyWarsReloaded.getCfg().maxPartySize())) {
            addMember(player);
            result = true;
        }
        invited.remove(player.getUniqueId());
        return result;
    }

    private ArrayList<UUID> getInvited() {
        return invited;
    }

    public boolean declineInvite(Player player) {
        if (invited.contains(player.getUniqueId())) {
            invited.remove(player.getUniqueId());
            sendPartyMessage(new Messaging.MessageFormatter().setVariable("player", player.getName()).format("party.declined"));
            return true;
        }
        return true;
    }
}
