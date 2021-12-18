package net.gcnt.skywarsreloaded.command.maps;

import net.gcnt.skywarsreloaded.SkyWarsReloaded;
import net.gcnt.skywarsreloaded.command.Cmd;
import net.gcnt.skywarsreloaded.game.GameTemplate;
import net.gcnt.skywarsreloaded.game.GameWorld;
import net.gcnt.skywarsreloaded.game.types.SpawnType;
import net.gcnt.skywarsreloaded.utils.AbstractItem;
import net.gcnt.skywarsreloaded.utils.SWCoord;
import net.gcnt.skywarsreloaded.utils.results.SpawnAddResult;
import net.gcnt.skywarsreloaded.wrapper.player.SWPlayer;
import net.gcnt.skywarsreloaded.wrapper.sender.SWCommandSender;

import java.util.ArrayList;
import java.util.List;

public class SetSpawnCmd extends Cmd {

    public SetSpawnCmd(SkyWarsReloaded plugin) {
        super(plugin, "skywarsmap", "setspawn", "skywars.command.map.setspawn", true, "<type> [team]", "Set a spawnpoint.", "ss", "spawn");
    }

    @Override
    public boolean run(SWCommandSender sender, String[] args) {
        SWPlayer player = (SWPlayer) sender;
        GameWorld world = plugin.getGameManager().getGameWorldFromWorldName(player.getLocation().world().getName());
        if (world == null) {
            sender.sendMessage(plugin.getUtils().colorize("&cYou must be in a skywars game template world to execute this command."));
            return true;
        }

        GameTemplate template = world.getTemplate();
        if (!world.isEditing()) {
            sender.sendMessage(plugin.getUtils().colorize("&cYou are currently in a skywars game world for the arena &7%s&c that is not in editing mode. Use \"&c&o/swmap edit %s&c\" to edit this template.".formatted(template.getDisplayName(), template.getName())));
            return true;
        }

        SpawnType type = SpawnType.fromString(args[0]);
        if (type == null) {
            sender.sendMessage(plugin.getUtils().colorize("&cPlease enter a valid spawn type. Options: &7%s&f, &7%s&f, &7%s".formatted("player", "lobby", "spectate")));
            return true;
        }
        int team = 1;

        if (type == SpawnType.PLAYER) {
            if (template.getTeamSize() > 1) {
                if (args.length == 1) {
                    sender.sendMessage(plugin.getUtils().colorize("&cPlease enter a team number."));
                    return true;
                }
                //else
                if (!plugin.getUtils().isInt(args[1])) {
                    sender.sendMessage(plugin.getUtils().colorize("&cPlease enter a valid team number (number)."));
                    return true;
                }

                team = Integer.parseInt(args[1]);
            } else {
                if (args.length > 1) {
                    // player entered a team number.
                    player.sendTitle(plugin.getUtils().colorize("&c&lNOT A TEAM GAME!"), plugin.getUtils().colorize("&7You can only set team spawns for team games"));
                    sender.sendMessage(plugin.getUtils().colorize("&7Not a team game template!&c You are currently editing a solo game template (with team size 1) which doesn't support adding a spawn to a specific team. If you wish to" +
                            " make this template a team arena, please use the command \"&o/swmap teamsize %s <size>&r&c\".".formatted(template.getName())));
                    return true;
                }
            }
        }

        final SWCoord location = player.getLocation();
        final int teamIndex = team - 1;

        switch (type) {
            case LOBBY -> {
                template.setWaitingLobbySpawn(location);
                player.sendTitle(plugin.getUtils().colorize("&a&lSPAWN SET!"), plugin.getUtils().colorize("&7Successfully set the waiting lobby spawnpoint!"));
                sender.sendMessage(plugin.getUtils().colorize("&aThe waiting spawn of the template &b%s &ahas been set to your current location.".formatted(template.getDisplayName())));
            }
            case SPECTATE -> {
                template.setSpectateSpawn(location);
                player.sendTitle(plugin.getUtils().colorize("&a&lSPAWN SET!"), plugin.getUtils().colorize("&7Successfully set the spectator spawnpoint!"));
                sender.sendMessage(plugin.getUtils().colorize("&aThe spectator spawn of the template &b%s &ahas been set to your current location.".formatted(template.getDisplayName())));
            }
            case PLAYER -> {
                final SWCoord blockLocation = location.asBlock();
                SpawnAddResult result = template.addSpawn(teamIndex, blockLocation);
                String message = switch (result) {
                    case INDEX_TOO_LOW -> "&cPlease enter a valid team number (greater than 0)";
                    case INDEX_TOO_HIGH -> "&cPlease enter a valid team number (max %d)".formatted(template.getTeamSpawnpoints().size() + 1);
                    case SPAWN_ALREADY_EXISTS -> "&cThe location you're trying to set is already defined for another team.";
                    case NEW_TEAM_ADDED -> {
                        if (template.getTeamSize() == 1) {
                            yield "&aAdded player spawnpoint &b#%d &ato game template &b%s&a.".formatted(template.getTeamSpawnpoints().size(), template.getDisplayName());
                        } else {
                            int currentSpawns = template.getTeamSpawnpoints().get(teamIndex).size();
                            if (currentSpawns < template.getTeamSize()) {
                                yield "&aAdded player spawnpoint &b#%d &ato team &b%d &afor game template &b%s&a. &e%d &aspawns left to set for this team.".formatted(currentSpawns, team, template.getDisplayName(), template.getTeamSize() - currentSpawns);
                            } else {
                                yield "&aAdded the final player spawnpoint &b#%d &ato team &b%d &afor game template &b%s&a. &e0 &aleft to go.".formatted(currentSpawns, team, template.getDisplayName());
                            }
                        }
                    }
                    case TEAM_UPDATED -> "&aAdded player spawnpoint &b#%d &ato team &b%d &afor game template &b%s&a.".formatted(template.getTeamSpawnpoints().get(team - 1).size(), team, template.getDisplayName());
                    case MAX_TEAM_SPAWNS_REACHED -> "&cTeam %d has reached its maximum player spawnpoints. You could increase the team size of this template with \"&o/swmap teamsize %s <size>&c\" if you wish to proceed.".formatted(team, template.getName());
                };

                if (result.isSuccess()) {
                    player.sendTitle(plugin.getUtils().colorize("&a&lSPAWN SET!"), plugin.getUtils().colorize("&7Successfully added a new player spawnpoint!"));
                    location.world().setBlockAt(blockLocation, AbstractItem.getItem("BEACON"));
                } else {
                    player.sendTitle(plugin.getUtils().colorize("&c&lOH NO!"), plugin.getUtils().colorize("&7Couldn't add the player spawn (see chat)!"));
                }
                sender.sendMessage(plugin.getUtils().colorize(message));
            }
        }

        template.checkToDoList(sender);
        return true;
    }

    // /swm setspawn player
    // /swm setspawn player <team>

    @Override
    public List<String> onTabCompletion(SWCommandSender sender, String[] args) {
        if (args.length == 1) {
            List<String> options = new ArrayList<>();
            for (SpawnType type : SpawnType.values()) {
                options.add(type.name().toLowerCase());
            }
            return options;
        } else if (args.length == 2) {
            if (sender instanceof SWPlayer player) {
                SpawnType type = SpawnType.fromString(args[0]);
                if (type == SpawnType.PLAYER) {
                    GameWorld world = plugin.getGameManager().getGameWorldFromWorldName(player.getLocation().world().getName());
                    if (world != null && world.isEditing() && world.getTemplate().getTeamSize() > 1) {
                        List<String> options = new ArrayList<>();
                        for (int i = 0; i < world.getTemplate().getTeamSpawnpoints().size() + 1; i++) {
                            options.add(String.valueOf(i + 1));
                        }
                        return options;
                    }
                }
            }
        }
        return new ArrayList<>();
    }
}
