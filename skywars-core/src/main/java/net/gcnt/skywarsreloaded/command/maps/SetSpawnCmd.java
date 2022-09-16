package net.gcnt.skywarsreloaded.command.maps;

import net.gcnt.skywarsreloaded.SkyWarsReloaded;
import net.gcnt.skywarsreloaded.command.Cmd;
import net.gcnt.skywarsreloaded.game.GameInstance;
import net.gcnt.skywarsreloaded.game.GameTemplate;
import net.gcnt.skywarsreloaded.game.types.SpawnType;
import net.gcnt.skywarsreloaded.utils.AbstractItem;
import net.gcnt.skywarsreloaded.utils.Message;
import net.gcnt.skywarsreloaded.utils.SWCoord;
import net.gcnt.skywarsreloaded.utils.properties.MessageProperties;
import net.gcnt.skywarsreloaded.utils.results.SpawnAddResult;
import net.gcnt.skywarsreloaded.wrapper.entity.SWPlayer;
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
        GameInstance world = plugin.getGameManager().getGameWorldByName(player.getLocation().getWorld().getName());
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
            case LOBBY:
                template.setWaitingLobbySpawn(location);
                plugin.getMessages().getMessage(MessageProperties.TITLES_MAPS_SET_SPAWN_LOBBY.toString()).replace("%template%", template.getName()).sendTitle(player);
                plugin.getMessages().getMessage(MessageProperties.MAPS_SET_SPAWN_LOBBY.toString()).replace("%template%", template.getName()).send(player);
                break;
            case SPECTATE:
                template.setSpectateSpawn(location);
                plugin.getMessages().getMessage(MessageProperties.TITLES_MAPS_SET_SPAWN_SPECTATE.toString()).replace("%template%", template.getName()).sendTitle(player);
                plugin.getMessages().getMessage(MessageProperties.MAPS_SET_SPAWN_SPECTATE.toString()).replace("%template%", template.getName()).send(player);
                break;
            case PLAYER:
                final SWCoord blockLocation = location.asBlock();
                SpawnAddResult result = template.addSpawn(teamIndex, blockLocation);
                Message message = null;
                switch (result) {
                    case INDEX_TOO_LOW:
                        plugin.getMessages().getMessage(MessageProperties.MAPS_SPAWN_INDEX_LOW.toString())
                                .replace("%template%", template.getName());
                        break;
                    case INDEX_TOO_HIGH:
                        plugin.getMessages().getMessage(MessageProperties.MAPS_SPAWN_INDEX_HIGH.toString())
                                .replace("%template%", template.getName()).replace("%max%", (template.getTeamSpawnpoints().size() + 1) + "");
                        break;
                    case SPAWN_ALREADY_EXISTS:
                        plugin.getMessages().getMessage(MessageProperties.MAPS_SPAWN_ALREADY_EXISTS.toString())
                                .replace("%template%", template.getName());
                        break;
                    case NEW_TEAM_ADDED:
                        if (template.getTeamSize() == 1) {
                            message = plugin.getMessages().getMessage(MessageProperties.MAPS_SET_SPAWN_PLAYER_SINGLES.toString())
                                    .replace("%template%", template.getName())
                                    .replace("%number%", template.getTeamSpawnpoints().size() + "");
                        } else {
                            int currentSpawns = template.getTeamSpawnpoints().get(teamIndex).size();
                            if (currentSpawns < template.getTeamSize()) {
                                message = plugin.getMessages().getMessage(MessageProperties.MAPS_SET_SPAWN_PLAYER_TEAM.toString())
                                        .replace("%template%", template.getName())
                                        .replace("%number%", currentSpawns + "")
                                        .replace("%team%", team + "")
                                        .replace("%left%", (template.getTeamSize() - currentSpawns) + "");
                            } else {
                                message = plugin.getMessages().getMessage(MessageProperties.MAPS_SET_SPAWN_PLAYER_TEAM_FINAL.toString())
                                        .replace("%template%", template.getName())
                                        .replace("%number%", currentSpawns + "")
                                        .replace("%team%", team + "");
                            }
                        }
                        break;
                    case TEAM_UPDATED:
                        plugin.getMessages().getMessage(MessageProperties.MAPS_SET_SPAWN_PLAYER_TEAM_FINAL.toString())
                                .replace("%template%", template.getName())
                                .replace("%number%", (template.getTeamSpawnpoints().get(team - 1).size()) + "")
                                .replace("%team%", team + "");
                        break;
                    case MAX_TEAM_SPAWNS_REACHED:
                        plugin.getMessages().getMessage(MessageProperties.MAPS_SET_SPAWN_PLAYER_TEAM_MAX.toString())
                                .replace("%template%", template.getName())
                                .replace("%team%", team + "");
                        break;
                }

                if (result.isSuccess()) {
                    plugin.getMessages().getMessage(MessageProperties.TITLES_MAPS_SET_SPAWN_PLAYER.toString()).replace("%template%", template.getName()).sendTitle(player);
                    location.getWorld().setBlockAt(blockLocation, AbstractItem.getItem("BEACON"));
                    player.teleport(player.getLocation().add(0, 1, 0));
                } else {
                    plugin.getMessages().getMessage(MessageProperties.TITLES_MAPS_SET_SPAWN_PLAYER_FAIL.toString()).replace("%template%", template.getName()).sendTitle(player);
                }
                if (message != null) message.send(sender);
                break;
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
            if (sender instanceof SWPlayer) {
                SpawnType type = SpawnType.fromString(args[0]);
                if (type == SpawnType.PLAYER) {
                    GameInstance world = plugin.getGameManager().getGameWorldByName(((SWPlayer) sender).getLocation().getWorld().getName());
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
