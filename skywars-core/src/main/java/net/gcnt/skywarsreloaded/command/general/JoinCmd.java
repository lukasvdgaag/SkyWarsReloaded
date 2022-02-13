package net.gcnt.skywarsreloaded.command.general;

import net.gcnt.skywarsreloaded.SkyWarsReloaded;
import net.gcnt.skywarsreloaded.command.Cmd;
import net.gcnt.skywarsreloaded.game.GamePlayer;
import net.gcnt.skywarsreloaded.game.GameTemplate;
import net.gcnt.skywarsreloaded.game.GameWorld;
import net.gcnt.skywarsreloaded.utils.properties.MessageProperties;
import net.gcnt.skywarsreloaded.wrapper.player.SWPlayer;
import net.gcnt.skywarsreloaded.wrapper.sender.SWCommandSender;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class JoinCmd extends Cmd {

    public JoinCmd(SkyWarsReloaded plugin) {
        super(plugin, "skywars", "join", "skywars.command.join", true, "", "Join a game.", "j");
    }

    @Override
    public boolean run(SWCommandSender sender, String[] args) {
        List<GameWorld> worlds = plugin.getGameManager().getGameWorlds().stream()
                .filter(GameWorld::canJoin)
                .sorted(Comparator.comparingInt(o -> o.getAlivePlayers().size()))
                .collect(Collectors.toList());

        if (worlds.isEmpty()) {
            plugin.getMessages().getMessage(MessageProperties.GAMES_NONE_AVAILABLE.toString()).send(sender);
            return true;
        }

        // todo make this random?
        GameWorld world = worlds.get(0);
        GamePlayer gamePlayer = world.preparePlayerJoin(((SWPlayer) sender).getUuid(), false);
        boolean joined = world.addPlayers(gamePlayer);
        if (!joined) {
            plugin.getMessages().getMessage(MessageProperties.GAMES_JOIN_FAILED.toString()).send(sender);
            return true;
        }
        return true;
    }

    @Override
    public List<String> onTabCompletion(SWCommandSender sender, String[] args) {
        return new ArrayList<>();
    }
}
