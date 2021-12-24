package net.gcnt.skywarsreloaded.command.maps;

import net.gcnt.skywarsreloaded.SkyWarsReloaded;
import net.gcnt.skywarsreloaded.command.Cmd;
import net.gcnt.skywarsreloaded.game.GameTemplate;
import net.gcnt.skywarsreloaded.game.GameWorld;
import net.gcnt.skywarsreloaded.game.types.SpawnType;
import net.gcnt.skywarsreloaded.utils.AbstractItem;
import net.gcnt.skywarsreloaded.utils.Message;
import net.gcnt.skywarsreloaded.utils.SWCoord;
import net.gcnt.skywarsreloaded.utils.properties.MessageProperties;
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
        if (args.length == 0) {
            plugin.getMessages().getMessage(MessageProperties.MAPS_ENTER_SPAWN_TYPE.toString()).send(sender);
            return true;
        }

        SWPlayer player = (SWPlayer) sender;
        GameWorld world = plugin.getGameManager().getGameWorldByName(player.getLocation().world().getName());
        if (world == null || !world.isEditing()) {
            plugin.getMessages().getMessage(MessageProperties.ERROR_NO_TEMPLATE_WORLD_FOUND.toString()).send(sender);
            return true;
        }

        GameTemplate template = world.getTemplate();

        SpawnType type = SpawnType.fromString(args[0]);
        if (type == null) {
            plugin.getMessages().getMessage(MessageProperties.MAPS_ENTER_SPAWN_TYPE.toString()).send(sender);
            return true;
        }
        int team = 1;

        if (type == SpawnType.PLAYER) {
            if (template.getTeamSize() > 1) {
                if (args.length == 1) {
                    plugin.getMessages().getMessage(MessageProperties.MAPS_ENTER_TEAM_NUMBER.toString()).send(sender);
                    return true;
                }
                //else
                if (!plugin.getUtils().isInt(args[1])) {
                    plugin.getMessages().getMessage(MessageProperties.MAPS_ENTER_TEAM_NUMBER_NUMBER.toString()).send(sender);
                    return true;
                }

                team = Integer.parseInt(args[1]);
            } else {
                if (args.length > 1) {
                    // player entered a team number.
                    plugin.getMessages().getMessage(MessageProperties.TITLES_MAPS_NO_TEAM_GAME.toString()).replace("%template%", template.getName()).sendTitle(sender);
                    plugin.getMessages().getMessage(MessageProperties.MAPS_NO_TEAM_GAME.toString()).replace("%template%", template.getName()).send(sender);
                    return true;
                }
            }
        }

        final SWCoord location = player.getLocation();
        final int teamIndex = team - 1;

        switch (type) {
            case LOBBY -> {
                template.setWaitingLobbySpawn(location);
                plugin.getMessages().getMessage(MessageProperties.TITLES_MAPS_SET_SPAWN_LOBBY.toString()).replace("%template%", template.getName()).sendTitle(player);
                plugin.getMessages().getMessage(MessageProperties.MAPS_SET_SPAWN_LOBBY.toString()).replace("%template%", template.getName()).send(player);
            }
            case SPECTATE -> {
                template.setSpectateSpawn(location);
                plugin.getMessages().getMessage(MessageProperties.TITLES_MAPS_SET_SPAWN_SPECTATE.toString()).replace("%template%", template.getName()).sendTitle(player);
                plugin.getMessages().getMessage(MessageProperties.MAPS_SET_SPAWN_SPECTATE.toString()).replace("%template%", template.getName()).send(player);
            }
            case PLAYER -> {
                final SWCoord blockLocation = location.asBlock();
                SpawnAddResult result = template.addSpawn(teamIndex, blockLocation);
                Message message = switch (result) {
                    case INDEX_TOO_LOW -> plugin.getMessages().getMessage(MessageProperties.MAPS_SPAWN_INDEX_LOW.toString())
                            .replace("%template%", template.getName());
                    case INDEX_TOO_HIGH -> plugin.getMessages().getMessage(MessageProperties.MAPS_SPAWN_INDEX_HIGH.toString())
                            .replace("%template%", template.getName()).replace("%max%", (template.getTeamSpawnpoints().size() + 1) + "");
                    case SPAWN_ALREADY_EXISTS -> plugin.getMessages().getMessage(MessageProperties.MAPS_SPAWN_ALREADY_EXISTS.toString())
                            .replace("%template%", template.getName());
                    case NEW_TEAM_ADDED -> {
                        if (template.getTeamSize() == 1) {
                            yield plugin.getMessages().getMessage(MessageProperties.MAPS_SET_SPAWN_PLAYER_SINGLES.toString())
                                    .replace("%template%", template.getName())
                                    .replace("%number%", template.getTeamSpawnpoints().size() + "");
                        } else {
                            int currentSpawns = template.getTeamSpawnpoints().get(teamIndex).size();
                            if (currentSpawns < template.getTeamSize()) {
                                yield plugin.getMessages().getMessage(MessageProperties.MAPS_SET_SPAWN_PLAYER_TEAM.toString())
                                        .replace("%template%", template.getName())
                                        .replace("%number%", currentSpawns + "")
                                        .replace("%team%", team + "")
                                        .replace("%left%", (template.getTeamSize() - currentSpawns) + "");
                            } else {
                                yield plugin.getMessages().getMessage(MessageProperties.MAPS_SET_SPAWN_PLAYER_TEAM_FINAL.toString())
                                        .replace("%template%", template.getName())
                                        .replace("%number%", currentSpawns + "")
                                        .replace("%team%", team + "");
                            }
                        }
                    }
                    case TEAM_UPDATED -> plugin.getMessages().getMessage(MessageProperties.MAPS_SET_SPAWN_PLAYER_TEAM_FINAL.toString())
                            .replace("%template%", template.getName())
                            .replace("%number%", (template.getTeamSpawnpoints().get(team - 1).size()) + "")
                            .replace("%team%", team + "");
                    case MAX_TEAM_SPAWNS_REACHED -> plugin.getMessages().getMessage(MessageProperties.MAPS_SET_SPAWN_PLAYER_TEAM_MAX.toString())
                            .replace("%template%", template.getName())
                            .replace("%team%", team + "");
                };

                if (result.isSuccess()) {
                    plugin.getMessages().getMessage(MessageProperties.TITLES_MAPS_SET_SPAWN_PLAYER.toString()).replace("%template%", template.getName()).sendTitle(player);
                    location.world().setBlockAt(blockLocation, AbstractItem.getItem("BEACON"));
                    player.teleport(player.getLocation().add(0, 1, 0));
                } else {
                    plugin.getMessages().getMessage(MessageProperties.TITLES_MAPS_SET_SPAWN_PLAYER_FAIL.toString()).replace("%template%", template.getName()).sendTitle(player);
                }
                message.send(sender);
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
                    GameWorld world = plugin.getGameManager().getGameWorldByName(player.getLocation().world().getName());
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
